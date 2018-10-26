package me.yourpaljake.zeuschat;

import org.bukkit.plugin.Plugin;

public interface IModule {

    boolean loadModule(Plugin plugin);

    boolean unloadModule();

    String getName();

    String getDescription();

}
