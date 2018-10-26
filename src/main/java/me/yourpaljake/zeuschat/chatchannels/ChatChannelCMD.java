package me.yourpaljake.zeuschat.chatchannels;

import me.yourpaljake.zeuschat.ZeusChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ChatChannelCMD extends BukkitCommand implements PluginIdentifiableCommand {

    private ChatChannel chatChannel;
    private Plugin plugin;
    private ChatChannels chatChannelsModule;

    ChatChannelCMD(String name, String description, String usageMessage, List<String> aliases, Plugin plugin, ChatChannel chatChannel, ChatChannels chatChannelsModule){
        super(name, description, usageMessage, aliases);
        this.plugin = plugin;
        this.chatChannel = chatChannel;
        this.chatChannelsModule = chatChannelsModule;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)){
            if(sender instanceof ConsoleCommandSender){
                if(args.length == 0) {
                    ZeusChat.getZLogger().log(false, this.usageMessage);
                    return false;
                }
                switch (args[0]){
                    case "say":
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i=1; i < args.length; i++){
                            stringBuilder.append(args[i]).append(" ");
                        }
                        chatChannel.sendMessage("CONSOLE", stringBuilder.toString().trim());
                        break;
                    case "toggle":
                        if(args.length == 1){
                            ZeusChat.getZLogger().log(false, "&cConsole cannot get toggled to chatchannels!");
                            return false;
                        }else{
                            Player target = Bukkit.getPlayer(args[1]);
                            if(target == null){
                                sender.sendMessage(ChatColor.RED + "Invalid player!");
                                ZeusChat.getZLogger().log(false, "&cInvalid player!");
                                return false;
                            }
                            if(chatChannelsModule.isToggled(target)) {
                                ChatChannel currentChatChannel = chatChannelsModule.getToggled(target);
                                if (currentChatChannel != chatChannel) {
                                    if (chatChannelsModule.setToggled(target, chatChannel)) {
                                        ZeusChat.getZLogger().log(false, "&aSuccessfully toggled " + target.getName() + " from " + currentChatChannel.getName() + " to " + chatChannel.getName());
                                        break;
                                    }
                                    ZeusChat.getZLogger().log(false, "&cSpecified player has no permission to this chatchannel!");
                                    return false;
                                } else {
                                    chatChannelsModule.setToggled(target, null);
                                    ZeusChat.getZLogger().log(false, "&aSuccessfully toggled " + target.getName() + " off from " + chatChannel.getName());
                                    break;
                                }
                            }else {
                                if(chatChannelsModule.setToggled(target, chatChannel)) {
                                    ZeusChat.getZLogger().log(false, "&aSuccessfully toggled " + target.getName() + " on to " + chatChannel.getName());
                                    break;
                                }
                                ZeusChat.getZLogger().log(false, "&cSpecified player has no permission to this chatchannel!");
                                return false;
                            }
                        }
                    default:
                        ZeusChat.getZLogger().log(false, this.usageMessage);
                        return false;
                }
                return true;
            }
            return false;
        }
        Player p = (Player)sender;
        if(p.hasPermission(chatChannel.getWritePermission())){
            if(args.length == 0){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.usageMessage));
                return false;
            }
            switch (args[0]){
                case "say":
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i=1; i < args.length; i++){
                        stringBuilder.append(args[i]).append(" ");
                    }
                    chatChannel.sendMessage(p, stringBuilder.toString().trim());
                    break;
                case "toggle":
                    if(args.length == 1){
                        if(chatChannelsModule.isToggled(p)) {
                            ChatChannel currentChatChannel = chatChannelsModule.getToggled(p);
                            if (currentChatChannel != chatChannel) {
                                if (chatChannelsModule.isForceToggle()) {
                                    chatChannelsModule.setToggled(p, chatChannel);
                                    break;
                                }
                                //TODO send already toggled to other channel message
                                return false;
                            }else{
                                chatChannelsModule.setToggled(p, null);
                                break;
                            }
                        }
                        chatChannelsModule.setToggled(p, chatChannel);
                        break;
                    }else{
                        if(p.hasPermission(chatChannel.getToggleOtherPermission())) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target == null) {
                                //TODO send sender invalid player message
                                return false;
                            }
                            if (chatChannelsModule.isToggled(target)) {
                                ChatChannel currentChatChannel = chatChannelsModule.getToggled(target);
                                if (currentChatChannel != chatChannel) {
                                    if (chatChannelsModule.isForceToggle()) {
                                        if (chatChannelsModule.setToggled(target, chatChannel)) {
                                            //TODO send player successfully toggled other to the current
                                            break;
                                        }
                                        //TODO send sender specified player has no permission to this channel message
                                        return false;
                                    } else {
                                        //TODO send sender message that forcetoggle isn't enabled
                                        return false;
                                    }
                                }else{
                                    chatChannelsModule.setToggled(target, null);
                                    //TODO send sender successfully toggled off
                                    break;
                                }
                            }else {
                                if(chatChannelsModule.setToggled(target, chatChannel)) {
                                    //TODO send sender successfully toggled on
                                    break;
                                }
                                //TODO send sender specified player has no permission to this channel message
                                return false;
                            }
                        }
                        //TODO send player no permission message
                        return false;
                    }
                default:
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.usageMessage));
                    return false;
            }
            return true;
        }
        //TODO send no permission message to sender
        return false;
    }

    /**
     * Get the plugin associated with the command
     *
     * @return The plugin instance
     */
    public Plugin getPlugin(){
        return plugin;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if(args[0].equals("")){
            List<String> tabCompleteList = new ArrayList<>();
            tabCompleteList.add("toggle");
            tabCompleteList.add("say");
            return tabCompleteList;
        }
        if(args.length == 2 && args[0].equals("toggle") && sender.hasPermission(chatChannel.getToggleOtherPermission())){
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        return null;
    }
}
