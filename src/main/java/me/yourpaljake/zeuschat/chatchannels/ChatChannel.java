package me.yourpaljake.zeuschat.chatchannels;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ChatChannel {

    private String name;
    private String prefix;
    private String format;

    private String readPermission;
    private String writePermission;
    private String colorPermission;

    private boolean color;

    private ChatChannelCMD command;

    public ChatChannel(String name, String prefix, String format, String readPermission, String writePermission, String colorPermission, boolean color, ConfigurationSection commandSection){
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
                , commandSection.getStringList("aliases"));
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getFormat() {
        return format;
    }

    public String getReadPermission() {
        return readPermission;
    }

    public String getWritePermission() {
        return writePermission;
    }

    public String getColorPermission() {
        return colorPermission;
    }

    public ChatChannelCMD getCommand(){
        return command;
    }

    public void sendMessage(Player sender, String message){
        if(sender.hasPermission(writePermission)){
            if(sender.hasPermission(colorPermission) && color){
                String format = PlaceholderAPI.setPlaceholders(sender, ChatColor.translateAlternateColorCodes('&', this.format + message));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(player.hasPermission(readPermission)){
                        player.sendMessage(format);
                    }
                }
                return;
            }
            String format = PlaceholderAPI.setPlaceholders(sender, ChatColor.translateAlternateColorCodes('&', this.format));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(player.hasPermission(readPermission)){
                    player.sendMessage(format + message);
                }
            }
        }
    }
}
