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
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Fisherman
  implements CommandExecutor, Listener
{
  public Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  public Fisherman(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("fisherman")) {
      if (p.hasPermission("kit.fisherman")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.addItem(new ItemStack[] { new ItemStack(Material.FISHING_ROD) });
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
  public void fish(PlayerFishEvent e) {
    Player p = e.getPlayer();
    if ((this.kitmg.hasAbility(p, "fisherman")) && 
      (e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY)) {
      Entity entity = e.getCaught();
      entity.teleport(p.getLocation());
    }
  }
}