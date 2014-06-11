package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;

public class Monk
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  public String cooldownmsg = ChatColor.RED + "Voce pode usar o Monk em alguns segundos ! /n Aguarde.";
  public String monkado = ChatColor.BLUE + "Voce foi monkado!";
  public boolean bol = true;
  private List<Player> cooldown = new ArrayList();

  public Monk(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("monk")) {
      if (p.hasPermission("kit.monk")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.addItem(new ItemStack[] { InvManager.getInvManager().toFill(Material.BLAZE_ROD, ChatColor.GOLD + "Monk Staff") });
          pi.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));

          for (int i = 0; i < 37; i++)
            pi.addItem(new ItemStack[] { new ItemStack(Material.MUSHROOM_SOUP) });
        }
      }
      else {
        p.sendMessage(ChatColor.RED + "Voce nao possue este kit !");
      }
    }
    return false;
  }

  @EventHandler
  public void onRightClick(PlayerInteractEntityEvent e)
  {
    if ((e.getRightClicked() instanceof Player)) {
      final Player monk = e.getPlayer();
      Player c = (Player)e.getRightClicked();
      ItemStack staff = InvManager.getInvManager().toFill(Material.BLAZE_ROD, ChatColor.GOLD + "Monk Staff");
      if ((monk.getItemInHand().equals(staff)) && 
        (this.kitmg.hasAbility(monk, "monk")))
      {
        if (this.cooldown.contains(monk.getPlayer())) {
          monk.sendMessage(ChatColor.RED + "Aguarde o cooldown !");
        }
        else {
          PlayerInventory inv = c.getInventory();
          int slot = new Random().nextInt(this.bol ? 36 : 9);
          ItemStack rd = inv.getItemInHand();
          if (rd == null)
            rd = new ItemStack(Material.AIR);
          ItemStack rr = inv.getItem(slot);
          if (rr == null)
            rr = new ItemStack(Material.AIR);
          inv.setItemInHand(rr);
          inv.setItem(slot, rd);
          this.cooldown.add(monk);
          c.sendMessage(this.monkado);

          Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
              if (Monk.this.cooldown.contains(monk))
                Monk.this.cooldown.remove(monk);
              monk.sendMessage(ChatColor.AQUA + "Voce ja pode usar so Monk denovo !");
            }
          }
          , 600L);
        }
      }
    }
  }

  @EventHandler
  public void onHit(EntityDamageByEntityEvent event) {
    if (((event.getDamager() instanceof Player)) && ((event.getEntity() instanceof Player))) {
      final Player monk = (Player)event.getDamager();
      Player v = (Player)event.getEntity();
      ItemStack staff = InvManager.getInvManager().toFill(Material.BLAZE_ROD, ChatColor.GOLD + "Monk Staff");
      if ((monk.getItemInHand().equals(staff)) && 
        (this.kitmg.hasAbility(monk, "monk")))
        if (this.cooldown.contains(monk)) {
          monk.sendMessage(ChatColor.RED + "Aguarde o cooldown !");
        } else {
          PlayerInventory inv = v.getInventory();
          int slot = new Random().nextInt(this.bol ? 36 : 9);
          ItemStack rd = inv.getItemInHand();
          if (rd == null)
            rd = new ItemStack(Material.AIR);
          ItemStack rr = inv.getItem(slot);
          if (rr == null)
            rr = new ItemStack(Material.AIR);
          inv.setItemInHand(rr);
          inv.setItem(slot, rd);
          this.cooldown.add(monk);
          v.sendMessage(this.monkado);

          Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
              if (Monk.this.cooldown.contains(monk))
                Monk.this.cooldown.remove(monk);
              monk.sendMessage(ChatColor.AQUA + "Voce ja pode usar so Monk denovo !");
            }
          }
          , 600L);
        }
    }
  }
}