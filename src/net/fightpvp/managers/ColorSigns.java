package net.fightpvp.managers;

import net.fightpvp.main.Fight;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class ColorSigns
  implements Listener
{
  public static Fight plugin;

  public ColorSigns(Fight kitPvP)
  {
    plugin = kitPvP;
  }

  @SuppressWarnings("unused")
 	@EventHandler(priority=EventPriority.MONITOR)
 	  public void onSignChange(SignChangeEvent event) {
 	    if (!event.getPlayer().isOp()) return;
 	    Block block = event.getBlock();
 		Sign sign = null;
 	    Material type = block.getType();
 	    if ((type.equals(Material.SIGN)) || (type.equals(Material.SIGN_POST)) || (type.equals(Material.WALL_SIGN)))
 	      sign = (Sign)block.getState();
 	    else {
 	      return;
 	    }
 	    String[] text = event.getLines();
 	    int count = 0;
 	    for (String line : text)
 	    {
 	      line = line.replaceAll("&", "§");
 	      line = line.replaceAll("§§", "&");
 	      event.setLine(count, line);
 	      count++;
 	    }
 	  }
}