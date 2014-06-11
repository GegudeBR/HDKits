package net.fightpvp.comandos;

import java.util.ArrayList;
import java.util.List;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.KitManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class toAdmins
  implements CommandExecutor, Listener
{
  private Fight plugin;
  public List<Player> infoL = new ArrayList<Player>();
  public List<Player> inviL = new ArrayList<Player>();
  public List<Player> invL = new ArrayList<Player>();

  public List<Player> admin = new ArrayList<Player>();

  KitManager kitmg = KitManager.getKitManager();
  InvManager invmg = InvManager.getInvManager();

  public toAdmins(Fight plugin)
  {
    this.plugin = plugin;
  }

  @SuppressWarnings("deprecation")
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if ((label.equalsIgnoreCase("kick")) && 
      (sender.hasPermission("fight.kick"))) {
      if (args.length < 2) {
        sender.sendMessage(ChatColor.RED + "Use; /kick <player> <motivo>");
      } else {
        OfflinePlayer t = Bukkit.getPlayer(args[0]);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          sb.append(args[i]).append(" ");
        }
        String msg = sb.toString().trim();
        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + ChatColor.DARK_GRAY + " kickou " + ChatColor.RED + t.getName() + ChatColor.DARK_GRAY + " por " + ChatColor.GOLD + ChatColor.ITALIC + " >> " + ChatColor.DARK_GRAY + msg);
        ((Player)t).kickPlayer(msg);
      }
    }

    if (label.equalsIgnoreCase("setspawn")) {
      Player p = (Player)sender;
      if (p.hasPermission("fight.setspawn"))
      {
        int x = (int)p.getLocation().getX();
        int y = (int)p.getLocation().getY();
        int z = (int)p.getLocation().getZ();

        this.plugin.getConfig().set("spawn.world", p.getWorld().getName());
        this.plugin.getConfig().set("spawn.x", Integer.valueOf(x));
        this.plugin.getConfig().set("spawn.y", Integer.valueOf(y));
        this.plugin.getConfig().set("spawn.z", Integer.valueOf(z));
        this.plugin.saveConfig();
        p.sendMessage("§aSpawn marcado !");
      }
    }

    if ((label.equalsIgnoreCase("ban")) && 
      (sender.hasPermission("fight.ban"))) {
      if (args.length < 2) {
        sender.sendMessage(ChatColor.RED + "Use; /ban <player> <motivo>");
      } else {
        OfflinePlayer t = Bukkit.getPlayer(args[0]);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          sb.append(args[i]).append(" ");
        }
        String msg = sb.toString().trim();
        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + ChatColor.DARK_GRAY + " baniu " + ChatColor.RED + t.getName() + ChatColor.DARK_GRAY + " por " + ChatColor.GOLD + ChatColor.ITALIC + " >> " + ChatColor.DARK_GRAY + msg);
        ((Player)t).kickPlayer(ChatColor.RED + sender.getName() + ChatColor.DARK_GRAY + " baniu voce" + ChatColor.DARK_GRAY + " por " + ChatColor.GOLD + ChatColor.ITALIC + " >> " + ChatColor.DARK_GRAY + msg);
        t.setBanned(true);
      }

    }

    if ((label.equalsIgnoreCase("pardon")) && 
      (sender.hasPermission("fight.pardon"))) {
      if (args.length == 0) {
        sender.sendMessage(ChatColor.RED + "Use; /pardon <player>");
      } else {
        OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
        if (t.isBanned()) {
          t.setBanned(false);
          Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " desbaniu " + t.getName());
        } else {
          sender.sendMessage(ChatColor.RED + "Este player nao esta banido !");
        }
      }

    }

    if (label.equalsIgnoreCase("dia")) {
      Player p = (Player)sender;
      if (p.hasPermission("fight.dia")) {
        World w = p.getWorld();
        w.setTime(0L);
      }
    }
    if (label.equalsIgnoreCase("noite")) {
      Player p = (Player)sender;
      if (p.hasPermission("fight.noite")) {
        World w = p.getWorld();
        w.setTime(16000L);
      }
    }

    if (label.equalsIgnoreCase("admin")) {
      Player p = (Player)sender;
      if (p.hasPermission("fight.admin")) {
        Inventory inv = Bukkit.createInventory(p, 36, ChatColor.DARK_GRAY + "Admin");
        inv.clear();

        ItemStack v = this.invmg.toFill(Material.THIN_GLASS, " ");
        ItemStack g = this.invmg.newItem(Material.WOOL, 13, ChatColor.GREEN + "Ativar");
        ItemStack info = this.invmg.toFill(Material.PAPER, ChatColor.WHITE + "Informaçoes - " + ChatColor.RED + ChatColor.BOLD + "Desativado");
        ItemStack openinv = this.invmg.toFill(Material.CHEST, ChatColor.WHITE + "Abrir Inventario - " + ChatColor.RED + ChatColor.BOLD + "Desativado");
        ItemStack invi = this.invmg.toFill(Material.SUGAR, ChatColor.WHITE + "Invisibilidade - " + ChatColor.RED + ChatColor.BOLD + "Desativado");

        if (this.plugin.getSave().containsKey(p)) {
          inv.setContents((ItemStack[])this.plugin.getSave().get(p));
        } else {
          this.invmg.setLayer(inv, 1, v, v, v, v, v, v, v, v, v);
          this.invmg.setLayer(inv, 2, v, g, g, g, v, v, info, v, v);
          this.invmg.setLayer(inv, 3, v, g, g, g, v, v, openinv, invi, v);
          this.invmg.setLayer(inv, 4, v, v, v, v, v, v, v, v, v);
        }
        p.openInventory(inv);
      }
    }
    return false;
  }
  @EventHandler
  public void Interact(PlayerInteractEntityEvent e) {
    if ((e.getRightClicked() instanceof Player)) {
      Player c = (Player)e.getRightClicked();
      Player p = e.getPlayer();

      if ((this.admin.contains(p)) && (this.invL.contains(p))) {
        p.openInventory(c.getInventory());
      }
      if ((this.admin.contains(p)) && (this.infoL.contains(p))) {
        p.sendMessage(ChatColor.WHITE + "Vida : " + ChatColor.GOLD + (int)c.getHealth() + ChatColor.WHITE + "/" + ChatColor.GOLD + "20");
        p.sendMessage(ChatColor.WHITE + "Kit : " + ChatColor.GOLD + this.kitmg.getPlayerKit(c).getName());
      }
    }
  }

  @EventHandler
  public void Quit(PlayerQuitEvent e) {
    Player p = e.getPlayer();
    for (Player a : Bukkit.getServer().getOnlinePlayers()) {
      a.showPlayer(p);
      this.admin.remove(p);
      this.infoL.remove(p);
      this.inviL.remove(p);
      this.invL.remove(p);
    }
  }

  @SuppressWarnings("deprecation")
@EventHandler
  public void Click(InventoryClickEvent e) {
    Player p = (Player)e.getWhoClicked();
    ItemStack i = e.getCurrentItem();
    if (e.getInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "Admin")) {
      e.setCancelled(true);
      p.updateInventory();
      e.setCursor(new ItemStack(Material.AIR));
      p.updateInventory();

      ItemStack g = this.invmg.newItem(Material.WOOL, 13, ChatColor.GREEN + "Ativar");
      ItemStack info = this.invmg.toFill(Material.PAPER, ChatColor.WHITE + "Informaçoes - " + ChatColor.GREEN + ChatColor.BOLD + "Ativado");
      ItemStack openinv = this.invmg.toFill(Material.CHEST, ChatColor.WHITE + "Abrir Inventario - " + ChatColor.GREEN + ChatColor.BOLD + "Ativado");
      ItemStack invi = this.invmg.toFill(Material.SUGAR, ChatColor.WHITE + "Invisibilidade - " + ChatColor.GREEN + ChatColor.BOLD + "Ativado");

      ItemStack goff = this.invmg.newItem(Material.WOOL, 14, ChatColor.DARK_RED + "Desativar");
      ItemStack infooff = this.invmg.toFill(Material.PAPER, ChatColor.WHITE + "Informaçoes - " + ChatColor.RED + ChatColor.BOLD + "Desativado");
      ItemStack openinvoff = this.invmg.toFill(Material.CHEST, ChatColor.WHITE + "Abrir Inventario - " + ChatColor.RED + ChatColor.BOLD + "Desativado");
      ItemStack invioff = this.invmg.toFill(Material.SUGAR, ChatColor.WHITE + "Invisibilidade - " + ChatColor.RED + ChatColor.BOLD + "Desativado");

      if (i.equals(g)) {
        this.admin.add(p);
        p.setGameMode(GameMode.CREATIVE);
        e.getInventory().setItem(10, goff);
        e.getInventory().setItem(11, goff);
        e.getInventory().setItem(12, goff);
        e.getInventory().setItem(19, goff);
        e.getInventory().setItem(20, goff);
        e.getInventory().setItem(21, goff);
        if (this.inviL.contains(p)) {
          for (Player a : Bukkit.getServer().getOnlinePlayers()) {
            a.hidePlayer(p);
          }
        }
        p.closeInventory();
      }

      if (i.equals(goff)) {
        this.admin.remove(p);
        p.setGameMode(GameMode.SURVIVAL);
        e.getInventory().setItem(10, g);
        e.getInventory().setItem(11, g);
        e.getInventory().setItem(12, g);
        e.getInventory().setItem(19, g);
        e.getInventory().setItem(20, g);
        e.getInventory().setItem(21, g);
        for (Player a : Bukkit.getServer().getOnlinePlayers()) {
          a.showPlayer(p);
          a.canSee(p);
        }
        p.closeInventory();
      }

      if (i.equals(invi)) {
        this.inviL.remove(p);
        e.getInventory().setItem(25, invioff);
        for (Player a : Bukkit.getServer().getOnlinePlayers()) {
          a.showPlayer(p);
          a.canSee(p);
        }
      }

      if (i.equals(invioff)) {
        this.inviL.add(p);
        e.getInventory().setItem(25, invi);
      }

      if (i.equals(info)) {
        this.infoL.remove(p);
        e.getInventory().setItem(15, infooff);
      }

      if (i.equals(infooff)) {
        this.infoL.add(p);
        e.getInventory().setItem(15, info);
      }

      if (i.equals(openinv)) {
        this.invL.remove(p);
        e.getInventory().setItem(24, openinvoff);
      }

      if (i.equals(openinvoff)) {
        this.invL.add(p);
        e.getInventory().setItem(24, openinv);
      }

      if (i.equals(invi)) {
        this.inviL.remove(p);
      }

      if (i.equals(invioff))
        this.inviL.add(p);
    }
  }

  @EventHandler
  public void CloseInventoraySave(InventoryCloseEvent e) {
    if ((e.getInventory().getTitle().contains("§8Admin")) && 
      ((e.getPlayer() instanceof Player))) {
      Player p = (Player)e.getPlayer();

      this.plugin.getSave().remove(p);
      this.plugin.getSave().put(p, e.getInventory().getContents());
    }
  }
}