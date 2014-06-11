package net.fightpvp.managers;

import net.fightpvp.main.Fight;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Nearfs
  implements Listener
{
  public static Fight plugin;

  public Nearfs(Fight kitPvP)
  {
    plugin = kitPvP;
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void onDamageByEntity(EntityDamageByEntityEvent event) {
    if ((event.getDamager() instanceof Player)) {
      Player player = (Player)event.getDamager();
      if (event.getDamage() > 1.0D) {
        event.setDamage(event.getDamage() - 1.0D);
      }
      if ((event.getDamager() instanceof Player)) {
        if ((player.getFallDistance() > 0.0F) && (!player.isOnGround()) && 
          (!player.hasPotionEffect(PotionEffectType.BLINDNESS)))
        {
          int NewDamage = (int)(event.getDamage() * 1.5D) - 
            (int)event.getDamage();
          if (event.getDamage() > 1.0D) {
            event.setDamage(event.getDamage() - NewDamage);
          }
        }
        if (player.getItemInHand().getType() == Material.WOOD_SWORD) {
          event.setDamage(3.0D);
        }
        if (player.getItemInHand().getType() == Material.STONE_SWORD) {
          event.setDamage(4.0D);
        }
        if (player.getItemInHand().getType() == Material.IRON_SWORD) {
          event.setDamage(5.0D);
        }
        if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
          event.setDamage(6.0D);
        }
        if (player.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL)) {
          event.setDamage(event.getDamage() + 1.0D);
        }
        if ((player.getFallDistance() > 0.0F) && (!player.isOnGround()) && 
          (!player.hasPotionEffect(PotionEffectType.BLINDNESS))) {
          if (player.getItemInHand().getType() == Material.WOOD_SWORD) {
            event.setDamage(event.getDamage() + 1.0D);
          }
          if (player.getItemInHand().getType() == Material.STONE_SWORD) {
            event.setDamage(event.getDamage() + 0.5D);
          }
          if (player.getItemInHand().getType() == Material.IRON_SWORD) {
            event.setDamage(event.getDamage() + 2.0D);
          }
          if (player.getItemInHand().getType() == Material.DIAMOND_SWORD)
            event.setDamage(event.getDamage() + 2.5D);
        }
      }
    }
  }
}