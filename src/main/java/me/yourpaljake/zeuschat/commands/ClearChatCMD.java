package me.yourpaljake.zeuschat.commands;

import me.yourpaljake.athoslibrary.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ClearChatCMD extends BaseCommand {

    @Override
    public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender){
            for(int i = 0; i <= 100; i++){
                Bukkit.broadcastMessage(" ");
            }
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The chat has been cleared by &bCONSOLE"));
            return true;
        }
        return false;
    }
}
