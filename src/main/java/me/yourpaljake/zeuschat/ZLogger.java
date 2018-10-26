package me.yourpaljake.zeuschat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ZLogger {
    private boolean logColor;
    private boolean debug;

    ZLogger(){
        logColor = FileManager.getConfig().getBoolean("core-settings.log-color");
        debug = FileManager.getConfig().getBoolean("core-settings.debug");
    }

    public void log(boolean usePrefix, String message){
        if(usePrefix){
            String translatedMessage = ChatColor.translateAlternateColorCodes('&', "&3[&bZeusChat3] &r" + message);
            if(!logColor) translatedMessage = ChatColor.stripColor(translatedMessage);
            Bukkit.getConsoleSender().sendMessage(translatedMessage);
        }else{
            String translatedMessage = ChatColor.translateAlternateColorCodes('&', message);
            if(!logColor) translatedMessage = ChatColor.stripColor(translatedMessage);
            Bukkit.getConsoleSender().sendMessage(translatedMessage);
        }
    }

    public void debug(String message){
        if(debug) log(true, "&4[&cDEBUG&4] &r" + message);
    }
}
