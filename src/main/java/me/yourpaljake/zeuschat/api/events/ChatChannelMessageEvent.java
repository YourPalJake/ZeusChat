package me.yourpaljake.zeuschat.api.events;

import me.yourpaljake.zeuschat.chatchannels.ChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatChannelMessageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player sender;
    private String message;
    private ChatChannel chatChannel;
    private boolean cancelled = false;

    public ChatChannelMessageEvent(Player sender, String message, ChatChannel chatChannel){
        this.sender = sender;
        this.message = message;
        this.chatChannel = chatChannel;
    }

    /**
     * Returns sender
     *
     * @return Sender player instance
     */
    public Player getSender(){
        return sender;
    }

    /**
     * Get the message raw, that sender send
     *
     * @return Raw message
     */
    public String getMessage(){
        return message;
    }

    /**
     * Get the ChatChannel where the message is sent
     *
     * @return ChatChannel instance
     */
    public ChatChannel getChatChannel(){
        return chatChannel;
    }

    /**
     * Cancel the sending on the message
     *
     * @param cancelled True/False
     */
    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }

    /**
     * Check if the event has been set to cancel
     *
     * @return cancelled
     */
    public boolean isCancelled(){
        return cancelled;
    }

    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
