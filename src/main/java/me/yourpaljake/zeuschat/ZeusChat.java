package me.yourpaljake.zeuschat;

import me.yourpaljake.athoslibrary.AthosLibrary;
import me.yourpaljake.athoslibrary.command.CommandBuilder;
import me.yourpaljake.athoslibrary.command.CommandObject;
import me.yourpaljake.athoslibrary.file.ConfigCommand;
import me.yourpaljake.athoslibrary.plugin.AthosPlugin;
import me.yourpaljake.athoslibrary.plugin.PluginBuilder;
import me.yourpaljake.zeuschat.commands.ClearChatCMD;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZeusChat extends JavaPlugin {
    private AthosPlugin athosPlugin;

    @Override
    public void onEnable(){
        if(!Bukkit.getPluginManager().isPluginEnabled("AthosLibrary")){
            Bukkit.getLogger().severe("+=================================={ Warning }==================================+");
            Bukkit.getLogger().severe("Could not find AthosLibrary, this is required for me to run!");
            Bukkit.getLogger().severe("Download it here: https://www.spigotmc.org/resources/athoslibrary.51992/");
            Bukkit.getLogger().severe("Disabling........");
            Bukkit.getLogger().severe("+===============================================================================+");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        initALPlugin();
        FileManager.setup(this);
        registerCommands();

    }

    @Override
    public void onDisable(){

    }


    private void initALPlugin(){
        PluginBuilder builder = new PluginBuilder();
        builder.setName("ZeusChat");
        builder.setDescription("Manage your chat like Zeus!");
        builder.setPlugin(this);
        builder.useCommandSystem();
        builder.useCommandFile();
        AthosPlugin athosPlugin = builder.build();
        AthosLibrary.getPluginManager().loadPlugin(athosPlugin);
        this.athosPlugin = athosPlugin;
    }

    private void registerCommands(){
        //ClearChat
        ConfigCommand command = new ConfigCommand(this.athosPlugin, "commands.clearchat");
        if(command.isEnabled()) {
            CommandBuilder builder = new CommandBuilder();
            builder.setName(command.getName());
            builder.setDescription(command.getDescription());
            builder.setUsage(command.getUsage());
            builder.addAliases(command.getAliases());
            builder.withOwner(this);
            CommandObject clearchatObj = builder.build();
            clearchatObj.setExecutor(new ClearChatCMD());
            this.athosPlugin.getCommandHandler().register("clearchat", clearchatObj);
        }
    }


}
