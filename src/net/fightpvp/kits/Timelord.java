package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;

public class Timelord
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  ArrayList<Player> cooldown = new ArrayList();
  List<Player> congelado = new ArrayList();
  long COOLDOWN_TIME = 600L;
  Integer task = null;

  public Timelord(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("timelord")) {
      if (p.hasPermission("kit.timelord")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
          pi.addItem(new ItemStack[] { InvManager.getInvManager().toFill(Material.WATCH, "§6Timelord") });

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
  public void Click(PlayerInteractEvent e)
  {
    final Player p = e.getPlayer();
    ItemStack lord = InvManager.getInvManager().toFill(Material.WATCH, "§6Timelord");

    if ((this.kitmg.hasAbility(p, "timelord")) && 
      (p.getItemInHand().equals(lord)) && (
      (e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)))
      if (this.cooldown.contains(p)) {
        p.sendMessage(ChatColor.RED + "Aguarde o cooldown ");
      }
      else
        for (Entity ent : p.getNearbyEntities(6.0D, 6.0D, 6.0D))
          if ((ent instanceof Player)) {
            final Player t = (Player)ent;

            if (!this.congelado.contains(t)) {
              this.congelado.add(p);
            }

            p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10.0F, 10.0F);
            this.cooldown.add(p);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
              public void run() {
                Timelord.this.congelado.remove(t);
              }
            }
            , 200L);

            this.task = Integer.valueOf(Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
              public void run() {
                Timelord.this.cooldown.remove(p);
              }
            }
            , this.COOLDOWN_TIME));
          }
  }

  public void cancelTask()
  {
    if (this.task != null) {
      Bukkit.getServer().getScheduler().cancelTask(this.task.intValue());
      this.task = null;
    }
  }

  @EventHandler
  public void Morre(PlayerDeathEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = e.getEntity();
      if (this.cooldown.contains(p)) {
        this.cooldown.remove(p);
        cancelTask();
      }
    }
  }

  @EventHandler
  public void Sair(PlayerQuitEvent e) {
    Player p = e.getPlayer();
    if (this.cooldown.contains(p)) {
      this.cooldown.remove(p);
      cancelTask();
    }
  }

  @EventHandler
  public void Move(PlayerMoveEvent e) {
    Player p = e.getPlayer();
    if ((this.congelado.contains(p)) && 
      (e.getFrom().getBlockX() == e.getTo().getBlockX()) && (e.getFrom().getBlockY() == e.getTo().getBlockY()) && (e.getFrom().getBlockZ() == e.getTo().getBlockZ()))
      e.setCancelled(true);
  }
}