package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class Endermage
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  String teleport = ChatColor.translateAlternateColorCodes('&', "&cPuxado!\nVoce esta invencivel por 5 segundos ! Prepare-se para a luta !");

  public Endermage(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("endermage")) {
      if (p.hasPermission("kit.endermage")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.addItem(new ItemStack[] { new ItemStack(Material.PORTAL) });
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

  public void TeleportP(Location portal, Player p1, Player p2)
  {
    p1.teleport(portal.clone().add(0.0D, 1.0D, 0.0D));
    p2.teleport(portal.clone().add(0.0D, 1.0D, 0.0D));
    p1.setNoDamageTicks(100);
    p2.setNoDamageTicks(100);
    p1.sendMessage(this.teleport);
    p2.sendMessage(this.teleport);
    p2.getWorld().playEffect(p2.getLocation(), Effect.ENDER_SIGNAL, 9);
    p1.getWorld().playEffect(portal, Effect.ENDER_SIGNAL, 9);
    p2.playSound(p2.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.2F);
    p1.playSound(portal, Sound.ENDERMAN_TELEPORT, 1.0F, 1.2F);
  }
  private boolean isEnderable(Location portal, Location player) {
    return (Math.abs(portal.getX() - player.getX()) < 3.0D) && (Math.abs(portal.getZ() - player.getZ()) < 3.0D) && (
      Math.abs(portal.getY() - player.getY()) >= 3.5D);
  }

  @SuppressWarnings("deprecation")
@EventHandler
  public void EndermageInteract(PlayerInteractEvent e) {
   final Player mage = e.getPlayer();
    if ((this.kitmg.hasAbility(mage, "endermage")) && 
      (e.getAction() == Action.RIGHT_CLICK_BLOCK) && 
      (mage.getItemInHand().getType() == Material.PORTAL)) {
      e.setCancelled(true);
      mage.updateInventory();
      mage.setItemInHand(new ItemStack(Material.AIR));
      mage.updateInventory();
     final Block b = e.getClickedBlock();

      final Location bLoc = b.getLocation();
      final BlockState bs = b.getState();

      b.setType(Material.ENDER_PORTAL_FRAME);
      for (Player nearby : Bukkit.getOnlinePlayers()) {
       final Player target = nearby.getPlayer();
        new BukkitRunnable() {
          int time = 5;

          public void run() { this.time -= 1;
            if ((!Endermage.this.kitmg.hasAbility(target, "endermage")) && 
              (Endermage.this.isEnderable(bLoc, target.getLocation())) && 
              (target != mage) && 
              (!target.isDead())) {
              b.setType(bs.getType());
              b.setData(bs.getBlock().getData());
              cancel();
              Endermage.this.TeleportP(bLoc, mage, target);
              if (!mage.getInventory().contains(new ItemStack(Material.PORTAL))) {
                mage.getInventory().addItem(new ItemStack[] { new ItemStack(Material.PORTAL) });
                mage.updateInventory();
              }
            }
            else if (this.time == 0) {
              cancel();
              b.setType(bs.getType());
              b.setData(bs.getBlock().getData());
              if (!mage.getInventory().contains(new ItemStack(Material.PORTAL))) {
                mage.getInventory().addItem(new ItemStack[] { new ItemStack(Material.PORTAL) });
                mage.updateInventory();
              }
            }
          }
        }
        .runTaskTimer(this.plugin, 0L, 20L);
      }
    }
  }
}