package me.yourpaljake.zeuschat.chatchannels;

import me.clip.placeholderapi.PlaceholderAPI;
import me.yourpaljake.zeuschat.api.events.ChatChannelMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("all")
public class ChatChannel {

    private String name;
    private String prefix;
    private String format;

    private String readPermission;
    private String writePermission;
    private String colorPermission;

    private boolean color;

    private ChatChannelCMD command;

    public ChatChannel(String name
            , String prefix
            , String format
            , String readPermission
            , String writePermission
            , String colorPermission
            , boolean color
            , ConfigurationSection commandSection
            , Plugin plugin){
        this.name = name;
        this.prefix = prefix;
        this.format = format;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.colorPermission = colorPermission;
        this.color = color;
        this.command = new ChatChannelCMD(commandSection.getString("name")
                , commandSection.getString("description")
                , commandSection.getString("usage")
                , commandSection.getStringList("aliases")
                , plugin);
    }

    /**
     * Get the ChatChannel name
     *
     * @return ChatChannel name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ChatChannel prefix
     *
     * @return ChatChannel prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get the ChatChannel format
     *
     * @return ChatChannel format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Get the readPermission
     *
     * @return readPermission
     */
    public String getReadPermission() {
        return readPermission;
    }

    /**
     * get the writePermission
     *
     * @return writePermission
     */
    public String getWritePermission() {
        return writePermission;
    }

    /**
     * get the colorPermission
     *
     * @return colorPermission
     */
    public String getColorPermission() {
        return colorPermission;
    }

    /**
     * get the ChatChannel command
     *
     * @return ChatChannel command instance
     */
    public ChatChannelCMD getCommand(){
        return command;
    }

    public void sendMessage(Player sender, String message){
        if(sender.hasPermission(writePermission)){
            if(sender.hasPermission(colorPermission) && color){
                //Call event for API
                ChatChannelMessageEvent ccme = new ChatChannelMessageEvent(sender, message, this);
                Bukkit.getPluginManager().callEvent(ccme);
                if(ccme.isCancelled()) return; //See if any plugin using the API cancelled the event
                String translatedFormat = PlaceholderAPI.setPlaceholders(sender, ChatColor.translateAlternateColorCodes('&', this.format));
                String translatedMessage =  ChatColor.translateAlternateColorCodes('&', message);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(player.hasPermission(readPermission)){
                        player.sendMessage(translatedFormat + translatedMessage);
                    }
                }
                return;
            }
            //Call event for API
            ChatChannelMessageEvent ccme = new ChatChannelMessageEvent(sender, message, this);
            Bukkit.getPluginManager().callEvent(ccme);
            if(ccme.isCancelled()) return; //See if any plugin using the API cancelled the event
            String translatedFormat = PlaceholderAPI.setPlaceholders(sender, ChatColor.translateAlternateColorCodes('&', this.format));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(player.hasPermission(readPermission)){
                    player.sendMessage(translatedFormat + message);
                }
            }
        }
    }
}
