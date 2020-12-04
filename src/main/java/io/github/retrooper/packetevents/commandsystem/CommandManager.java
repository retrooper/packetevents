package io.github.retrooper.packetevents.commandsystem;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandManager implements CommandExecutor {
    private final HashMap<String, Method> commands = new HashMap<>();
    private final HashMap<Method, Class<?>> classes = new HashMap<>();
    private SimpleCommandMap commandMap;
    private final Plugin plugin;

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
        if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager spm = (SimplePluginManager) plugin.getServer().getPluginManager();
            try {
                Field map = spm.getClass().getDeclaredField("commandMap");
                map.setAccessible(true);
                commandMap = (SimpleCommandMap) map.get(spm);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Bukkit has no PluginManager");
        }
    }

    public void register(Class<?> clazz) {

        for (Method method : clazz.getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(Command.class)
                    && method.getParameterTypes()[0].equals(CommandArgument.class)
                    && method.getParameterTypes().length == 1) {
                Command command = method.getAnnotation(Command.class);
                commands.putIfAbsent(command.arguments(), method);
                PacketEventsCommand packetEventsCommand = new PacketEventsCommand(
                        command.arguments().split("\\.")[0], command.description(),
                        command.usageMessage(), Arrays.asList(command.aliases()), this
                );
                classes.put(method, clazz);
                commandMap.register(plugin.getName(), packetEventsCommand);
                Bukkit.getLogger().info("Registered command " + command.arguments() + " in class " + clazz.getSimpleName() + ".");
            }
        }
    }

    public void register(Object o) {
        register(o.getClass());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        List<String> argsList = new ArrayList<>();
        String[] a;
        if(label.contains(":")) {
            String[] splitLabel = (label.replace(label.split(":")[0] + ":", "")).split(":");
            argsList.addAll(Arrays.asList(splitLabel));
            argsList.addAll(Arrays.asList(args));
            a = argsList.toArray(new String[0]);
        } else a = args;
        for(int arg = a.length; arg >= 0 ; arg--) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < arg; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }
            String builderString = buffer.toString();
            if(commands.containsKey(builderString)) {
                Method m = commands.get(builderString);
                Command anno = m.getAnnotation(Command.class);
                Bukkit.getLogger().info("Run command: " + anno.arguments());
                CommandArgument ca = new CommandArgument(commandSender, a);
                try {
                    m.invoke(classes.get(m).getConstructor().newInstance(), ca);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
