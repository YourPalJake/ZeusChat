package me.yourpaljake.zeuschat;

import me.yourpaljake.zeuschat.chatchannels.ChatChannels;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZeusChat extends JavaPlugin {

    private ChatChannels chatChannels;

    @Override
    public void onEnable(){
        if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            Bukkit.getLogger().warning("+===============================================================+");
            Bukkit.getLogger().warning("         Error I am missing the following Dependency:            ");
            Bukkit.getLogger().warning("    https://www.spigotmc.org/resources/placeholderapi.6245/      ");
            Bukkit.getLogger().warning("                    I will now shutdown!                         ");
            Bukkit.getLogger().warning("+===============================================================+");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        this.chatChannels = new ChatChannels();
    }

    @Override
    public void onDisable(){

    }

}
