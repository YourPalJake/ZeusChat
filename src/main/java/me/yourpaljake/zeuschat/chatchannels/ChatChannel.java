package me.yourpaljake.zeuschat.chatchannels;

import me.clip.placeholderapi.PlaceholderAPI;
import me.yourpaljake.zeuschat.ZeusChat;
import me.yourpaljake.zeuschat.api.events.ChatChannelMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChatChannel {

    private String name;
    private String prefix;
    private String playerFormat;
    private String format;

    private String readPermission;
    private String writePermission;
    private String toggleOtherPermission;
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
            , String toggleOtherPermission
            , String colorPermission
            , boolean color
            , boolean logToConsole
            , ConfigurationSection commandSection
            , ChatChannels chatChannelsModule
            , Plugin plugin){
        this.name = name;
        this.prefix = prefix;
        this.playerFormat = playerFormat;
        this.format = format;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.toggleOtherPermission = toggleOtherPermission;
        this.colorPermission = colorPermission;
        this.color = color;
        this.logToConsole = logToConsole;
        if(commandSection.getBoolean("enabled")) {
            this.command = new ChatChannelCMD(commandSection.getString("name")
                    , commandSection.getString("description")
                    , commandSection.getString("usagemessage")
                    , commandSection.getStringList("aliases")
                    , plugin
                    ,this
                    , chatChannelsModule);
            ZeusChat.getZLogger().debug("&fRegistering ChatChanelCMD for " + this.getName());
            ZeusChat.getCommandMap().register(commandSection.getString("name"), this.command);
        }else this.command = null; //Since its disabled in the config
        ZeusChat.getZLogger().debug("&fChatChannel " + this.getName() + " initialized");
    }

    /**
     * Unloads the ChatChannel
     * NOTE this will also disable the command!
     */
    public void unload(){
        if(command != null) { //Its null when the command is disabled in the config
            ZeusChat.getCommandMap().getCommand(command.getName()).unregister(ZeusChat.getCommandMap());
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
     * get the toggleOtherPermission
     *
     * @return toggleOtherPermission
     */
    public String getToggleOtherPermission(){
        return toggleOtherPermission;
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
                if(logToConsole) ZeusChat.getZLogger().log(false, translatedPlayerFormat + translatedMessage);
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
            if(logToConsole) ZeusChat.getZLogger().log(false, translatedPlayerFormat + message);
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
        if(logToConsole) ZeusChat.getZLogger().log(false, translatedFormat + translatedMessage);
    }
}
