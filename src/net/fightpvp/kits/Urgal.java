package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class Urgal
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  ArrayList<Player> cooldown = new ArrayList();

  public Urgal(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("urgal")) {
      if (p.hasPermission("kit.urgal")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.addItem(new ItemStack[] { InvManager.getInvManager().toFill(Material.EMERALD, ChatColor.GREEN + "Pocao de forca") });
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
    if (this.cooldown.contains(p))
      this.cooldown.remove(p);
  }

  @EventHandler
  public void morre(PlayerDeathEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = e.getEntity();
      if (this.cooldown.contains(p))
        this.cooldown.remove(p);
    }
  }

  @EventHandler
  public void Interact(PlayerInteractEvent e) {
    final Player p = e.getPlayer();
    ItemStack a = InvManager.getInvManager().toFill(Material.EMERALD, ChatColor.GREEN + "Pocao de forca");
    if ((this.kitmg.hasAbility(p, "urgal")) && 
      (p.getItemInHand().equals(a))) {
      e.setCancelled(true);
      if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
        if (this.cooldown.contains(p)) {
          p.sendMessage(ChatColor.RED + "Aguarde o cooldown !");
        } else {
          p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2400, 0));
          this.cooldown.add(p);
          Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            public void run() {
              Urgal.this.cooldown.remove(p);
            }
          }
          , 2400L);
        }
    }
  }
}