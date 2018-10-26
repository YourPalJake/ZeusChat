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

    ChatChannelCMD(String name, String description, String usageMessage, List<String> aliases, Plugin plugin, ChatChannel chatChannel){
        super(name, description, usageMessage, aliases);
        this.plugin = plugin;
        this.chatChannel = chatChannel;
        this.chatChannelsModule = ZeusChat.getModuleRegistry().getChatChannels();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)){
            if(sender instanceof ConsoleCommandSender){
                if(args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.usageMessage));
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
                            sender.sendMessage(ChatColor.RED + "Console cannot be toggled to chatchannels!");
                            return false;
                        }else{
                            Player target = Bukkit.getPlayer(args[1]);
                            if(target == null){sender.sendMessage(ChatColor.RED + "Invalid player!"); return false; }
                            ChatChannel currentChatChannel = chatChannelsModule.getToggled(target); //TODO fix NPE
                            if(currentChatChannel == null || currentChatChannel != chatChannel){
                                if(chatChannelsModule.setToggled(target, chatChannel)) {
                                    Bukkit.getLogger().info(ChatColor.GREEN + "Successfully toggled " + target.getName() + " to " + chatChannel.getName());
                                    break;
                                }
                                Bukkit.getLogger().info(ChatColor.RED + "Specified player has no permissions to this chatchannel");
                            }else{
                                chatChannelsModule.setToggled(target, null);
                                break;
                            }
                        }
                    default:
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.usageMessage));
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
                        ChatChannel currentChatChannel = chatChannelsModule.getToggled(p); //TODO fix NPE
                        if(currentChatChannel == null || currentChatChannel != chatChannel){
                            if(chatChannelsModule.isForceToggle()) {
                                chatChannelsModule.setToggled(p, chatChannel);
                                break;
                            }
                            //TODO send already toggled to other channel message
                            return false;
                        }
                        chatChannelsModule.setToggled(p, null);
                        break;
                    }else{
                        if(p.hasPermission(chatChannel.getToggleotherPermission())) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target == null) {
                                //TODO send sender invalid player message
                                return false;
                            }
                            ChatChannel currentChatChannel = chatChannelsModule.getToggled(target);
                            if (currentChatChannel == null || currentChatChannel != chatChannel) {
                                if (chatChannelsModule.setToggled(target, chatChannel)) {
                                    //TODO send player successfully toggled other to on
                                    break;
                                }
                                //TODO send sender specified player has no permission to this channel message
                            } else {
                                chatChannelsModule.setToggled(target, null);
                                //TODO send player successfully toggled other to off
                                break;
                            }
                        }
                        //TODO send player no permission message
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
        if(args.length == 0){
            List<String> tabCompleteList = new ArrayList<>();
            tabCompleteList.add("toggle");
            tabCompleteList.add("say");
            return tabCompleteList;
        }
        if(args.length == 2 && args[0].equals("toggle") && sender.hasPermission(chatChannel.getToggleotherPermission())){
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        return null;
    }
}
