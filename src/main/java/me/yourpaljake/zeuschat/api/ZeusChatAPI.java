package me.yourpaljake.zeuschat.api;

import me.yourpaljake.zeuschat.ModuleRegistry;
import me.yourpaljake.zeuschat.ZeusChat;
import me.yourpaljake.zeuschat.chatchannels.ChatChannels;
import org.bukkit.plugin.Plugin;

public class ZeusChatAPI {

    public ZeusChatAPI(Plugin plugin){

    }

    /**
     * Returns ModuleRegistry
     *
     * @return ModuleRegistry instance
     */
    public ModuleRegistry getModuleRegistry(){
        return ZeusChat.getModuleRegistry();
    }

    /**
     * Returns ChatChannels module
     *
     * @return ChatChannels Instance, null if disabled
     */
    public ChatChannels getChatChannels(){
        return ZeusChat.getModuleRegistry().getChatChannels();
    }

}
