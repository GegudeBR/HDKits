package net.fightpvp.comandos;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Suicide
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();
  ArrayList<Player> death = new ArrayList<Player>();

  public Suicide(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;
    if (label.equalsIgnoreCase("suicide")) {
      p.setHealth(0.0D);
      this.death.add(p);
    }
    return false;
  }
  @EventHandler
  public void death(PlayerDeathEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = e.getEntity();
      if (this.death.contains(p)) {
        e.setDeathMessage(ChatColor.AQUA + p.getName() + "(" + this.kitmg.getPlayerKit(p).getName() + ") se matou");
        this.death.remove(p);
      }
    }
  }
}