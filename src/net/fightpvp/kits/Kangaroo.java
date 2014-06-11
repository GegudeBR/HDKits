package net.fightpvp.kits;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class Kangaroo
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  ArrayList<String> jumpa = new ArrayList();
  private HashMap<String, Integer> inta = new HashMap();

  public Kangaroo(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("kangaroo")) {
      if (p.hasPermission("kit.kangaroo")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();

          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          pi.addItem(new ItemStack[] { new ItemStack(Material.FIREWORK) });
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
  public void onInteract(PlayerInteractEvent e)
  {
    Player p = e.getPlayer();
    Block b = p.getLocation().getBlock();
    if ((this.kitmg.hasAbility(p, "Kangaroo")) && 
      (p.getItemInHand().getType() == Material.FIREWORK)) {
      e.setCancelled(true);
      if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_AIR)) {
        if ((b.getType() == Material.AIR) && (b.getRelative(BlockFace.DOWN).getType() == Material.AIR))
        {
          this.jumpa.contains(p.getName());
        }

        if ((((Integer)this.inta.get(p.getName())).intValue() == 1) && 
          (p.isSneaking())) {
          Vector v1 = p.getLocation().getDirection().multiply(1.5D).setY(0.45D);
          p.setVelocity(v1);
          this.inta.put(p.getName(), Integer.valueOf(0));
        }

        this.jumpa.contains(p.getName());

        if ((((Integer)this.inta.get(p.getName())).intValue() == 1) && 
          (!p.isSneaking())) {
          Vector v2 = p.getLocation().getDirection().multiply(0.35D).setY(0.9D);
          p.setVelocity(v2);
          this.inta.put(p.getName(), Integer.valueOf(0));
        }
      }
    }
  }

  @EventHandler
  public void dmg(EntityDamageEvent e)
  {
    if ((e.getEntity() instanceof Player)) {
      Player p = (Player)e.getEntity();
      if ((this.kitmg.hasAbility(p, "Kangaroo")) && 
        (e.getCause() == EntityDamageEvent.DamageCause.FALL) && 
        (e.getDamage() >= 7.0D)) {
        e.setCancelled(true);
        p.damage(7.0D);
      }
    }
  }

  @EventHandler
  public void drop(PlayerDropItemEvent e)
  {
    Player p = e.getPlayer();
    if ((this.kitmg.hasAbility(p, "kangaroo")) && 
      (e.getItemDrop().getItemStack().getType() == Material.FIREWORK))
      e.setCancelled(true);
  }

  @EventHandler
  public void fly(PlayerMoveEvent e) {
    Player p = e.getPlayer();
    Block b = p.getLocation().getBlock();
    if (this.kitmg.hasAbility(p, "Kangaroo"))
      if (b.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
        this.jumpa.add(p.getName());
      }
      else if ((b.getType() != Material.AIR) || (b.getRelative(BlockFace.DOWN).getType() != Material.AIR)) {
        this.inta.put(p.getName(), Integer.valueOf(1));
        this.jumpa.remove(p.getName());
      }
  }
}