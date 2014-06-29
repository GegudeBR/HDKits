package net.fightpvp.comandos;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.KitManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Kill
  implements CommandExecutor, Listener
{
  public Fight plugin;
  KitManager kitmg = KitManager.getKitManager();

  public Kill(Fight plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (label.equalsIgnoreCase("kill") && sender.hasPermission("fight.admin")) {
    	if(args.length == 1){
    		Player a = sender.getServer().getPlayer(args[0]);
    		if(a != null) {
    		a.setHealth(0);
    		sender.sendMessage("§cVoce matou " + args[0]);
    	} else {
    		sender.sendMessage("§cErro jogador inexistente!");
    	}
    } else {
		sender.sendMessage("§cUse: /kill <jogador>");
	 }
    }	
    return false;
  }
}