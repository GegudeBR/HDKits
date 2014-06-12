package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Stomper
  implements CommandExecutor, Listener
{
  public Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  public Stomper(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("stomper")) {
      if (p.hasPermission("kit.stomper")) {
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
  public void EntityDamage(EntityDamageEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = (Player)e.getEntity();
      if (this.kitmg.hasAbility(p, "stomper"))
      {
        if ((e.getCause() == EntityDamageEvent.DamageCause.FALL) && 
          (e.getDamage() > 4.0D)) {
          e.setCancelled(true);
          p.damage(4.0D);

          for (Entity ent : p.getNearbyEntities(3.5D, 3.5D, 3.5D))
            if ((ent instanceof Player)) {
              Player t = (Player)ent;

              if (!t.isSneaking())
                t.damage(e.getDamage(), p);
              else {
                t.damage(e.getDamage() / 6.0D, p);
              }

              if (t.isDead())
                t.sendMessage("§fVoce foi estompado por §a" + p.getName());
            }
        }
      }
    }
  }
}