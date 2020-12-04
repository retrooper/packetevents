package io.github.retrooper.packetevents.commandsystem;

import org.bukkit.command.CommandSender;

public class CommandArgument {
    private final CommandSender sender;
    private final String[] args;

    public CommandArgument(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }

    public CommandSender getSender() {
        return sender;
    }
}
