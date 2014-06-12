package net.fightpvp.comandos;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.InvManager;
import net.fightpvp.managers.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kits
  implements CommandExecutor
{
  public Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  public Kits(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;
    if (label.equalsIgnoreCase("kits")) {
      InvManager.getInvManager().KitsInv(p);
    }
    return false;
  }
}