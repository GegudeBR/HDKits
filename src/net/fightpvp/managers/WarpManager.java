package net.fightpvp.managers;

import net.fightpvp.configs.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpManager
  implements Listener
{
  ConfigManager config = ConfigManager.getConfigs();
  InvManager im = InvManager.getInvManager();

  private static WarpManager instance = new WarpManager();

  public static WarpManager getWarpManager() {
    return instance;
  }

  @SuppressWarnings("unchecked")
public void newWarps(Player p)
  {
    Inventory inv = Bukkit.createInventory(p, 18, ChatColor.DARK_GRAY + "Warps");
    if (this.config.getWarpsConfig().getConfigurationSection("warps") != null) {
      for (String s : this.config.getWarpsConfig().getConfigurationSection("warps").getKeys(false))
      {
        String a = this.config.getWarpsConfig().getString("warps." + s + ".item");
        a.replace("'", "");
        String[] slit = a.split(":");
        int type = Integer.valueOf(slit[0]).intValue();
        int data = Integer.valueOf(slit[1]).intValue();

        @SuppressWarnings("deprecation")
		ItemStack i = new ItemStack(type, 0, (byte)data);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + this.config.getWarpsConfig().getString(new StringBuilder("warps.").append(s).append(".name").toString()));
        List l = new ArrayList();
        l.add(this.config.getWarpsConfig().getString("warps." + s + ".lore"));
        meta.setLore(l);
        i.setItemMeta(meta);

        inv.setItem(inv.firstEmpty(), i);
      }
      this.im.Fill(inv, this.im.toFill(Material.IRON_FENCE, " "));
      p.openInventory(inv);
    } else {
      p.sendMessage(ChatColor.RED + "Nao ha warps");
    }
  }

  public void sendWarpList(Player p) {
    if (this.config.getWarpsConfig().getConfigurationSection("warps") != null) {
      String w = this.config.getWarpsConfig().getConfigurationSection("warps").getKeys(false);
      w = ((String) w).replace("[", "").replace("]", "");

      p.sendMessage(ChatColor.WHITE + "Warps: " + ChatColor.GRAY + w);
    } else {
      p.sendMessage(ChatColor.WHITE + "Warps: " + ChatColor.GRAY + "Nenhuma");
    }
  }

  public void setItem(Player p, String warp)
  {
    @SuppressWarnings("deprecation")
	int type = p.getItemInHand().getTypeId();
    int id = p.getItemInHand().getDurability();
    if (p.getItemInHand().getType() != Material.AIR) {
      if (this.config.getWarpsConfig().getConfigurationSection("warps." + warp.toLowerCase()) != null) {
        this.config.getWarpsConfig().set("warps." + warp.toLowerCase() + ".item", type + ":" + id);
        p.sendMessage(ChatColor.GRAY + "O Item da warp " + warp.toLowerCase() + " agora é - " + type + ":" + id);
        this.config.saveWarpsConfig();
      } else {
        p.sendMessage(ChatColor.RED + "Esta warp nao existe");
      }
    }
    else p.sendMessage(ChatColor.GRAY + "Voce precisa estar com um item na mao!");
  }

  public void setWarp(Player p, Location loc, String name)
  {
    char[] c = name.toCharArray();
    c[0] = Character.toUpperCase(c[0]);
    name = new String(c);

    this.config.getWarpsConfig().set("warps." + name.toLowerCase() + ".world", loc.getWorld().getName());
    this.config.getWarpsConfig().set("warps." + name.toLowerCase() + ".x", Double.valueOf(loc.getX()));
    this.config.getWarpsConfig().set("warps." + name.toLowerCase() + ".y", Double.valueOf(loc.getY()));
    this.config.getWarpsConfig().set("warps." + name.toLowerCase() + ".z", Double.valueOf(loc.getZ()));
    this.config.getWarpsConfig().set("warps." + name.toLowerCase() + ".name", name);
    this.config.getWarpsConfig().set("warps." + name.toLowerCase() + ".lore", ChatColor.WHITE + "Adicione na warps.yml");
    this.config.getWarpsConfig().set("warps." + name.toLowerCase() + ".item", "276:0");
    this.config.saveWarpsConfig();
    p.sendMessage(ChatColor.GREEN + "Warp " + name + " marcada !");
  }

  public void removeWarp(Player p, String name) {
    char[] c = name.toCharArray();
    c[0] = Character.toUpperCase(c[0]);
    name = new String(c);

    if (this.config.getWarpsConfig().contains("warps." + name.toLowerCase())) {
      this.config.getWarpsConfig().set("warps." + name.toLowerCase(), null);
      this.config.saveWarpsConfig();
      p.sendMessage(ChatColor.GREEN + "Warp " + name + " removida !");
    } else {
      p.sendMessage(ChatColor.RED + "Esta warp nao existe");
    }
  }
}