package net.fightpvp.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Gladiator
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  ArrayList<String> inPvP = new ArrayList<String>();
  public Map<String, Location> local = new HashMap<String, Location>();
  public Gladiator(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("gladiator")) {
      if (p.hasPermission("kit.gladiator")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.addItem(new ItemStack[] { InvManager.getInvManager().toFill(Material.IRON_FENCE, "§cGladiator") });
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

  public static ItemStack newItem(String name, Material material)
  {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    item.setItemMeta(meta);
    return item;
  }

  @SuppressWarnings("deprecation")
@EventHandler
  public void GladiatorPlaceCancel(BlockPlaceEvent e)
  {
    Player p = e.getPlayer();
    ItemStack item = newItem(ChatColor.RED + "Gladiator", Material.IRON_FENCE);
    if (p.getItemInHand().equals(item)) {
      e.setCancelled(true);
      p.updateInventory();
    }
  }
  
  @SuppressWarnings("deprecation")
 @EventHandler
  public void place(BlockPlaceEvent e) {
    Player p = e.getPlayer();

    ItemStack glad = new ItemStack(Material.IRON_FENCE);
    ItemMeta m = glad.getItemMeta();
    m.setDisplayName("§6Use para ir 1v1!");
    glad.setItemMeta(m);

    if (this.kitmg.hasAbility(p, "gladiator"))
    {
      e.setCancelled(true);
      p.updateInventory();
    }
  }
  
  @EventHandler
  public void death(PlayerDeathEvent e) {
    if (e.getEntity() instanceof Player) {
      Player p = e.getEntity();
      if(p.getKiller() instanceof Player) {
        Player killer = p.getKiller();
        if (inPvP.contains(p.getName())) {
          inPvP.remove(p.getName());
        }
          if (inPvP.contains(killer.getName())) {
            inPvP.remove(killer.getName());
          }
      }
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void block(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    if ((e.getAction() == Action.LEFT_CLICK_BLOCK) && (inPvP.contains(p.getName())) && (e.getClickedBlock().getType() == Material.GLASS)) {
      final Block b = e.getClickedBlock();
      b.setType(Material.BEDROCK);
  	  plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
 		public void run() {
 		   if(b.getType() == Material.AIR) {
 			 b.setType(Material.AIR);
 		   } else {
 		   b.setType(Material.GLASS);
 		  }
 		}
 	}, 20L);
    }
  }
  @EventHandler
  public void quebrar(BlockBreakEvent e) {
 	 if(e.getBlock().getType() == Material.GLASS && inPvP.contains(e.getPlayer().getName())) {
 		 e.setCancelled(true);
 	 }
  }
  
  @EventHandler
  public void removeOnTp(PlayerTeleportEvent e) {
    Player player = e.getPlayer();
    if (inPvP.contains(player.getName())) {
      inPvP.remove(player.getName());
    }
  }

  @EventHandler
  public void PlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
    final Player pessoa1 = event.getPlayer();

    ItemStack glad = new ItemStack(Material.IRON_FENCE);
    ItemMeta fim = glad.getItemMeta();
    fim.setDisplayName(ChatColor.GOLD + "Use para ir 1v1!");
    glad.setItemMeta(fim);

    if (this.kitmg.hasAbility(pessoa1, "gladiator"))
    {
      final Player pessoa2 = (Player)event.getRightClicked();
      Location pLoc = pessoa1.getLocation();

      if ((!inPvP.contains(pessoa1.getName())) && (!inPvP.contains(pessoa2.getName()))) {
        local.put(pessoa1.getName(), pLoc);
        local.put(pessoa2.getName(), pessoa2.getLocation());

        Location GladLoc = pessoa1.getLocation();
        final Location GladFence = new Location(pessoa1.getWorld(), GladLoc.getBlockX(), GladLoc.getWorld().getHighestBlockYAt(GladLoc) + 80, GladLoc.getBlockZ());

        generateArena(GladFence, pessoa2, pessoa1);
        pessoa1.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120 , 3));
        pessoa2.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120 , 3));
        new BukkitRunnable() {
          int tempo = 120;

          public void run() { 
         	 this.tempo -= 1;

            if (!inPvP.contains(pessoa1.getName())) {
              inPvP.add(pessoa1.getName());
            }
            if (!inPvP.contains(pessoa2.getName())) {
              inPvP.add(pessoa2.getName());
            }
            if ((pessoa1.isDead()) || (pessoa2.isDead()) || (!pessoa1.isOnline()) || (!pessoa2.isOnline()) || 
              (!inPvP.contains(pessoa1.getName())) || (!inPvP.contains(pessoa2.getName()))) {
              inPvP.remove(pessoa1.getName());
              inPvP.remove(pessoa2.getName());

              clearArena(GladFence);

              cancel();

              if (pessoa1.isOnline()) {
                pessoa1.teleport((Location)local.get(pessoa1.getName()));
                local.remove(pessoa1.getName());
                if (pessoa1.hasPotionEffect(PotionEffectType.WITHER)) {
                  pessoa1.removePotionEffect(PotionEffectType.WITHER);
                }
              }

              if (pessoa2.isOnline()) {
                pessoa2.teleport((Location)local.get(pessoa2.getName()));
                local.remove(pessoa2.getName());
                if (pessoa2.hasPotionEffect(PotionEffectType.WITHER))
                  pessoa2.removePotionEffect(PotionEffectType.WITHER);
              }
            }
            else
            {
              if (this.tempo == 60)
              {
                if ((!pessoa1.isDead()) && (pessoa1.isOnline()) && (inPvP.contains(pessoa1.getName()))) {
                  pessoa1.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 999999, 4));
                }
                if ((!pessoa2.isDead()) && (pessoa2.isOnline()) && (inPvP.contains(pessoa2.getName()))) {
                  pessoa2.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 999999, 4));
                }
              }
              if (this.tempo == 0) {
                inPvP.remove(pessoa1.getName());
                inPvP.remove(pessoa2.getName());

                clearArena(GladFence);

                cancel();

                if ((!pessoa1.isDead()) && (pessoa1.isOnline())) {
                  pessoa1.teleport((Location)local.get(pessoa1.getName()));

                  if (pessoa1.hasPotionEffect(PotionEffectType.WITHER)) {
                    pessoa1.removePotionEffect(PotionEffectType.WITHER);
                  }
                  local.remove(pessoa1);
                }

                if ((!pessoa2.isDead()) && (pessoa2.isOnline())) {
                  pessoa2.teleport((Location)local.get(pessoa2.getName()));

                  if (pessoa2.hasPotionEffect(PotionEffectType.WITHER)) {
                    pessoa2.removePotionEffect(PotionEffectType.WITHER);
                  }
                  local.remove(pessoa2);
                }
              }
            }
          }
        }
        .runTaskTimer(plugin, 0L, 20L);
        } 
      } else {
      return;
    }
  }

  public void clearArena(Location loc) {
 	    int x = 0;
 	    int y = 0;
 	    int z = 0;
 	    for (x = -7; x < 7; x++){
 	      for (z = -7; z < 7; z++) {
 	        for (y = 0; y < 7; y++) {
 	          Block b = loc.clone().add(x, 0.0D, z).getBlock();
 	          Block b2 = loc.clone().add(x, 7.0D, z).getBlock();
 	          Block b3 = loc.clone().add(-7.0D, y, z).getBlock();
 	          Block b4 = loc.clone().add(x, y, -7.0D).getBlock();
 	          Block b5 = loc.clone().add(x, y, 7.0D).getBlock();
 	          Block b6 = loc.clone().add(7.0D, y, z).getBlock();

 	          b.setType(Material.AIR);
 	          b2.setType(Material.AIR);
 	          b3.setType(Material.AIR);
 	          b4.setType(Material.AIR);
 	          b5.setType(Material.AIR);
 	          b6.setType(Material.AIR);
 	        }
 	      }
 	    }
 	  }

  public void generateArena(Location loc, Player gladiator, Player target) {
    int x = 0;
    int y = 0;
    int z = 0;
    for (x = -7; x < 7; x++) {
 	      for (z = -7; z < 7; z++) {
 	        for (y = 0; y < 7; y++) {
 	          Block b = loc.clone().add(x, 0.0D, z).getBlock();
 	          Block b2 = loc.clone().add(x, 7.0D, z).getBlock();
 	          Block b3 = loc.clone().add(-7.0D, y, z).getBlock();
 	          Block b4 = loc.clone().add(x, y, -7.0D).getBlock();
 	          Block b5 = loc.clone().add(x, y, 7.0D).getBlock();
 	          Block b6 = loc.clone().add(7.0D, y, z).getBlock();

 	          b.setType(Material.GLASS);
 	          b2.setType(Material.GLASS);
 	          b3.setType(Material.GLASS);
 	          b4.setType(Material.GLASS);
 	          b5.setType(Material.GLASS);
 	          b6.setType(Material.GLASS);
 	        }
 	      }
 	    }

    gladiator.teleport(loc.clone().add(x - 1, y - 2, -4.0D));
    target.teleport(loc.clone().add(-4.0D, y - 4, z - 1));
  }
  
}