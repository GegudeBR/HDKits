package net.fightpvp.managers;

import java.util.ArrayList;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class KitManager
{
  private static KitManager instance = new KitManager();
  private ArrayList<Kit> kits = new ArrayList();

  KitManager kitmg = getKitManager();
  InvManager invmg = InvManager.getInvManager();

  public static KitManager getKitManager()
  {
    return instance;
  }

  public void setupKits(Plugin p) {
    Kit stomper = new Kit("Stomper"); stomper.setMaterial(Material.LEATHER_BOOTS); this.kits.add(stomper);

    Kit switcher = new Kit("Switcher"); switcher.setMaterial(Material.SNOW_BALL); this.kits.add(switcher);
    ArrayList b = new ArrayList();
    b.add(new ItemStack(Material.SNOW_BALL, 16));
    switcher.setItems(b);

    Kit pvp = new Kit("Pvp"); pvp.setMaterial(Material.STONE_SWORD); this.kits.add(pvp);

    Kit archer = new Kit("Archer"); archer.setMaterial(Material.BOW); this.kits.add(archer);
    ArrayList d = new ArrayList();
    ItemStack arco = new ItemStack(Material.BOW);
    arco.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
    d.add(arco);
    d.add(new ItemStack(Material.ARROW));
    archer.setItems(d);

    Kit kangaroo = new Kit("Kangaroo"); kangaroo.setMaterial(Material.FIREWORK); this.kits.add(kangaroo);
    ArrayList e = new ArrayList();
    e.add(new ItemStack(Material.FIREWORK));
    kangaroo.setItems(e);

    Kit snail = new Kit("Snail"); snail.setMaterial(Material.STRING); this.kits.add(snail);

    Kit turtle = new Kit("Turtle"); turtle.setMaterial(Material.IRON_CHESTPLATE); this.kits.add(turtle);

    Kit tank = new Kit("Tank"); tank.setMaterial(Material.TNT); this.kits.add(tank);

    Kit grappler = new Kit("Grappler"); grappler.setMaterial(Material.LEASH); this.kits.add(grappler);
    ArrayList i = new ArrayList();
    i.add(new ItemStack(Material.LEASH));
    grappler.setItems(i);

    Kit checkpoint = new Kit("Checkpoint"); checkpoint.setMaterial(Material.FLOWER_POT_ITEM); this.kits.add(checkpoint);
    ArrayList j = new ArrayList();
    ItemStack no = InvManager.getInvManager().toFill(Material.NETHER_FENCE, ChatColor.GREEN + "CheckPoint");
    ItemStack pointer = InvManager.getInvManager().toFill(Material.FLOWER_POT_ITEM, ChatColor.GREEN + "Teleporte");
    j.add(no);
    j.add(pointer);
    checkpoint.setItems(j);

    Kit endermage = new Kit("Endermage"); endermage.setMaterial(Material.PORTAL); this.kits.add(endermage);
    ArrayList k = new ArrayList();
    ItemStack portal = new ItemStack(Material.PORTAL);
    k.add(portal);
    endermage.setItems(k);

    Kit fisherman = new Kit("Fisherman"); fisherman.setMaterial(Material.FISHING_ROD); this.kits.add(fisherman);
    ArrayList l = new ArrayList();
    l.add(new ItemStack(fisherman.getMaterial()));
    fisherman.setItems(l);

    Kit poseidon = new Kit("Poseidon"); poseidon.setMaterial(Material.WATER_BUCKET); this.kits.add(poseidon);

    Kit specialist = new Kit("Specialist"); specialist.setMaterial(Material.EXP_BOTTLE); this.kits.add(specialist);
    ArrayList m = new ArrayList();
    m.add(InvManager.getInvManager().toFill(Material.BOOK, ChatColor.YELLOW + "Echantment table"));
    specialist.setItems(m);

    Kit monk = new Kit("Monk"); monk.setMaterial(Material.BLAZE_ROD); this.kits.add(monk);
    ArrayList n = new ArrayList();
    n.add(InvManager.getInvManager().toFill(Material.BLAZE_ROD, ChatColor.GOLD + "Monk Staff"));
    monk.setItems(n);

    Kit anchor = new Kit("Anchor"); anchor.setMaterial(Material.IRON_BLOCK); this.kits.add(anchor);

    Kit hulk = new Kit("Hulk"); hulk.setMaterial(Material.DISPENSER); this.kits.add(hulk);

    Kit copycat = new Kit("Copycat"); copycat.setMaterial(Material.SUGAR); this.kits.add(copycat);

    Kit urgal = new Kit("Urgal"); urgal.setMaterial(Material.EMERALD); this.kits.add(urgal);
    ArrayList y = new ArrayList();
    y.add(InvManager.getInvManager().toFill(Material.EMERALD, ChatColor.GREEN + "Pocao de forca"));
    urgal.setItems(y);

    Kit thor = new Kit("Thor"); thor.setMaterial(Material.STONE_AXE); this.kits.add(thor);
    ArrayList z = new ArrayList();
    z.add(new ItemStack(thor.getMaterial()));
    thor.setItems(z);

    Kit gladiator = new Kit("Gladiator"); gladiator.setMaterial(Material.IRON_FENCE); this.kits.add(gladiator);
    ArrayList ze = new ArrayList();
    ze.add(new ItemStack(gladiator.getMaterial()));
    gladiator.setItems(ze);

    Kit timelord = new Kit("Timelord"); timelord.setMaterial(Material.WATCH); this.kits.add(timelord);
    ArrayList asd = new ArrayList();
    ze.add(InvManager.getInvManager().toFill(Material.WATCH, "§6Timelord"));
    timelord.setItems(asd);
  }

  public void sendPlayerKitMessage(Player p) {
    p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "FightPvP" + ChatColor.GRAY + "] Voce escolheu o kit : " + ChatColor.GOLD + getPlayerKit(p).getName());
  }

  public void giveKit(Player p, Kit k)
  {
    String kitname = k.getName();
    p.getInventory().clear();
    PlayerInventory pi = p.getInventory();
    pi.clear();
    pi.setArmorContents(null);
    String str1;
    switch ((str1 = kitname).hashCode()) { case -2012000414:
      if (str1.equals("Timelord"));
      break;
    case -1778555556:
      if (str1.equals("Turtle"));
      break;
    case -1676789727:
      if (str1.equals("Copycat"));
      break;
    case -1598349803:
      if (str1.equals("Fisherman"));
      break;
    case -1378859433:
      if (str1.equals("Gladiator"));
      break;
    case -219755362:
      if (str1.equals("Stomper"));
      break;
    case -168473574:
      if (str1.equals("Endermage"));
      break;
    case -20632831:
      if (str1.equals("Switcher"));
      break;
    case 80650:
      if (str1.equals("Pvp"));
      break;
    case 2260844:
      if (str1.equals("Hulk"));
      break;
    case 2404095:
      if (str1.equals("Monk"));
      break;
    case 2599178:
      if (str1.equals("Tank"));
      break;
    case 2605943:
      if (str1.equals("Thor"));
      break;
    case 80025833:
      if (str1.equals("Snail"));
      break;
    case 81997557:
      if (str1.equals("Urgal"));
      break;
    case 165592355:
      if (str1.equals("Grappler"));
      break;
    case 808422507:
      if (str1.equals("Poseidon"));
      break;
    case 995511633:
      if (str1.equals("Specialist"));
      break;
    case 1002632192:
      if (str1.equals("Kangaroo"));
      break;
    case 1490694568:
      if (str1.equals("Checkpoint"));
      break;
    case 1965534933:
      if (str1.equals("Anchor")) break; break;
    case 1969228707:
      if (!str1.equals("Archer")) { return;
      } else
      {
        pi.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
        ItemStack arco = new ItemStack(Material.BOW);
        arco.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        pi.addItem(new ItemStack[] { arco });
        pi.setItem(17, new ItemStack(Material.ARROW));
        pi.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        return;
      } }
  }

  public ArrayList<Kit> getKitList()
  {
    return this.kits;
  }

  public boolean hasKit(String kitname) {
    kitname = kitname.toLowerCase();

    char[] c = kitname.toCharArray();
    c[0] = Character.toUpperCase(c[0]);
    kitname = new String(c);

    for (Kit k : this.kits)
      if (k.getName().equalsIgnoreCase(kitname))
        return true;
    return false;
  }

  public boolean hasAbility(Player p, String kitname) {
    kitname = kitname.toLowerCase();

    char[] c = kitname.toCharArray();
    c[0] = Character.toUpperCase(c[0]);
    kitname = new String(c);

    for (Kit s : this.kits)
      if ((s.getName().equalsIgnoreCase(kitname)) && 
        (s.getPlayers().contains(p)))
        return true;
    return false;
  }

  public boolean hasOneKit(Player p) {
    for (Kit s : this.kits) {
      if (s.getPlayers().contains(p)) {
        return true;
      }
    }
    return false;
  }

  public Kit getKit(String kitname)
  {
    kitname = kitname.toLowerCase();

    char[] c = kitname.toCharArray();
    c[0] = Character.toUpperCase(c[0]);
    kitname = new String(c);

    for (Kit s : this.kits)
      if (s.getName().equalsIgnoreCase(kitname))
        return s;
    Bukkit.getServer().getConsoleSender().sendMessage("§cKit " + kitname + " nao existe !");
    return null;
  }

  public Kit getPlayerKit(Player p) {
    for (Kit k : this.kits)
      if (k.getPlayers().contains(p))
        return k;
    Kit n = new Kit("Nenhum");
    return n;
  }
}