package net.fightpvp.comandos;

import java.util.logging.Logger;

import net.fightpvp.main.Fight;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


public class Spawn implements CommandExecutor, Listener 
{
	 public Fight plugin;
	 public final Logger logger = Logger.getLogger("Minecraft");
	 public Location spawn;
	 public Spawn(Fight plugin) {
	    this.plugin = plugin;
	  }

	  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	  {
	    if (!(sender instanceof Player)) {
	      sender.sendMessage(ChatColor.RED + "Sorry :/ apenas para players!");
	      return true;
	    }
	    Player player = (Player)sender;

	    if (command.getName().equalsIgnoreCase("setspawn")) {
	        if (player.hasPermission("fight.setspawn"))
	        {
	            World world = player.getLocation().getWorld();
	            double x = player.getLocation().getX();
	            double y = player.getLocation().getY();
	            double z = player.getLocation().getZ();
	            float yaw = player.getLocation().getYaw();
	            float pitch = player.getLocation().getPitch();
	            FileConfiguration config = plugin.getConfig();
	            config.set("spawn.world", world.getName().toString());
	            config.set("spawn.x", Double.valueOf(x));
	            config.set("spawn.y", Double.valueOf(y));
	            config.set("spawn.z", Double.valueOf(z));
	            config.set("spawn.yaw", Float.valueOf(yaw));
	            config.set("spawn.pitch", Float.valueOf(pitch));
	            plugin.saveConfig();
	            world.setSpawnLocation((int)x, (int)y, (int)z);
	            this.spawn = new Location(world, x, y, z, yaw, pitch);
	            sender.sendMessage(ChatColor.GREEN + "Spawn point set");
	        }
	      }
	    
	    if (command.getName().equalsIgnoreCase("spawn")) {
	      if (!player.hasPermission("fight.spawn")) {
	        player.sendMessage(ChatColor.RED + "Voce nao pode fazer isso.");
	        return true;
	      }
	      FileConfiguration config = plugin.getConfig();
	      this.spawn = new Location(Bukkit.getWorld(config.getString("spawn.world")), 
	        Double.parseDouble(config.getString("spawn.x")), 
	        Double.parseDouble(config.getString("spawn.y")), 
	        Double.parseDouble(config.getString("spawn.z")), 
	        Float.parseFloat(config.getString("spawn.yaw")), 
	        Float.parseFloat(config.getString("spawn.pitch")));
	      ((Player)sender).teleport(this.spawn);
          return true;
	    } 
	    return true;
	  }
}
