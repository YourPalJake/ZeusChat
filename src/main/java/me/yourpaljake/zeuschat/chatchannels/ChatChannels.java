package me.yourpaljake.zeuschat.chatchannels;

import me.yourpaljake.zeuschat.FileManager;
import me.yourpaljake.zeuschat.IModule;
import me.yourpaljake.zeuschat.ZeusChat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("all")
public class ChatChannels implements IModule {

    private HashMap<String, ChatChannel> chatChannels = new HashMap<>();
    private HashMap<String, ChatChannel> prefixes = new HashMap<>();
    private HashMap<UUID, ChatChannel> toggled = new HashMap<>();
    private HashMap<UUID, ChatChannel> toggledHistory = new HashMap<>();

    private ConfigurationSection configSect;

    private boolean forceToggle;

    @Override
    public boolean loadModule(Plugin plugin){
        configSect = FileManager.getConfig().getConfigurationSection("chatchannels");
        if(!configSect.getBoolean("enabled")) return false;
        for (String chatChannelName : configSect.getConfigurationSection("channels").getKeys(false)) {
            ZeusChat.getZLogger().debug("&fLoading chatchannel: " + chatChannelName);
            ConfigurationSection chatChannelConfigSect = configSect.getConfigurationSection("channels." + chatChannelName);
            ChatChannel chatChannel = new ChatChannel(chatChannelName
                    , chatChannelConfigSect.getString("prefix")
                    , chatChannelConfigSect.getString("playerformat")
                    , chatChannelConfigSect.getString("format")
                    , chatChannelConfigSect.getString("permissions.readpermission")
                    , chatChannelConfigSect.getString("permissions.writepermission")
                    , chatChannelConfigSect.getString("permissions.toggleotherpermission")
                    , chatChannelConfigSect.getString("permissions.colorpermission")
                    , chatChannelConfigSect.getBoolean("color")
                    , chatChannelConfigSect.getBoolean("logtoconsole")
                    , chatChannelConfigSect.getConfigurationSection("command")
                    , this
                    , plugin);
            chatChannels.put(chatChannelName, chatChannel);
            prefixes.put(chatChannelConfigSect.getString("prefix"), chatChannel);
        }
        forceToggle = configSect.getBoolean("forcetoggle");
        ZeusChat.getZLogger().debug("&fChatChannels module loaded");
        return true;
    }

    @Override
    public boolean unloadModule() {
        //TODO Store toggles
        for (ChatChannel chatChannel : chatChannels.values()) {
            chatChannel.unload();
            chatChannels.remove(chatChannel.getName());
            prefixes.remove(chatChannel.getPrefix());
        }
        return chatChannels.isEmpty() && prefixes.isEmpty();
    }

    @Override
    public String getName() {
        return "ChatChannels";
    }

    @Override
    public String getDescription() {
        return "Talk in secret channels that only people with the right permission can read!";
    }

    /**
     * Get ChatChannel object
     *
     * @param name The name of the ChatChannel
     * @return specified ChatChannel instance, null if doesn't exist
     */
    public ChatChannel getChatChannel(String name){
        if(chatChannels.containsKey(name)) return chatChannels.get(name);
        return null;
    }

    /**
     * Get ChatChannel object using a prefix
     *
     * @param prefix The prefix
     * @return ChatChannel using specified prefix, null if doesn't exist
     */
    public ChatChannel getChatChannelByPrefix(String prefix){
        if(prefixes.containsKey(prefix)) return prefixes.get(prefix);
        return null;
    }

    /**
     * See if the forceToggle feature is enabled
     *
     * @return forceToggle
     */
    public boolean isForceToggle(){
        return forceToggle;
    }

    /**
     * Checks if the player is toggled to any channel
     *
     * @param uuid The player's uuid
     * @return toggled
     */
    public boolean isToggled(UUID uuid){
        return toggled.containsKey(uuid);
    }

    /**
     * Checks if the player is toggled to any channel
     *
     * @param player The player instance
     * @return toggled
     */
    public boolean isToggled(Player player){
        return isToggled(player.getUniqueId());
    }

    /**
     * Check if the player has a toggled history
     *
     * @param uuid The player's uuid
     * @return toggledHistory
     */
    public boolean hasToggledHistory(UUID uuid) { return toggledHistory.containsKey(uuid); }

    /**
     * Check if the player has a toggled history
     *
     * @param player The player instance
     * @return toggledHistory
     */
    public boolean hasToggledHistory(Player player) { return hasToggledHistory(player.getUniqueId()); }

    /**
     * Gets the ChatChannel the player is toggled to
     *
     * @param uuid The player's uuid
     * @return ChatChannel instance, null if not toggled to any ChatChannel
     */
    public ChatChannel getToggled(UUID uuid){
        if(toggled.containsKey(uuid)) return toggled.get(uuid);
        return null;
    }

    /**
     * Gets the ChatChannel the player is toggled to
     *
     * @param player The player's instance
     * @return ChatChannel instance, null if not toggled to any ChatChannel
     */
    public ChatChannel getToggled(Player player){
        return getToggled(player.getUniqueId());
    }

    /**
     * Gets the previous ChatChannel the player was toggled to
     *
     * @param uuid The player's uuid
     * @return The previous ChatChannel instance, null if no previous ChatChannel
     */
    public ChatChannel getToggledHistory(UUID uuid){
        if(toggled.containsKey(uuid)) return toggledHistory.get(uuid);
        return null;
    }

    /**
     * Gets the previous ChatChannel the player was toggled to
     *
     * @param player The player's instance
     * @return The previous ChatChannel instance, null if no previous ChatChannel
     */
    public ChatChannel getToggledHistory(Player player){
        return getToggledHistory(player.getUniqueId());
    }

    /**
     * Set a player toggled to a ChatChannel
     *
     * @param uuid The player's UUID
     * @param chatChannel The ChatChannel you wanna toggle the player to
     * @return true successful, false had no permission to the ChatChannel or was already toggled to that ChatChannel
     */
    public boolean setToggled(UUID uuid, ChatChannel chatChannel){
        if(chatChannel != null){
            if(!Bukkit.getServer().getPlayer(uuid).hasPermission(chatChannel.getWritePermission())) return false;
            if(toggled.containsKey(uuid) && forceToggle){
                if(toggledHistory.containsKey(uuid)) toggledHistory.remove(uuid);
                toggledHistory.put(uuid, toggled.get(uuid));
                toggled.remove(uuid);
                toggled.put(uuid, chatChannel);
                //TODO Send switch between channel message
                return true;
            }
            if(toggledHistory.containsKey(uuid)) toggledHistory.remove(uuid);
            toggled.put(uuid, chatChannel);
            //TODO Send toggle on message
        }else{
            if(toggled.containsKey(uuid)){
                toggledHistory.put(uuid, toggled.get(uuid));
                toggled.remove(uuid);
                //TODO Send toggle off message
                return true;
            }
        }
        return false;
    }

    /**
     * Set a player toggled to a ChatChannel
     *
     * @param player The player's instance
     * @param chatChannel The ChatChannel you wanna toggle the player to
     * @return true successful, false had no permission to the ChatChannel or was already toggled to that ChatChannel
     */
    public boolean setToggled(Player player, ChatChannel chatChannel){
        return setToggled(player.getUniqueId(), chatChannel);
    }


}
