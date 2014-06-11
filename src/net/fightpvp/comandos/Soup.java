package net.fightpvp.comandos;

import me.confuser.barapi.BarAPI;
import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Soup
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();
  InvManager invmg = InvManager.getInvManager();
  ArrayList<Player> insoup = new ArrayList();

  public Soup(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    final Player p = (Player)sender;
    if ((label.equalsIgnoreCase("sopa")) && 
      (!this.insoup.contains(p)) && (!this.kitmg.getPlayerKit(p).getName().equalsIgnoreCase("Nenhum"))) {
      this.insoup.add(p);
      p.getInventory().clear();
      p.getInventory().setArmorContents(null);
      p.getInventory().setHelmet(new ItemStack(Material.WOOL));
      p.getInventory().setChestplate(new ItemStack(Material.WOOL));
      p.getInventory().setLeggings(new ItemStack(Material.WOOL));
      p.getInventory().setBoots(new ItemStack(Material.WOOL));
      new BukkitRunnable() {
        int time = 5;

        public void run() { if (Soup.this.insoup.contains(p)) {public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
            final ItemStack sopa = new ItemStack(Material.MUSHROOM_SOUP);
            ItemMeta sMeta = sopa.getItemMeta();
            sMeta.setDisplayName(ChatColor.RED + "Sopa");
            sopa.setItemMeta(sMeta);
            final Player player = (Player)sender;

            if (commandLabel.equalsIgnoreCase("sopa")) {
              final ItemStack[] inventoryContents = player.getInventory().getContents();
              final ItemStack[] armorContents = player.getInventory().getArmorContents();
              player.getInventory().clear();
              player.getInventory().setArmorContents(null);
              player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 9));
              player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 9));
              plugin.weak.add(player.getName());
              player.playSound(player.getLocation(), Sound.ZOMBIE_WOOD, 10.0F, 1.0F);
              BarAPI.setMessage(player, ChatColor.GOLD + "Resoup em 5 segundos!", 5);
              player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 9));
              player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1 ,(byte) 14));
              player.getInventory().setChestplate(new ItemStack(Material.WOOL, 1 ,(byte) 14));
              player.getInventory().setLeggings(new ItemStack(Material.WOOL, 1 ,(byte) 14));
              player.getInventory().setBoots(new ItemStack(Material.WOOL, 1 ,(byte) 14));
              plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                  player.getInventory().setArmorContents(armorContents);
                  player.getInventory().setContents(inventoryContents);
                  plugin.weak.remove(player.getName());
                  BarAPI.setMessage(player, ChatColor.GOLD + "Resoup concluido", 1);
                  player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 10.0F, 1.0F);
                  for (int i = 0; i <= 35; i++)
                    player.getInventory().addItem(new ItemStack[] { sopa });
                }
              }
              , 100L);
            }
            return false;
          }
            this.time -= 1;
            ItemStack counter = Soup.this.invmg.newItem(Material.INK_SACK, 8, "§aRefiling: " + this.time);
            p.getWorld().playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            Soup.this.invmg.setLayer(p.getInventory(), 1, counter, counter, counter, counter, counter, counter, counter, counter, counter);
            if ((this.time == 0) && (Soup.this.insoup.contains(p))) {
              cancel();
              p.getInventory().clear();
             p.getInventory().setArmorContents(null);
              Soup.this.kitmg.giveKit(p, Soup.this.kitmg.getPlayerKit(p));
              for (int i = 0; i < 37; i++) {
                p.getInventory().addItem(new ItemStack[] { new ItemStack(Material.MUSHROOM_SOUP) });
              }
              Soup.this.insoup.remove(p);
            }
          } }
      }
      .runTaskTimer(this.plugin, 0L, 20L);
    }

    return false;
  }

  @EventHandler
  public void ClickCancel(InventoryClickEvent e) {
    Player p = (Player)e.getWhoClicked();
    if ((this.insoup.contains(p)) && 
      (e.getCurrentItem().hasItemMeta()) && (e.getCurrentItem().getItemMeta().hasDisplayName()) && (e.getCurrentItem().getItemMeta().getDisplayName().contains("§aRefiling: "))) {
      e.setCancelled(true);
      p.updateInventory();
    }
  }

  @EventHandler
  public void InteractCancel(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    if ((this.insoup.contains(p)) && 
      (p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getItemMeta().hasDisplayName())) p.getItemInHand().getItemMeta().getDisplayName().contains("§aRefiling: ");
  }

  @EventHandler
  public void DropCancel(PlayerDropItemEvent e)
  {
    Player p = e.getPlayer();
    if ((this.insoup.contains(p)) && 
      (e.getItemDrop().getItemStack().hasItemMeta()) && (e.getItemDrop().getItemStack().getItemMeta().hasDisplayName()) && (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("§aRefiling: "))) {
      e.setCancelled(true);
      p.updateInventory();
    }
  }

  @EventHandler
  public void Damage(EntityDamageEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = (Player)e.getEntity();
      if (this.insoup.contains(p)) {
        p.sendMessage("§fDamage: §c-" + e.getDamage());
        p.damage(20.0D);
      }
    }
  }
}