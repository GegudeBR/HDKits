package net.fightpvp.kits;

import java.util.HashMap;
import java.util.Map;

import net.fightpvp.listeners.PlayerListener;
import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Checkpoint
  implements CommandExecutor, Listener
{
  public Fight plugin;
  KitManager kitmg = KitManager.getKitManager();
  InvManager invmg = InvManager.getInvManager();

  HashMap<Player, Location> checkLoc = new HashMap<Player, Location>();

  public Checkpoint(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;

    if (label.equalsIgnoreCase("checkpoint")) {
      if (p.hasPermission("kit.checkpoint")) {
        if (this.kitmg.hasOneKit(p)) {
          p.sendMessage("§cSomente 1 kit por vida !");
        } else {
          Kit a = this.kitmg.getKit(label.toLowerCase());
          a.addPlayer(p);
          this.kitmg.sendPlayerKitMessage(p);

          PlayerInventory pi = p.getInventory();
          pi.clear();


          pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
          ItemStack no = this.invmg.toFill(Material.NETHER_FENCE, ChatColor.GREEN + "CheckPoint");
          ItemStack pointer = this.invmg.toFill(Material.FLOWER_POT_ITEM, ChatColor.GREEN + "Teleporte");
          pi.addItem(new ItemStack[] { no });
          pi.addItem(new ItemStack[] { pointer });
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
  public void DropItem(PlayerDropItemEvent e)
  {
    Player p = e.getPlayer();
    ItemStack no = this.invmg.toFill(Material.NETHER_FENCE, ChatColor.GREEN + "CheckPoint");
    ItemStack pointer = this.invmg.toFill(Material.FLOWER_POT_ITEM, ChatColor.GREEN + "Teleporte");
    if ((this.kitmg.hasAbility(p, "checkpoint")) && (
      (e.getItemDrop().getItemStack().equals(no)) || (e.getItemDrop().getItemStack().equals(pointer))))
      e.setCancelled(true);
  }

  @EventHandler
  public void CheckPointRemoveQuit(PlayerQuitEvent e)
  {
    Player p = e.getPlayer();
    if (this.checkLoc.containsKey(p)) {
      Block b = this.checkLoc.get(p).getBlock();
      b.setType(Material.AIR);
      this.checkLoc.remove(p);
    }
  }

  @EventHandler
  public void CheckPointRemoveDeath(PlayerDeathEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = e.getEntity();
      if (this.checkLoc.containsKey(p)) {
        Block b = this.checkLoc.get(p).getBlock();
        b.setType(Material.AIR);
        this.checkLoc.remove(p);
      }
    }
  }

  @SuppressWarnings("rawtypes")
@EventHandler
  public void CheckPointRemoveInteract(PlayerInteractEvent e) {
    if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
      Block CheckPoint = e.getClickedBlock();

      for (Map.Entry entry : checkLoc.entrySet()) {
        Player dono = (Player)entry.getKey();

        Block Point = checkLoc.get(dono).getBlock();
        if (checkLoc.containsValue(CheckPoint.getLocation())) {
          Point.getWorld().playEffect(Point.getLocation(), Effect.SMOKE, 4);
          Point.setType(Material.AIR);
          checkLoc.remove(dono);
        }
      }
    }
  }

  @SuppressWarnings("deprecation")
@EventHandler
  public void CheckPointPlace(BlockPlaceEvent e) {
    Player p = e.getPlayer();
    if (this.kitmg.hasAbility(p, "checkpoint")) {
      Block b = e.getBlock();
      if (b.getType() == Material.NETHER_FENCE)
        if (!this.checkLoc.containsKey(p)) {
          this.checkLoc.put(p, b.getLocation());
          ItemStack point = this.invmg.toFill(Material.NETHER_FENCE, ChatColor.GREEN + "CheckPoint");
          p.getInventory().remove(point);
          p.getInventory().addItem(new ItemStack[] { point });
          p.updateInventory();
        } else {
          Block checkBlock = this.checkLoc.get(p).getBlock();
          checkBlock.setType(Material.AIR);
          this.checkLoc.remove(p);
          this.checkLoc.put(p, b.getLocation());
          ItemStack point = this.invmg.toFill(Material.NETHER_FENCE, ChatColor.GREEN + "CheckPoint");
          p.getInventory().remove(point);
          p.getInventory().addItem(new ItemStack[] { point });
          p.updateInventory();
        }
    }
  }

  @SuppressWarnings("deprecation")
@EventHandler
  public void CheckPointInteract(PlayerInteractEvent e)
  {
    Player p = e.getPlayer();
    ItemStack pointer = this.invmg.toFill(Material.FLOWER_POT_ITEM, ChatColor.GREEN + "Teleporte");
    if ((this.kitmg.hasAbility(p, "checkpoint")) && 
      ((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR)) && 
      (p.getItemInHand().equals(pointer))) {
      e.setCancelled(true);
      p.updateInventory();
      if (this.checkLoc.containsKey(p)) {
        if (PlayerListener.combat.containsKey(p))
          p.sendMessage(ChatColor.AQUA + "Voce nao pode teleportar em combate");
        else
          p.teleport(this.checkLoc.get(p));
      }
      else
        p.sendMessage(ChatColor.RED + "Seu local de teleporte ainda nao foi marcado\nou foi destruido");
    }
  }
}