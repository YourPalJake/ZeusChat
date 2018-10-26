package me.yourpaljake.zeuschat.events;

import me.yourpaljake.zeuschat.ZeusChat;
import me.yourpaljake.zeuschat.chatchannels.ChatChannel;
import me.yourpaljake.zeuschat.chatchannels.ChatChannels;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEvent implements Listener {

    private ChatChannels chatChannelsModule;

    public PlayerChatEvent(){
        chatChannelsModule = ZeusChat.getModuleRegistry().getChatChannels();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        String[] splitMessage = event.getMessage().split(" ");
        if(ZeusChat.getModuleRegistry().getEnabledModules().contains(chatChannelsModule)) {
            String prefix = splitMessage[0];
            ChatChannel chatChannel = chatChannelsModule.getChatChannelByPrefix(prefix);
            if(chatChannel != null){
                chatChannel.sendMessage(p, event.getMessage().replaceFirst(prefix, "").trim());
                event.setCancelled(true);
                return;
            }
            if (chatChannelsModule.isToggled(p)) {
                chatChannelsModule.getToggled(p).sendMessage(p, event.getMessage());
                event.setCancelled(true);
            }
        }
    }
}
