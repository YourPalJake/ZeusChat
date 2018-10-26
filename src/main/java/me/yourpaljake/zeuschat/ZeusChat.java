package me.yourpaljake.zeuschat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZeusChat extends JavaPlugin {

    private static ModuleRegistry moduleRegistry;

    @Override
    public void onEnable(){
        Metrics metrics = new Metrics(this);
        if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            Bukkit.getLogger().warning("+===============================================================+");
            Bukkit.getLogger().warning("         Error I am missing the following Dependency:            ");
            Bukkit.getLogger().warning("    https://www.spigotmc.org/resources/placeholderapi.6245/      ");
            Bukkit.getLogger().warning("                    I will now shutdown!                         ");
            Bukkit.getLogger().warning("+===============================================================+");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        moduleRegistry = new ModuleRegistry(this);
        moduleRegistry.loadModules();
    }

    @Override
    public void onDisable(){
        moduleRegistry.unloadModules();
    }

    public static ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }
}
