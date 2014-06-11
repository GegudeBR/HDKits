package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class Grappler
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  Map<Player, CopyOfFishingHook> hooks = new HashMap();

  public Grappler(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("grappler")) {
      if (p.hasPermission("kit.grappler")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.addItem(new ItemStack[] { new ItemStack(Material.LEASH) });
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
  public void onSlot(PlayerItemHeldEvent e)
  {
    if ((this.kitmg.hasAbility(e.getPlayer(), "grappler")) && 
      (this.hooks.containsKey(e.getPlayer()))) {
      ((CopyOfFishingHook)this.hooks.get(e.getPlayer())).remove();
      this.hooks.remove(e.getPlayer());
    }
  }

  @EventHandler
  public void onMove(PlayerMoveEvent e) {
    if ((this.kitmg.hasAbility(e.getPlayer(), "grappler")) && 
      (this.hooks.containsKey(e.getPlayer())) && 
      (!e.getPlayer().getItemInHand().getType().equals(Material.LEASH))) {
      ((CopyOfFishingHook)this.hooks.get(e.getPlayer())).remove();
      this.hooks.remove(e.getPlayer());
    }
  }

  @EventHandler
  public void onLeash(PlayerLeashEntityEvent e)
  {
    Player p = e.getPlayer();
    if ((this.kitmg.hasAbility(p, "grappler")) && 
      (e.getPlayer().getItemInHand().getType().equals(Material.LEASH))) {
      e.setCancelled(true);
      e.getPlayer().updateInventory();
      e.setCancelled(true);
      if (!this.hooks.containsKey(p)) {
        return;
      }

      if (!((CopyOfFishingHook)this.hooks.get(p)).isHooked()) {
        return;
      }

      double d = ((CopyOfFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().distance(p.getLocation());
      double t = d;
      double v_x = (1.0D + 0.07000000000000001D * t) * (((CopyOfFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getX() - p.getLocation().getX()) / t;
      double v_y = (1.0D + 0.03D * t) * (((CopyOfFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getY() - p.getLocation().getY()) / t;
      double v_z = (1.0D + 0.07000000000000001D * t) * (((CopyOfFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getZ() - p.getLocation().getZ()) / t;

      Vector v = p.getVelocity();
      v.setX(v_x);
      v.setY(v_y);
      v.setZ(v_z);
      p.setVelocity(v);
    }
  }

  @EventHandler
  public void onDamageByFall(EntityDamageEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player player = (Player)e.getEntity();
      if ((this.kitmg.hasAbility(player, "grappler")) && 
        (e.getCause() == EntityDamageEvent.DamageCause.FALL) && 
        (this.hooks.containsKey(player)) && 
        (((CopyOfFishingHook)this.hooks.get(player)).isHooked()))
        e.setDamage(e.getDamage() / 10.0D);
    }
  }

  @EventHandler
  public void onClick(PlayerInteractEvent e)
  {
    Player p = e.getPlayer();
    if ((this.kitmg.hasAbility(p, "grappler")) && 
      (e.getPlayer().getItemInHand().getType().equals(Material.LEASH))) {
      e.setCancelled(true);
      if ((e.getAction() == Action.LEFT_CLICK_AIR) || 
        (e.getAction() == Action.LEFT_CLICK_BLOCK)) {
        if (this.hooks.containsKey(p)) {
          ((CopyOfFishingHook)this.hooks.get(p)).remove();
        }

        CopyOfFishingHook nmsHook = new CopyOfFishingHook(p.getWorld(), ((CraftPlayer)p).getHandle());
        nmsHook.spawn(p.getEyeLocation().add(p.getLocation().getDirection().getX(), p.getLocation().getDirection().getY(), p.getLocation().getDirection().getZ()));
        nmsHook.move(p.getLocation().getDirection().getX() * 5.0D, p.getLocation().getDirection().getY() * 5.0D, p.getLocation().getDirection().getZ() * 5.0D);
        this.hooks.put(p, nmsHook);
      }
      else
      {
        if (!this.hooks.containsKey(p)) {
          return;
        }

        if (!((CopyOfFishingHook)this.hooks.get(p)).isHooked()) {
          return;
        }
        double d = ((CopyOfFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().distance(p.getLocation());
        double t = d;
        double v_x = (1.0D + 0.07000000000000001D * t) * (((CopyOfFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getX() - p.getLocation().getX()) / t;
        double v_y = (1.0D + 0.03D * t) * (((CopyOfFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getY() - p.getLocation().getY()) / t;
        double v_z = (1.0D + 0.07000000000000001D * t) * (((CopyOfFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getZ() - p.getLocation().getZ()) / t;

        Vector v = p.getVelocity();
        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);
        p.setVelocity(v);
      }
    }
  }
}