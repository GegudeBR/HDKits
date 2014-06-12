package net.fightpvp.comandos;

import net.fightpvp.configs.ConfigManager;
import net.fightpvp.listeners.PlayerListener;
import net.fightpvp.main.Fight;
import net.fightpvp.managers.WarpManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Warp
  implements CommandExecutor
{
  private Fight plugin;
  WarpManager wm = WarpManager.getWarpManager();
  ConfigManager cm = ConfigManager.getConfigs();

  public Warp(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;
    if (label.equalsIgnoreCase("spawn")) {
      if (!PlayerListener.combat.containsKey(p)) {
        p.teleport(
          new Location(Bukkit.getWorld(this.plugin.getConfig().getString("spawn.world")), 
          this.plugin.getConfig().getDouble("spawn.x"), 
          this.plugin.getConfig().getDouble("spawn.y"), 
          this.plugin.getConfig().getDouble("spawn.z")));
        p.sendMessage(ChatColor.DARK_GRAY + "Teleportado para o spawn");
      } else {
        p.sendMessage(ChatColor.RED + "Voce nao pode teleportar em combate");
      }
    }

    if ((label.equalsIgnoreCase("setwarp")) && 
      (p.hasPermission("warp.setwarp"))) {
      if (args.length == 0)
        p.sendMessage(ChatColor.RED + "Use; /setwarp <warp>");
      else {
        this.wm.setWarp(p, p.getLocation(), args[0]);
      }
    }

    if ((label.equalsIgnoreCase("delwarp")) && 
      (p.hasPermission("warp.delwarp"))) {
      if (args.length == 0)
        p.sendMessage(ChatColor.RED + "Use; /delwarp <warp>");
      else {
        this.wm.removeWarp(p, args[0]);
      }
    }

    if (label.equalsIgnoreCase("warp")) {
      if (args.length == 0) {
        this.wm.sendWarpList(p);
        this.wm.newWarps(p);
      } else {
        String warp = args[0];

        if (this.cm.getWarpsConfig().getConfigurationSection("warps." + warp.toLowerCase()) == null) {
          p.sendMessage(ChatColor.GRAY + "Esta warp nao existe !");
        } else {
          ConfigurationSection cs = this.cm.getWarpsConfig().getConfigurationSection("warps." + warp.toLowerCase());
          if (!PlayerListener.combat.containsKey(p)) {
            p.teleport(
              new Location(Bukkit.getWorld(cs.getString(".world")), 
              cs.getDouble(".x"), 
              cs.getDouble(".y"), 
              cs.getDouble(".z")));
            p.sendMessage(ChatColor.DARK_GRAY + "Teleportado para a warp " + args[0].toLowerCase());
          } else {
            p.sendMessage(ChatColor.RED + "Voce nao pode teleportar em combate");
          }
        }
      }
    }
    if ((label.equalsIgnoreCase("setitem")) && 
      (p.hasPermission("warp.setitem"))) {
      if (args.length == 0)
        p.sendMessage(ChatColor.RED + "Use; /setitem <warp>");
      else {
        this.wm.setItem(p, args[0].toLowerCase());
      }
    }

    return false;
  }
}