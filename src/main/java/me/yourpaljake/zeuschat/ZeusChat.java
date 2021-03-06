package me.yourpaljake.zeuschat;

import me.yourpaljake.zeuschat.events.PlayerChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;

public final class ZeusChat extends JavaPlugin {

    private static ZLogger zLogger;
    private static ModuleRegistry moduleRegistry;
    private static CommandMap commandMap; //For internal uses.
    private static ZeusChat instance; //For internal/external uses;

    private static ArrayList<Plugin> pluginsUsingAPI = new ArrayList<>();

    @Override
    public void onEnable(){
        if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            Bukkit.getLogger().warning("+===============================================================+");
            Bukkit.getLogger().warning("         Error I am missing the following Dependency:            ");
            Bukkit.getLogger().warning("    https://www.spigotmc.org/resources/placeholderapi.6245/      ");
            Bukkit.getLogger().warning("                    I will now shutdown!                         ");
            Bukkit.getLogger().warning("+===============================================================+");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        instance = this;
        Metrics metrics = new Metrics(this);
        FileManager.setup(this);
        zLogger = new ZLogger();
        zLogger.log(true, "&fLogger initialized!");
        zLogger.debug("&fDebug mode &aon");
        fetchCommandMap();
        moduleRegistry = new ModuleRegistry(this);
        moduleRegistry.loadModules();
        registerEvents();
    }

    @Override
    public void onDisable(){
        moduleRegistry.unloadModules();
    }

    /**
     * For internal uses, fetches commandMap for the plugin
     */
    private void fetchCommandMap(){
        try{
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            zLogger.debug("&fFetched commandMap");
        }catch (NoSuchFieldException | IllegalAccessException exception){
            exception.printStackTrace();
            Bukkit.getLogger().warning("Failed to fetch commandMap, disabling....");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new PlayerChatEvent(), this);
    }

    /**
     * Get the Logger
     *
     * @return The zLogger instance
     */
    public static ZLogger getZLogger(){
        return zLogger;
    }

    /**
     * Get the moduleRegistry
     * NOTE for 3rd party plugins use the ZeusChatAPI
     * class to grab the ModuleRegistry
     *
     * @return The moduleRegistry instance
     */
    public static ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }

    /**
     * Get the bukkitCommandMap
     * NOTE do not use this method in 3rd party plugins
     * as this is only meant for internal uses
     *
     * @return The commandMap instance
     */
    public static CommandMap getCommandMap() { return commandMap; }

    /**
     * Get the instance of ZeusChat
     * NOTE this method is only meant for internal uses
     * or external for example the PlaceholderExpansion
     * that's required to use placeholders from this plugin.
     *
     * @return ZeusChat instance
     */
    public static ZeusChat getInstance(){
        return instance;
    }

    /**
     * Hook with the API to use in your plugin
     *
     * @param plugin Plugin's plugin instance
     */
    public static void hookAPI(Plugin plugin){
        pluginsUsingAPI.add(plugin);
    }
}
