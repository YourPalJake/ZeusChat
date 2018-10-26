package me.yourpaljake.zeuschat.api;

import me.yourpaljake.zeuschat.ModuleRegistry;
import me.yourpaljake.zeuschat.ZeusChat;
import me.yourpaljake.zeuschat.chatchannels.ChatChannels;
import org.bukkit.plugin.Plugin;

public class ZeusChatAPI {
    private ModuleRegistry moduleRegistry;
    private ChatChannels chatChannels;

    public ZeusChatAPI(Plugin plugin){
        ZeusChat.hookAPI(plugin);
        moduleRegistry = ZeusChat.getModuleRegistry();
        chatChannels = moduleRegistry.getChatChannels();
    }

    /**
     * Returns ModuleRegistry
     *
     * @return ModuleRegistry instance
     */
    public ModuleRegistry getModuleRegistry(){
        return moduleRegistry;
    }

    /**
     * Returns ChatChannels module
     *
     * @return ChatChannels Instance, null if disabled
     */
    public ChatChannels getChatChannels(){
        return chatChannels;
    }

}
