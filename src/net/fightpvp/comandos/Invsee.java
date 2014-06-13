package net.fightpvp.comandos;

import net.fightpvp.main.Fight;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Invsee implements CommandExecutor {
  
	  public Fight plugin;

	  public Invsee(Fight plugin) {
	    this.plugin = plugin;
	  }

public boolean onCommand(CommandSender sender, Command cmd, String tag, String[] args) {
    Player p = (Player) sender;
    if (!(sender instanceof Player)) {
        sender.sendMessage(ChatColor.RED + "Voce nao e um jogador!");
        return false;
      }
    if (cmd.getName().equalsIgnoreCase("invsee") && (p.hasPermission("fight.staff"))) {
     if (args.length == 1) {
    	  Player target = p.getServer().getPlayer(args[0]);
    	  if (target != null) {
    		p.openInventory(target.getInventory());
		    p.sendMessage("§6Voce esta vendo o inventario de: " + ChatColor.RED + target.getName());
    	  } else {
    		p.sendMessage("§4Jogador invalido!");
    	  } 
    	   } else {
    		p.sendMessage("§4Erro:§c Use /invsee <jogador>");  
    	   }
    }
	return false;
   }
}