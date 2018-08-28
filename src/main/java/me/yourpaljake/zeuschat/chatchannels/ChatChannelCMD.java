package me.yourpaljake.zeuschat.chatchannels;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ChatChannelCMD extends Command {

    private ChatChannel chatChannel;

    public ChatChannelCMD(String name, String description, String usage, List<String> aliases){
        super(name, description, usage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return null;
    }
}
