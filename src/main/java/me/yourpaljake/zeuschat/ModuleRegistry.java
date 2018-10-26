package me.yourpaljake.zeuschat;

import me.yourpaljake.zeuschat.chatchannels.ChatChannels;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ModuleRegistry {

    private List<IModule> enabledModules = new ArrayList<>();

    private ChatChannels chatChannels = new ChatChannels();

    private Plugin plugin;

    ModuleRegistry(Plugin plugin){
        this.plugin = plugin;
        ZeusChat.getZLogger().debug("&fModuleRegistry initialized");
    }
    void loadModules(){
        ZeusChat.getZLogger().log(true, "Loading modules...");
        //ChatChannels
        ZeusChat.getZLogger().debug("&fLoading ChatChannels module");
        if(chatChannels.loadModule(plugin)) enabledModules.add(chatChannels);
    }

    void unloadModules(){
        for (IModule module : enabledModules) {
            module.unloadModule();
        }
    }

    /**
     * Load/reload a module
     * NOTE module must still be enabled in config to load
     *
     * @param moduleName The name of the module
     */
    public void loadModule(String moduleName){
        switch (moduleName){
            case "chatchannels":
                if(enabledModules.contains(chatChannels)){
                    chatChannels.unloadModule();
                    if(chatChannels.loadModule(plugin)) enabledModules.add(chatChannels);
                }
                if(chatChannels.loadModule(plugin)) enabledModules.add(chatChannels);
                break;
        }

    }

    /**
     * Enable a module in the config
     * NOTE the specified module will directly load
     *
     * @param moduleName The name of the module
     */
    public void enableModule(String moduleName){
        switch (moduleName){
            case "chatchannels":
                if(!enabledModules.contains(chatChannels)){
                    FileManager.getConfig().set("chatchannels.enabled", true);
                    FileManager.saveConfig();
                    loadModule(moduleName);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Disable a module in the config
     * NOTE the specified module will directly unload
     *
     * @param moduleName The name of the module
     */
    public void disableModule(String moduleName){
        switch (moduleName){
            case "chatchannels":
                if(enabledModules.contains(chatChannels)){
                    FileManager.getConfig().set("chatchannels.enabled", false);
                    FileManager.saveConfig();
                    chatChannels.unloadModule();
                    enabledModules.remove(chatChannels);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Get a list of enabled modules
     *
     * @return List containing enabled modules
     */
    public List<IModule> getEnabledModules(){
        return enabledModules;
    }

    /**
     * Get a list of all the available modules
     *
     * @return List containing all available modules
     */
    public List<IModule> getModules(){
        List<IModule> moduleList = new ArrayList<>();
        moduleList.add(chatChannels);
        return moduleList;
    }

    /**
     * Get ChatChannels module
     *
     * @return ChatChannels module instance, returns null if disabled
     */
    public ChatChannels getChatChannelsModule(){
        if(enabledModules.contains(chatChannels)) return chatChannels;
        return null;
    }

}
