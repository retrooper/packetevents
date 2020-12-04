package io.github.retrooper.packetevents.commandsystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PacketEventsCommand extends Command {
    private final CommandExecutor executor;
    public PacketEventsCommand(String name, String description, String usageMessage, List<String> aliases, CommandExecutor executor) {
        super(name, description, usageMessage, aliases);
        this.executor = executor;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        executor.onCommand(commandSender, this, s, strings);
        return false;
    }
}
