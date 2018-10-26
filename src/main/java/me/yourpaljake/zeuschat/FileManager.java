package me.yourpaljake.zeuschat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;

@SuppressWarnings("all")
public final class FileManager {

    private static FileConfiguration configFile;
    private static File cfile;

    private FileManager(){}

    public static void setup(Plugin plugin){
        if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        cfile = new File(plugin.getDataFolder(), "config.yml");
        if(!cfile.exists()) copy(plugin.getResource("config.yml"), cfile);
        configFile = YamlConfiguration.loadConfiguration(cfile);
    }

    public static FileConfiguration getConfig(){
        return configFile;
    }

    public static void saveConfig(){
        try{
            configFile.save(cfile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void reloadConfig(){
        configFile = YamlConfiguration.loadConfiguration(cfile);
    }

    private static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
