package net.fightpvp.comandos;

import net.fightpvp.main.Fight;
import net.fightpvp.managers.KitManager;
import net.fightpvp.managers.Tag;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Tags
  implements CommandExecutor, Listener
{
  private Fight plugin;
  KitManager kitmg = KitManager.getKitManager();
  static List<Tag> tags = new ArrayList();

  public Tags(Fight plugin)
  {
    this.plugin = plugin;
  }

  public static void setupTags()
  {
    Tag normal = new Tag("Normal", ChatColor.GRAY); tags.add(normal);
    Tag vip = new Tag("Vip", ChatColor.GOLD); tags.add(vip);
    Tag vipplus = new Tag("VipPlus", ChatColor.GOLD, ChatColor.ITALIC); tags.add(vipplus);
    Tag yt = new Tag("Youtuber", ChatColor.AQUA); tags.add(yt);
    Tag trial = new Tag("Trial", ChatColor.LIGHT_PURPLE); tags.add(trial);
    Tag mod = new Tag("Mod", ChatColor.DARK_PURPLE); tags.add(mod);
    Tag adm = new Tag("Admin", ChatColor.RED); tags.add(adm);
    Tag dono = new Tag("Dono", ChatColor.DARK_RED); tags.add(dono);
  }

  public void addPlayer(Player p, Tag t) {
    t.addPlayer(p);

    String nome = p.getName();

    if (nome.length() >= 16) {
      nome.substring(0, 14);
    }

    if (t.colors().size() == 1) {
      p.setDisplayName(t.getColor(0) + nome);
      p.setPlayerListName(t.getColor(0) + nome);
    } else {

    }
  }

  public Tag getTagByName(String name) {
    for (Tag t : tags) {
      if (t.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) return t;
    }
    return null;
  }

  public boolean containsTag(String name) {
    for (Tag t : tags)
      if (t.getName().equalsIgnoreCase(name.toLowerCase())) return true;
    return false;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    Player p = (Player)sender;
    if (label.equalsIgnoreCase("tag")) {
      if (args.length == 0) {
        StringBuilder sb = new StringBuilder();
        List t = new ArrayList();

        for (Tag k : tags) {
          if (p.hasPermission("fight.tag." + k.getName())) {
            t.add(k);
          }
        }

        for (int i = 0; i < t.size(); i++) {
          if (i < t.size() - 1)
            sb.append(ChatColor.RED + ((Tag)t.get(i)).getName() + ChatColor.GRAY + ", ");
          else {
            sb.append(ChatColor.RED + ((Tag)t.get(i)).getName() + ChatColor.GRAY + ".");
          }
        }

        p.sendMessage(ChatColor.RED + "Tags" + ChatColor.GRAY + ": " + sb.toString().trim());
      }
      else if (containsTag(args[0])) {
        Tag t = getTagByName(args[0]);
        String s = t.getName();
        if (p.hasPermission("fight.tag." + s.toLowerCase())) {
          for (Tag k : tags) {
            k.removePlayer(p);
          }
          addPlayer(p, t);

          char[] c = s.toCharArray();
          c[0] = Character.toUpperCase(c[0]);
          s = new String(c);

          p.sendMessage(ChatColor.GRAY + "Usando agora a tag : " + ChatColor.GREEN + s);

          if (t.colors().size() == 1)
            p.setDisplayName(t.getColor(0) + p.getName());

        }
        else {
          p.sendMessage(ChatColor.RED + "Voce nao tem permissao para esta Tag !");
        }
      }
    }

    return false;
  }
  @EventHandler
  public void chat(AsyncPlayerChatEvent e) {
    Player p = e.getPlayer();
    for (Tag t : tags) {
      if (!t.containsPlayer(p))
        continue;
      String msg = e.getMessage();

      if (t.colors().size() == 1)
        e.setFormat("§f<" + t.getColor(0) + p.getName() + "§f> " + ChatColor.RESET + msg);
      else
        e.setFormat("§f<" + t.getColor(0) + t.getColor(1) + p.getName() + "§f> " + ChatColor.RESET + msg);
    }
  }
}