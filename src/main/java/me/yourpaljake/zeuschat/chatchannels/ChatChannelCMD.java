package me.yourpaljake.zeuschat.chatchannels;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ChatChannelCMD extends Command implements PluginIdentifiableCommand {

    private ChatChannel chatChannel;
    private Plugin plugin;

    public ChatChannelCMD(String name, String description, String usage, List<String> aliases, Plugin plugin){
        super(name, description, usage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        return false;
    }

    public Plugin getPlugin(){
        return plugin;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return null;
    }
}
