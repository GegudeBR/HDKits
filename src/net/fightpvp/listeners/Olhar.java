package net.fightpvp.listeners;

import java.util.List;

import me.confuser.barapi.BarAPI;
import net.fightpvp.main.Fight;
import net.fightpvp.managers.KitManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Olhar
  implements Listener
{
  public Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  public Olhar(Fight plugin){
    this.plugin = plugin;
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event)
  {  
    Player p = event.getPlayer();
      List<Entity> meuovo = p.getNearbyEntities(1.0D, 1.0D, 1.0D);
      for (Entity t : meuovo) {
        if (t.getType() == EntityType.PLAYER) {
        	Player p2 = (Player)t;
            int tempo = 1;
         	if (!kitmg.hasKit(p2.getName())) {
                BarAPI.setMessage(p, p2.getName() + " - None", tempo);
              	}
        	if (kitmg.hasAbility(p2, "gladiator")) {
          BarAPI.setMessage(p, p2.getName() + " - Gladiator", tempo);
        	}
        	if (kitmg.hasAbility(p2, "pvp")) {
                BarAPI.setMessage(p, p2.getName() + " - PvP", tempo);
              	}
        	if (kitmg.hasAbility(p2, "archer")) {
                BarAPI.setMessage(p, p2.getName() + " - Archer", tempo);
              	}
        	if (kitmg.hasAbility(p2, "thor")) {
                BarAPI.setMessage(p, p2.getName() + " - Thor", tempo);
              	}
        	if (kitmg.hasAbility(p2, "urgal")) {
                BarAPI.setMessage(p, p2.getName() + " - Urgal", tempo);
              	}
        	if (kitmg.hasAbility(p2, "grappler")) {
                BarAPI.setMessage(p, p2.getName() + " - Grappler", tempo);
              	}
        	if (kitmg.hasAbility(p2, "kangaroo")) {
                BarAPI.setMessage(p, p2.getName() + " - Kangaroo", tempo);
              	}
        	if (kitmg.hasAbility(p2, "stomper")) {
                BarAPI.setMessage(p, p2.getName() + " - Stomper", tempo);
              	}
        	if (kitmg.hasAbility(p2, "checkpoint")) {
                BarAPI.setMessage(p, p2.getName() + " - Checkpoint", tempo);
              	}
        	if (kitmg.hasAbility(p2, "copycat")) {
                BarAPI.setMessage(p, p2.getName() + " - Copycat", tempo);
              	}
        	if (kitmg.hasAbility(p2, "hulk")) {
                BarAPI.setMessage(p, p2.getName() + " - Hulk", tempo);
              	}
        	if (kitmg.hasAbility(p2, "switcher")) {
                BarAPI.setMessage(p, p2.getName() + " - Switcher", tempo);
              	}
        	if (kitmg.hasAbility(p2, "snail")) {
                BarAPI.setMessage(p, p2.getName() + " - Snail", tempo);
              	}
        	if (kitmg.hasAbility(p2, "turtle")) {
                BarAPI.setMessage(p, p2.getName() + " - Turtle", tempo);
              	}
        	if (kitmg.hasAbility(p2, "tank")) {
                BarAPI.setMessage(p, p2.getName() + " - Tank", tempo);
              	}
        	if (kitmg.hasAbility(p2, "fisherman")) {
                BarAPI.setMessage(p, p2.getName() + " - Fisherman", tempo);
              	}
        	if (kitmg.hasAbility(p2, "endermage")) {
                BarAPI.setMessage(p, p2.getName() + " - Endermage", tempo);
              	}
        	if (kitmg.hasAbility(p2, "poseidon")) {
                BarAPI.setMessage(p, p2.getName() + " - Poseidon", tempo);
              	}
        	if (kitmg.hasAbility(p2, "specialist")) {
                BarAPI.setMessage(p, p2.getName() + " - Specialist", tempo);
              	}
        	if (kitmg.hasAbility(p2, "monk")) {
                BarAPI.setMessage(p, p2.getName() + " - Monk", tempo);
              	}
        	if (kitmg.hasAbility(p2, "anchor")) {
                BarAPI.setMessage(p, p2.getName() + " - Anchor", tempo);
              	}
        	if (kitmg.hasAbility(p2, "timelord")) {
                BarAPI.setMessage(p, p2.getName() + " - Timelord", tempo);
              	}
        }
      }
    }

}