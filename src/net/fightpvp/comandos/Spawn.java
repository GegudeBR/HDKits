package net.fightpvp.comandos;

import net.fightpvp.main.Fight;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;


public class Spawn implements CommandExecutor 
{
	  public Spawn(Fight fight) {
	}

	public void onEnable()
	  {
	    PluginDescriptionFile pdfFile = getDescription();
	    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	  }

	  private PluginDescriptionFile getDescription() {

		return null;
	}

	public void onDisable() {
	    PluginDescriptionFile pdfFile = getDescription();
	    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " stopping...");
	  }

	  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	  {
	    if (!(sender instanceof Player)) {
	      sender.sendMessage(ChatColor.RED + "Sorry :/ apenas para players!");
	      return true;
	    }
	    Player player = (Player)sender;

	    if (command.getName().equalsIgnoreCase("setspawn")) {
	      if (!player.hasPermission("fight.setspawn")) {
	        player.sendMessage(ChatColor.RED + "Voce nao pode fazer isso.");
	        return true;
	      }
	      Location spawn = player.getLocation();
	      player.getWorld().setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
	      player.sendMessage(ChatColor.RED + "Spawn Marcado!");
	    } else if (command.getName().equalsIgnoreCase("spawn")) {
	      if (!player.hasPermission("fight.spawn")) {
	        player.sendMessage(ChatColor.RED + "Voce nao pode fazer isso.");
	        return true;
	      }

	      Location spawn = player.getWorld().getSpawnLocation();
	      spawn.setX(spawn.getBlockX() + 0.5D);
	      spawn.setY(spawn.getBlockY());
	      spawn.setZ(spawn.getBlockZ() + 0.5D);
	      player.teleport(spawn);
	    } else {
	      return false;
	    }return true;
	  }
	}