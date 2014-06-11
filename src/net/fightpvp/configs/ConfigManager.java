package net.fightpvp.configs;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager
{
  private static ConfigManager instance = new ConfigManager();
  FileConfiguration config;
  File fconf;
  FileConfiguration kit;
  File fkits;
  FileConfiguration warp;
  File fwarps;

  public static ConfigManager getConfigs()
  {
    return instance;
  }

  public void setup(Plugin p)
  {
    this.fconf = new File(p.getDataFolder(), "config.yml");
    this.config = p.getConfig();

    if (!p.getDataFolder().exists()) {
      p.getDataFolder().mkdir();
    }

    this.fkits = new File(p.getDataFolder(), "kits.yml");

    if (!this.fkits.exists()) {
      try {
        this.fkits.createNewFile();
      } catch (IOException e) {
        Bukkit.getServer().getLogger().severe("§4[ERROR] §cNao foi possivel criar a file kits.yml");
        e.printStackTrace();
      }
    }

    this.kit = YamlConfiguration.loadConfiguration(this.fkits);

    this.fwarps = new File(p.getDataFolder(), "warps.yml");

    if (!this.fwarps.exists()) {
      try {
        this.fwarps.createNewFile();
      } catch (IOException e) {
        Bukkit.getServer().getLogger().severe("§4[ERROR] §cNao foi possivel criar a file warps.yml");
        e.printStackTrace();
      }
    }

    this.warp = YamlConfiguration.loadConfiguration(this.fwarps);
  }

  public FileConfiguration getKitsConfig()
  {
    return this.kit;
  }

  public FileConfiguration getConfig() {
    return this.config;
  }

  public FileConfiguration getWarpsConfig() {
    return this.warp;
  }

  public void saveWarpsConfig() {
    try {
      this.warp.save(this.fwarps);
    } catch (IOException e) {
      Bukkit.getServer().getLogger().severe("§4[ERROR] §cNao foi possivel salvar a file warps.yml");
      e.printStackTrace();
    }
  }

  public void saveKitsConfig() {
    try {
      this.kit.save(this.fkits);
    } catch (IOException e) {
      Bukkit.getServer().getLogger().severe("§4[ERROR] §cNao foi possivel salvar a file kits.yml");
      e.printStackTrace();
    }
  }

  public void saveConfig() {
    try {
      this.config.save(this.fconf);
    } catch (IOException e) {
      Bukkit.getServer().getLogger().severe("§4[ERROR] §cNao foi possivel salvar a file config.yml");
      e.printStackTrace();
    }
  }

  public void reloadWarpsConfig() {
    this.warp = YamlConfiguration.loadConfiguration(this.fwarps);
  }

  public void reloadKitsConfig() {
    this.kit = YamlConfiguration.loadConfiguration(this.fkits);
  }

  public void reloadConfig() {
    this.config = YamlConfiguration.loadConfiguration(this.fconf);
  }
}