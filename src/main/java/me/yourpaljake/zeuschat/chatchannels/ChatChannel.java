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
    private String playerFormat;
    private String format;

    private String readPermission;
    private String writePermission;
    private String toggleotherPermission;
    private String colorPermission;

    private boolean color;
    private boolean logToConsole;

    private ChatChannelCMD command;

    public ChatChannel(String name
            , String prefix
            , String playerFormat
            , String format
            , String readPermission
            , String writePermission
            , String toggleotherPermission
            , String colorPermission
            , boolean color
            , boolean logToConsole
            , ConfigurationSection commandSection
            , Plugin plugin){
        this.name = name;
        this.prefix = prefix;
        this.playerFormat = playerFormat;
        this.format = format;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.toggleotherPermission = toggleotherPermission;
        this.colorPermission = colorPermission;
        this.color = color;
        this.logToConsole = logToConsole;
        if(commandSection.getBoolean("enabled")) {
            this.command = new ChatChannelCMD(commandSection.getString("name")
                    , commandSection.getString("description")
                    , commandSection.getString("usagemessage")
                    , commandSection.getStringList("aliases")
                    , plugin
                    ,this);
            command.register();
        }else this.command = null; //Since its disabled in the config
    }

    /**
     * Unloads the ChatChannel
     * NOTE this will also disable the command!
     */
    public void unload(){
        if(command != null) { //Its null when the command is disabled in the config
            command.unregister();
        }
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
     * Get the ChatChannel playerFormat
     *
     * @return ChatChannel playerFormat
     */
    public String getPlayerFormat() {
        return playerFormat;
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
     * get the toggleotherPermission
     *
     * @return toggleotherPermission
     */
    public String getToggleotherPermission(){
        return toggleotherPermission;
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

    /**
     * Send a message to the chatchannel
     *
     * @param sender The player sending the message
     * @param message The message
     */
    public void sendMessage(Player sender, String message){
        if(sender.hasPermission(writePermission)){
            if(sender.hasPermission(colorPermission) && color){
                //Call event for API
                ChatChannelMessageEvent ccme = new ChatChannelMessageEvent(sender, message, this);
                Bukkit.getPluginManager().callEvent(ccme);
                if(ccme.isCancelled()) return; //See if any plugin using the API cancelled the event
                String translatedPlayerFormat = PlaceholderAPI.setPlaceholders(sender, ChatColor.translateAlternateColorCodes('&', this.playerFormat));
                String translatedMessage =  ChatColor.translateAlternateColorCodes('&', message);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(player.hasPermission(readPermission)){
                        player.sendMessage(translatedPlayerFormat + translatedMessage);
                    }
                }
                if(logToConsole) Bukkit.getLogger().info(translatedPlayerFormat + translatedMessage);
                return;
            }
            //Call event for API
            ChatChannelMessageEvent ccme = new ChatChannelMessageEvent(sender, message, this);
            Bukkit.getPluginManager().callEvent(ccme);
            if(ccme.isCancelled()) return; //See if any plugin using the API cancelled the event
            String translatedPlayerFormat = PlaceholderAPI.setPlaceholders(sender, ChatColor.translateAlternateColorCodes('&', this.playerFormat));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(player.hasPermission(readPermission)){
                    player.sendMessage(translatedPlayerFormat + message);
                }
            }
            if(logToConsole) Bukkit.getLogger().info(translatedPlayerFormat + message);
        }
    }

    public void sendMessage(String sender, String message){
        String translatedFormat = ChatColor.translateAlternateColorCodes('&', format).replace("{sender}", sender);
        String translatedMessage =  ChatColor.translateAlternateColorCodes('&', message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission(readPermission)){
                player.sendMessage(translatedFormat + translatedMessage);
            }
        }
        if(logToConsole) Bukkit.getLogger().info(translatedFormat + translatedMessage);
    }
}
