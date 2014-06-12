package net.fightpvp.kits;

import java.util.ArrayList;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

public class Thor
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  ArrayList<Player> c = new ArrayList<Player>();

  public Thor(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("thor")) {
      if (p.hasPermission("kit.thor")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.addItem(new ItemStack[] { new ItemStack(Material.STONE_AXE) });
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
  public void playersai(PlayerQuitEvent e)
  {
    Player p = e.getPlayer();
    if (this.c.contains(p))
      this.c.remove(p);
  }

  @EventHandler
  public void morre(PlayerDeathEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = e.getEntity();
      if (this.c.contains(p))
        this.c.remove(p);
    }
  }

  @EventHandler
  public void Dano(EntityDamageByEntityEvent e) {
    if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof LightningStrike))) {
      Player p = (Player)e.getEntity();
      LightningStrike b = (LightningStrike)e.getDamager();
      if ((b.hasMetadata("Thor")) && (this.kitmg.hasAbility(p, "thor")))
        e.setCancelled(true);
    }
  }

  @EventHandler
  public void Ignite(BlockIgniteEvent e) {
    if ((e.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) && 
      ((e.getIgnitingEntity() instanceof LightningStrike))) {
      LightningStrike ls = (LightningStrike)e.getIgnitingEntity();
      if (ls.hasMetadata("Thor"))
        e.setCancelled(true);
    }
  }

  @EventHandler
  public void Interat(PlayerInteractEvent e)
  {
    final Player p = e.getPlayer();
    if ((this.kitmg.hasAbility(p, "thor")) && 
      (p.getItemInHand().getType() == Material.STONE_AXE) && 
      (e.getAction() == Action.RIGHT_CLICK_BLOCK))
      if (this.c.contains(p)) {
        p.sendMessage(ChatColor.RED + "Espere terminar o cooldown !");
      } else {
        Location bLoc = e.getClickedBlock().getWorld().getHighestBlockAt(e.getClickedBlock().getLocation()).getLocation();
        LightningStrike ls = p.getWorld().strikeLightning(bLoc);
        ls.setMetadata("Thor", new FixedMetadataValue(this.plugin, Boolean.valueOf(true)));
        this.c.add(p);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
          public void run() {
            Thor.this.c.remove(p);
          }
        }
        , 200L);
      }
  }
}