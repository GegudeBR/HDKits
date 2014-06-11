package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
import org.bukkit.util.Vector;

public class Hulk
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  public Hulk(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("hulk")) {
      if (p.hasPermission("kit.hulk")) {
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

          for (int i = 0; i < 37; i++) {
            pi.addItem(new ItemStack[] { new ItemStack(Material.MUSHROOM_SOUP) });
          }
          pi.setItem(1, null);
        }
      }
      else p.sendMessage(ChatColor.RED + "Voce nao possue este kit !");
    }

    return false;
  }
  @EventHandler
  public void hulk(PlayerInteractEntityEvent e) {
    Player p = e.getPlayer();
    if ((e.getRightClicked() instanceof Player)) {
      Player clicked = (Player)e.getRightClicked();
      if ((!p.isInsideVehicle()) && (!clicked.isInsideVehicle()) && 
        (p.getItemInHand().getType() == Material.AIR) && 
        (this.kitmg.hasAbility(p, "hulk")))
        p.setPassenger(clicked);
    }
  }

  @EventHandler
  public void noHulkMor(EntityDamageByEntityEvent e)
  {
    if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
      final Player p = (Player)e.getEntity();
      Player hulk = (Player)e.getDamager();
      if ((hulk.getPassenger() != null) && 
        (hulk.getPassenger() == p) && 
        (this.kitmg.hasAbility(hulk, "hulk")) && 
        (hulk.getPassenger() == p) && 
        (hulk.getItemInHand().getType() == Material.AIR)) {
        e.setCancelled(true);
        p.setSneaking(true);
        Vector vec = p.getLocation().getDirection().multiply(1.5F);
        vec.setY(0.6D);
        p.setVelocity(vec);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
          public void run() {
            p.setSneaking(false);
          }
        }
        , 10L);
      }
    }
  }
}