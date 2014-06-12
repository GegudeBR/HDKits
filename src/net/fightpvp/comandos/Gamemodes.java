package net.fightpvp.comandos;

import net.fightpvp.main.Fight;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemodes
  implements CommandExecutor
{
  public Fight plugin;

  public Gamemodes(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    Player p = (Player)sender;
    if ((label.equalsIgnoreCase("c")) && 
      (p.hasPermission("fight.creative"))) {
      p.setGameMode(GameMode.CREATIVE);
    }

    if ((label.equalsIgnoreCase("s")) && 
      (p.hasPermission("fight.survival"))) {
      p.setGameMode(GameMode.SURVIVAL);
    }

    return false;
  }
}