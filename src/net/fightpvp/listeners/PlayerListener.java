package net.fightpvp.listeners;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import net.fightpvp.managers.WarpManager;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerListener
  implements Listener
{
  private Fight plugin;
  private String combatCom = ChatColor.AQUA + "Voce esta em combate com " + ChatColor.YELLOW + "%s" + ChatColor.AQUA + "\nNao saia em combate !";
  public static HashMap<Player, Player> combat = new HashMap();

  public PlayerListener(Fight plugin)
  {
    this.plugin = plugin;
  }

  @EventHandler
  public void ChatTranslate(AsyncPlayerChatEvent e)
  {
    Player p = e.getPlayer();
    if (p.hasPermission("fight.colors")) {
      String msg = e.getMessage();
      msg = ChatColor.translateAlternateColorCodes('&', msg);
      e.setMessage(msg);
    }
  }

  @EventHandler
  public void InteractItems(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    ItemStack kits = InvManager.getInvManager().toFill(Material.CHEST, ChatColor.GRAY + ""+ ChatColor.ITALIC + "<<x " + ChatColor.GREEN + ChatColor.BOLD + "Kits" + ChatColor.GRAY + ChatColor.ITALIC + " x>>");
    ItemStack warps = InvManager.getInvManager().toFill(Material.BOOK, ChatColor.GRAY + ""+ ChatColor.ITALIC + "<<x " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Warps" + ChatColor.GRAY + ChatColor.ITALIC + " x>>");
    ItemStack loja = InvManager.getInvManager().toFill(Material.GOLD_INGOT, ChatColor.GRAY + ""+ ChatColor.ITALIC + "<<x " + ChatColor.GOLD + ChatColor.BOLD + "Loja" + ChatColor.GRAY + ChatColor.ITALIC + " x>>");
    if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
      if (p.getItemInHand().equals(kits)) {
        e.setCancelled(true);
        p.updateInventory();
        p.chat("/kits");
      }
      if (p.getItemInHand().equals(warps)) {
        e.setCancelled(true);
        p.updateInventory();
        WarpManager.getWarpManager().newWarps(p);
      }
      if (p.getItemInHand().equals(loja)) {
        e.setCancelled(true);
        p.updateInventory();
        p.chat("/loja");
      }
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void Respawn(PlayerRespawnEvent e) {
    final Player p = e.getPlayer();
    InvManager.getInvManager().InitialItems(p);
    if (this.plugin.getConfig().contains("spawn"))
      Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
        public void run() {
          Location loc = new Location(Bukkit.getWorld(PlayerListener.this.plugin.getConfig().getString("spawn.world")), 
            PlayerListener.this.plugin.getConfig().getDouble("spawn.x"), 
            PlayerListener.this.plugin.getConfig().getDouble("spawn.y"), 
            PlayerListener.this.plugin.getConfig().getDouble("spawn.z"));
          p.teleport(loc);
        }
      }
      , 10L);
  }

  @EventHandler
  public void Join(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    Kit k = KitManager.getKitManager().getPlayerKit(p);
    k.removePlayer(p);
    p.getInventory().clear();
    p.getInventory().setArmorContents(null);
    InvManager.getInvManager().InitialItems(p);
    Location loc = new Location(Bukkit.getWorld(this.plugin.getConfig().getString("spawn.world")), 
      this.plugin.getConfig().getDouble("spawn.x"), 
      this.plugin.getConfig().getDouble("spawn.y"), 
      this.plugin.getConfig().getDouble("spawn.z"));
    p.teleport(loc);
    p.setMaxHealth(20.0D);
  }
  @EventHandler
  public void Death(PlayerDeathEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = e.getEntity();
      Kit k = KitManager.getKitManager().getPlayerKit(p);
      k.removePlayer(p);
      p.getInventory().clear();
      p.getInventory().setArmorContents(null);
    }
  }

  @EventHandler
  public void EntityDamg(EntityDamageByEntityEvent e) {
    if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
      final Player p = (Player)e.getEntity();
      final Player d = (Player)e.getDamager();
      if ((e.getDamage() > 0.0D) && (!e.isCancelled())) {
        if (!combat.containsKey(p)) {
          combat.put(p, d);
          p.sendMessage(String.format(this.combatCom, new Object[] { d.getName() }));
          Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
              if ((PlayerListener.combat.containsKey(p)) && 
                (PlayerListener.combat.get(p) == d)) {
                PlayerListener.combat.remove(p);
                p.sendMessage(ChatColor.AQUA + "Voce nao esta mais em combate. Agora pode deslogar !");
              }
            }
          }
          , 200L);
        } else {
          Player k = (Player)combat.get(p);
          if (d != k) {
            combat.remove(p);
            combat.put(p, d);
            p.sendMessage(String.format(this.combatCom, new Object[] { d.getName() }));
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
              public void run() {
                if ((PlayerListener.combat.containsKey(p)) && 
                  (PlayerListener.combat.get(p) == d)) {
                  PlayerListener.combat.remove(p);
                  p.sendMessage(ChatColor.AQUA + "Voce nao esta mais em combate. Agora pode deslogar !");
                }
              }
            }
            , 200L);
          }

        }

        if (!combat.containsKey(d)) {
          combat.put(d, p);
          d.sendMessage(String.format(this.combatCom, new Object[] { p.getName() }));
          Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
              if ((PlayerListener.combat.containsKey(d)) && 
                (PlayerListener.combat.get(d) == p)) {
                PlayerListener.combat.remove(d);
                d.sendMessage(ChatColor.AQUA + "Voce nao esta mais em combate. Agora pode deslogar !");
              }
            }
          }
          , 200L);
        } else {
          Player k = (Player)combat.get(d);
          if (p != k) {
            combat.remove(d);
            combat.put(d, p);
            d.sendMessage(String.format(this.combatCom, new Object[] { p.getName() }));
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
              public void run() {
                if ((PlayerListener.combat.containsKey(d)) && 
                  (PlayerListener.combat.get(d) == p)) {
                  PlayerListener.combat.remove(d);
                  d.sendMessage(ChatColor.AQUA + "Voce nao esta mais em combate. Agora pode deslogar !");
                }
              }
            }
            , 200L);
          }
        }
      }
    }
  }
}