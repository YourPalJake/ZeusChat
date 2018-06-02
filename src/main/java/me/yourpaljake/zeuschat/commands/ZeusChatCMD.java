package me.yourpaljake.zeuschat.commands;

import me.yourpaljake.athoslibrary.command.BaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class ZeusChatCMD extends BaseCommand implements TabCompleter {

    @Override
    public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
