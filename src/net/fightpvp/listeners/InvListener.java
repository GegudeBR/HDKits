package net.fightpvp.listeners;

import net.fightpvp.configs.ConfigManager;
import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InvListener
  implements Listener
{
  public Fight plugin;

  public InvListener(Fight plugin) {
    this.plugin = plugin;
  }
  @SuppressWarnings("deprecation")
@EventHandler
  public void InventoryClickE(InventoryClickEvent e) {
    Player p = (Player)e.getWhoClicked();
    ItemStack item = e.getCurrentItem();

    ItemStack passar = InvManager.getInvManager().newItem(Material.CARPET, 1, ChatColor.GOLD + "Passar Pagina");
    ItemStack voltar = InvManager.getInvManager().newItem(Material.CARPET, 1, ChatColor.GOLD + "Voltar Pagina");
    ItemStack n = InvManager.getInvManager().newItem(Material.CARPET, 8, " ");
    ItemStack v = InvManager.getInvManager().toFill(Material.THIN_GLASS, " ");

    if (e.getInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "Kits")) {
      e.setCancelled(true);
      if(item.equals(v) || item.getType() == Material.THIN_GLASS) {
    	  e.setCurrentItem(v);
    	  e.setCancelled(true);
    	  p.updateInventory();
      }
      if ((!item.equals(n)) || (item.getType() != Material.THIN_GLASS) || ((item.hasItemMeta()) && (!item.getItemMeta().getDisplayName().equalsIgnoreCase(" "))))
      {
        String kitname = item.getItemMeta().getDisplayName();
        kitname = ChatColor.stripColor(kitname);

        if (KitManager.getKitManager().hasKit(kitname)) {
          Kit k = KitManager.getKitManager().getKit(kitname);
          InvManager.getInvManager().KitPreChooseInv(p, k);
        }

        if (item.equals(passar)) {
          p.closeInventory();
          p.openInventory(InvManager.getInvManager().kit2);
        }

        if (item.equals(voltar)) {
          p.closeInventory();
          p.chat("/kits");
        }
      } else {
        e.setCancelled(true);
      }
    }
    if (e.getInventory().getTitle().contains(ChatColor.DARK_GRAY + "Kit ")) {
      e.setCancelled(true);
      ItemStack escolher = InvManager.getInvManager().toFill(Material.FIRE, "Jogar com este kit");
      ItemStack po = InvManager.getInvManager().toFill(Material.SUGAR, ChatColor.GREEN + "Voltar");
      if(item.getType() == Material.THIN_GLASS || item.getItemMeta().getDisplayName().equalsIgnoreCase(" ")) { e.setCancelled(true); return; }
      if ((item.getType() != Material.THIN_GLASS) || (!item.getItemMeta().getDisplayName().equalsIgnoreCase(" ")))
      {
        if (item.equals(escolher)) {
          String[] s = e.getInventory().getTitle().split(" ");
          String name = s[1];

          if (p.hasPermission("kit." + name)) {
            p.chat("/" + name);
            p.closeInventory();
          } else {
            p.sendMessage(ChatColor.RED + "Voce nao possue este kit !");
          }
        }

        if (item.equals(po)) {
          p.closeInventory();
          p.chat("/kits");
        }
      }
    }
    if (e.getInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "Warps")) {
      e.setCancelled(true);
      if ((!item.getItemMeta().getDisplayName().equalsIgnoreCase(" ")) && (item.getType() != Material.IRON_FENCE)) {
        String name = item.getItemMeta().getDisplayName();
        name = ChatColor.stripColor(name);
        name = name.toLowerCase();

        if (ConfigManager.getConfigs().getWarpsConfig().getConfigurationSection("warps." + name.toLowerCase()) != null)
          p.chat("/warp " + name);
      }
    }
  }
}