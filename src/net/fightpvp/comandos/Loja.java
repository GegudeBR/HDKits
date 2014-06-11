package net.fightpvp.comandos;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.KitManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Loja
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();
  InvManager invmg = InvManager.getInvManager();

  public Loja(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;
    if (label.equalsIgnoreCase("loja")) {
      this.plugin.items = Bukkit.createInventory(p, 27, ChatColor.DARK_GRAY + "x- Shop - Items -x");
      this.plugin.shop = Bukkit.createInventory(p, 27, ChatColor.DARK_GRAY + "x- Shop -x");
      this.plugin.kits = Bukkit.createInventory(p, 27, ChatColor.DARK_GRAY + "x- Shop - Kits -x");
      this.plugin.items.clear();
      this.plugin.shop.clear();
      this.plugin.kits.clear();

      ItemStack g = InvManager.getInvManager().toFill(Material.IRON_FENCE, " ");
      ItemStack v = InvManager.getInvManager().toFill(Material.THIN_GLASS, " ");
      ItemStack a = new ItemStack(Material.AIR);
      ItemStack k = InvManager.getInvManager().toFill(Material.CHEST, "§7§o-> §aKits §7§o<-");
      ItemStack i = InvManager.getInvManager().toFill(Material.LEATHER_CHESTPLATE, "§7§o-> §aItems §7§o<-");
      ItemStack kP = InvManager.getInvManager().toFill(Material.IRON_FENCE, ChatColor.GOLD + "Gladiator");
      ItemStack kH = InvManager.getInvManager().toFill(Material.WOOD_AXE, ChatColor.GOLD + "Thor");
      ItemStack kA = InvManager.getInvManager().toFill(Material.LEATHER_BOOTS, ChatColor.GOLD + "Stomper");
      ItemStack kS = InvManager.getInvManager().toFill(Material.BLAZE_ROD, ChatColor.GOLD + "Monk");
      ItemStack kT = InvManager.getInvManager().toFill(Material.IRON_BLOCK, ChatColor.GOLD + "Anchor");

      this.invmg.setLayer(this.plugin.shop, 1, g, g, v, v, v, v, v, g, g);
      this.invmg.setLayer(this.plugin.shop, 2, g, v, v, a, g, a, v, v, g);
      this.invmg.setLayer(this.plugin.shop, 3, g, g, v, v, v, v, v, g, g);
      this.plugin.shop.setItem(this.plugin.shop.firstEmpty(), k);
      this.plugin.shop.setItem(this.plugin.shop.firstEmpty(), i);

      this.invmg.setLayer(this.plugin.kits, 1, g, g, v, v, v, v, v, g, g);
      this.invmg.setLayer(this.plugin.kits, 2, g, v, kP, kH, kA, kS, kT, v, g);
      this.invmg.setLayer(this.plugin.kits, 3, g, g, v, v, v, v, v, g, g);

      this.invmg.Fill(this.plugin.items, this.invmg.toFill(Material.IRON_FENCE, " "));

      p.openInventory(this.plugin.shop);
    }
    return false;
  }

  @EventHandler
  public void Click(InventoryClickEvent e) {
    Player p = (Player)e.getWhoClicked();
    ItemStack i = e.getCurrentItem();

    if (e.getInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "x- Shop -x")) {
      e.setCancelled(true);
      p.updateInventory();

      ItemStack k = InvManager.getInvManager().toFill(Material.CHEST, "§7§o-> §aKits §7§o<-");
      ItemStack b = InvManager.getInvManager().toFill(Material.LEATHER_CHESTPLATE, "§7§o-> §aItems §7§o<-");

      if (i.equals(k)) {
        p.closeInventory();
        p.openInventory(this.plugin.kits);
      }

      if (i.equals(b)) {
        p.openInventory(this.plugin.items);
      }
    }

    if (e.getInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "x- Shop - Items -x")) {
      e.setCancelled(true);
      p.updateInventory();
    }

    if (e.getInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "x- Shop - Kits -x")) {
      e.setCancelled(true);
      p.updateInventory();

      ItemStack kP = InvManager.getInvManager().toFill(Material.IRON_FENCE, ChatColor.GOLD + "Gladiator");
      ItemStack kH = InvManager.getInvManager().toFill(Material.WOOD_AXE, ChatColor.GOLD + "Thor");
      ItemStack kA = InvManager.getInvManager().toFill(Material.LEATHER_BOOTS, ChatColor.GOLD + "Stomper");
      ItemStack kS = InvManager.getInvManager().toFill(Material.BLAZE_ROD, ChatColor.GOLD + "Monk");
      ItemStack kT = InvManager.getInvManager().toFill(Material.IRON_BLOCK, ChatColor.GOLD + "Anchor");

      EconomyResponse poseidon = this.plugin.econ.withdrawPlayer(p.getName(), 50000.0D);
      EconomyResponse hulk = this.plugin.econ.withdrawPlayer(p.getName(), 7000.0D);
      EconomyResponse anchor = this.plugin.econ.withdrawPlayer(p.getName(), 35000.0D);
      EconomyResponse specialist = this.plugin.econ.withdrawPlayer(p.getName(), 15000.0D);
      EconomyResponse turtle = this.plugin.econ.withdrawPlayer(p.getName(), 12000.0D);

      if (i.equals(kP)) {
        if (!p.hasPermission("kit.gladiator")) {
          if (poseidon.transactionSuccess()) {
            this.plugin.perms.playerAdd(p, "kit.gladiator");
            p.sendMessage(ChatColor.GOLD + "Voce comprou o kit : " + ChatColor.WHITE + "Gladiator");
            p.closeInventory();
          } else {
            p.sendMessage(ChatColor.RED + "Dinheiro insuficiente !");
          }
        }
        else p.sendMessage(ChatColor.RED + "Voce ja possui este kit !");

      }

      if (i.equals(kH)) {
        if (!p.hasPermission("kit.thor")) {
          if (hulk.transactionSuccess()) {
            this.plugin.perms.playerAdd(p, "kit.thor");
            p.sendMessage(ChatColor.GOLD + "Voce comprou o kit : " + ChatColor.WHITE + "Thor");
            p.closeInventory();
          } else {
            p.sendMessage(ChatColor.RED + "Dinheiro insuficiente !");
          }
        }
        else p.sendMessage(ChatColor.RED + "Voce ja possui este kit !");

      }

      if (i.equals(kA)) {
        if (!p.hasPermission("kit.Stomper")) {
          if (anchor.transactionSuccess()) {
            this.plugin.perms.playerAdd(p, "kit.Stomper");
            p.sendMessage(ChatColor.GOLD + "Voce comprou o kit : " + ChatColor.WHITE + "Stomper");
            p.closeInventory();
          } else {
            p.sendMessage(ChatColor.RED + "Dinheiro insuficiente !");
          }
        }
        else p.sendMessage(ChatColor.RED + "Voce ja possui este kit !");

      }

      if (i.equals(kS)) {
        if (!p.hasPermission("kit.Monk")) {
          if (specialist.transactionSuccess()) {
            this.plugin.perms.playerAdd(p, "kit.Monk");
            p.sendMessage(ChatColor.GOLD + "Voce comprou o kit : " + ChatColor.WHITE + "Monk");
            p.closeInventory();
          } else {
            p.sendMessage(ChatColor.RED + "Dinheiro insuficiente !");
          }
        }
        else p.sendMessage(ChatColor.RED + "Voce ja possui este kit !");

      }

      if (i.equals(kT))
        if (!p.hasPermission("kit.Anchor")) {
          if (turtle.transactionSuccess()) {
            this.plugin.perms.playerAdd(p, "kit.Anchor");
            p.sendMessage(ChatColor.GOLD + "Voce comprou o kit : " + ChatColor.WHITE + "Anchor");
            p.closeInventory();
          } else {
            p.sendMessage(ChatColor.RED + "Dinheiro insuficiente !");
          }
        }
        else p.sendMessage(ChatColor.RED + "Voce ja possui este kit !");
    }
  }
}