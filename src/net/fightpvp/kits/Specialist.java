package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Specialist
  implements CommandExecutor, Listener
{
  public Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  public Specialist(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("specialist")) {
      if (p.hasPermission("kit.specialist")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.addItem(new ItemStack[] { InvManager.getInvManager().toFill(Material.BOOK, ChatColor.YELLOW + "Echantment table") });
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
  public void GainXp(PlayerExpChangeEvent e) {
    if (this.kitmg.hasAbility(e.getPlayer(), "specialist"))
      e.setAmount(e.getPlayer().getExpToLevel());
  }

  @EventHandler
  public void InteractOpen(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    ItemStack i = InvManager.getInvManager().toFill(Material.BOOK, ChatColor.YELLOW + "Echantment table");
    if ((this.kitmg.hasAbility(p, "specialist")) && 
      (p.getItemInHand().equals(i))) {
      e.setCancelled(true);
      if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
        p.openEnchanting(p.getLocation(), true);
    }
  }

  @EventHandler
  public void DeathXP(PlayerDeathEvent e)
  {
    if (((e.getEntity() instanceof Player)) && ((e.getEntity().getKiller() instanceof Player))) {
      Player p = e.getEntity();
      Player k = p.getKiller();
      if (this.kitmg.hasAbility(k, "specialist"))
        k.getInventory().addItem(new ItemStack[] { new ItemStack(Material.EXP_BOTTLE) });
    }
  }
}