package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Turtle
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  public Turtle(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("turtle")) {
      if (p.hasPermission("kit.turtle")) {
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
  public void damage(EntityDamageEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = (Player)e.getEntity();
      if ((this.kitmg.hasAbility(p, "turtle")) && 
        (p.isSneaking())) {
        e.setCancelled(true);
        p.damage(2.0D);
      }
    }
  }

  @EventHandler
  public void DamageCancelByTurtle(EntityDamageByEntityEvent e) {
    if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
      Player d = (Player)e.getDamager();
      if ((this.kitmg.hasAbility(d, "turtle")) && 
        (d.isSneaking()))
        e.setCancelled(true);
    }
  }
}