package net.fightpvp.comandos;

import net.fightpvp.listeners.PlayerListener;
import net.fightpvp.main.Fight;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Repair implements CommandExecutor, Listener
{
  public Fight plugin;
  public Repair(Fight plugin){
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
	    final Player player = (Player)sender;

	    if (commandLabel.equalsIgnoreCase("repair")) {
	    	if (!PlayerListener.combat.containsKey(player)) {
	    		player.getInventory().getChestplate().setDurability((short) 0);
		        player.getItemInHand().setDurability((short) 0);
	    	} else {
	    		player.sendMessage("§7[§6FightPvP§7] Voce nao pode usar esse comando em combate.");
	    	}
	    }
	    return false;
	  }
}