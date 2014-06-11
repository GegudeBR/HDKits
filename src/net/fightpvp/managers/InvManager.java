package net.fightpvp.managers;

import net.fightpvp.configs.ConfigManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class InvManager
{
  private static InvManager instance = new InvManager();
  public static HashMap<String, Material> KitList = new HashMap();
  public Inventory kit;
  public Inventory kit2;

  public static InvManager getInvManager()
  {
    return instance;
  }

  public void setLayer(Inventory inv, int layer, ItemStack a, ItemStack b, ItemStack c, ItemStack d, ItemStack e, ItemStack f, ItemStack g, ItemStack h, ItemStack i) {
    switch (layer) {
    case 1:
      inv.setItem(0, a);
      inv.setItem(1, b);
      inv.setItem(2, c);
      inv.setItem(3, d);
      inv.setItem(4, e);
      inv.setItem(5, f);
      inv.setItem(6, g);
      inv.setItem(7, h);
      inv.setItem(8, i);
      break;
    case 2:
      inv.setItem(9, a);
      inv.setItem(10, b);
      inv.setItem(11, c);
      inv.setItem(12, d);
      inv.setItem(13, e);
      inv.setItem(14, f);
      inv.setItem(15, g);
      inv.setItem(16, h);
      inv.setItem(17, i);
      break;
    case 3:
      inv.setItem(18, a);
      inv.setItem(19, b);
      inv.setItem(20, c);
      inv.setItem(21, d);
      inv.setItem(22, e);
      inv.setItem(23, f);
      inv.setItem(24, g);
      inv.setItem(25, h);
      inv.setItem(26, i);
      break;
    case 4:
      inv.setItem(27, a);
      inv.setItem(28, b);
      inv.setItem(29, c);
      inv.setItem(30, d);
      inv.setItem(31, e);
      inv.setItem(32, f);
      inv.setItem(33, g);
      inv.setItem(34, h);
      inv.setItem(35, i);
      break;
    case 5:
      inv.setItem(36, a);
      inv.setItem(37, b);
      inv.setItem(38, c);
      inv.setItem(39, d);
      inv.setItem(40, e);
      inv.setItem(41, f);
      inv.setItem(42, g);
      inv.setItem(43, h);
      inv.setItem(44, i);
    }
  }

  public void InitialItems(Player p)
  {
    PlayerInventory inv = p.getInventory();
    inv.clear();
    inv.setArmorContents(null);
    inv.setItem(1, toFill(Material.IRON_FENCE, " "));
    inv.setItem(2, toFill(Material.BOOK, 
      ChatColor.GRAY + ""+ ChatColor.ITALIC + "<<x " + 
      ChatColor.DARK_AQUA + ChatColor.BOLD + "Warps" + 
      ChatColor.GRAY + ChatColor.ITALIC + " x>>"));
    inv.setItem(3, toFill(Material.IRON_FENCE, " "));
    inv.setItem(4, toFill(Material.CHEST, 
      ChatColor.GRAY + ""+ ChatColor.ITALIC + "<<x " + 
      ChatColor.GREEN + ChatColor.BOLD + "Kits" + 
      ChatColor.GRAY + ChatColor.ITALIC + " x>>"));
    inv.setItem(5, toFill(Material.IRON_FENCE, " "));
    inv.setItem(6, toFill(Material.GOLD_INGOT, 
      ChatColor.GRAY + ""+ ChatColor.ITALIC + "<<x " + 
      ChatColor.GOLD + ChatColor.BOLD + "Loja" + 
      ChatColor.GRAY + ChatColor.ITALIC + " x>>"));
    inv.setItem(7, toFill(Material.IRON_FENCE, " "));
  }

  public void newKitDescription(Inventory inv, List<String> lore) {
    ItemStack i = new ItemStack(Material.BOOK);
    ItemMeta m = i.getItemMeta();
    m.setDisplayName(ChatColor.YELLOW + "Descricao");
    m.setLore(lore);
    i.setItemMeta(m);

    inv.setItem(20, i);
  }

  public void newKitStrategie(Inventory inv, List<String> lore) {
    ItemStack i = new ItemStack(Material.COMPASS);
    ItemMeta m = i.getItemMeta();
    m.setDisplayName(ChatColor.BLUE + "Dicas");
    m.setLore(lore);
    i.setItemMeta(m);

    inv.setItem(22, i);
  }

  public void newKitAbilities(Inventory inv, List<String> lore) {
    ItemStack i = new ItemStack(Material.EMERALD);
    ItemMeta m = i.getItemMeta();
    m.setDisplayName(ChatColor.GREEN + "Abilidade");
    m.setLore(lore);
    i.setItemMeta(m);

    inv.setItem(24, i);
  }

  public void setKitInventoryThings(Inventory inv, boolean perm) {
    ItemStack nada = newItem(Material.CARPET, 8, " ");
    inv.setItem(0, nada); inv.setItem(8, nada);
    if (perm) {
      ItemStack escolher = toFill(Material.FIRE, "Jogar com este kit");
      inv.setItem(44, escolher);
    } else {
      ItemStack escolher = toFill(Material.GOLD_INGOT, "Comprar este kit");
      inv.setItem(44, escolher);
    }
    ItemStack items = toFill(Material.SIGN, "Items Especiais");
    inv.setItem(36, items);
    ItemStack po = toFill(Material.SUGAR, ChatColor.GREEN + "Voltar");
    inv.setItem(4, po);
  }

  public void setKitEspecialItems(Inventory inv, Kit k) {
    for (ItemStack i : k.getItems())
      inv.setItem(inv.firstEmpty(), i);
  }

  public void KitPreChooseInv(Player p, Kit k)
  {
    String name = k.getName();
    ItemStack v = new ItemStack(Material.AIR);

    Inventory inv = Bukkit.createInventory(p, 54, ChatColor.DARK_GRAY + "Kit " + name);
    if (p.hasPermission("kit." + name))
      setKitInventoryThings(inv, true);
    else {
      setKitInventoryThings(inv, false);
    }
    List lista = new ArrayList();
    for (String s : ConfigManager.getConfigs().getKitsConfig().getStringList("kits." + name + ".abilidade")) {
      lista.add(ChatColor.WHITE + s);
    }
    newKitAbilities(inv, lista);

    List strag = new ArrayList();
    for (String s : ConfigManager.getConfigs().getKitsConfig().getStringList("kits." + name + ".estrategia")) {
      strag.add(ChatColor.WHITE + s);
    }
    newKitStrategie(inv, strag);

    Object desc = new ArrayList();
    for (String s : ConfigManager.getConfigs().getKitsConfig().getStringList("kits." + name + ".descricao")) {
      ((List)desc).add(ChatColor.WHITE + s);
    }
    newKitDescription(inv, (List)desc);

    Fill(inv, toFill(Material.THIN_GLASS, " "));
    inv.setItem(45, v); inv.setItem(46, v); inv.setItem(47, v); inv.setItem(48, v); inv.setItem(49, v); inv.setItem(45, v);
    setKitEspecialItems(inv, k);
    Fill(inv, toFill(Material.THIN_GLASS, " "));
    p.openInventory(inv);
  }

  public void KitsInv(Player p) {
    this.kit = Bukkit.createInventory(p, 54, ChatColor.DARK_GRAY + "Kits");
    this.kit2 = Bukkit.createInventory(p, 54, ChatColor.DARK_GRAY + "Kits");
    getInvManager().setFirstBar(this.kit, false);
    getInvManager().setFirstBar(this.kit2, true);

    for (Kit k : KitManager.getKitManager().getKitList()) {
      String s = k.getName();

      ItemStack i = new ItemStack(k.getMaterial());
      ItemMeta m = i.getItemMeta();
      m.setDisplayName(ChatColor.GRAY + s);
      i.setItemMeta(m);

      if (p.hasPermission("kit." + s.toLowerCase()))
        this.kit.setItem(this.kit.firstEmpty(), i);
      else {
        this.kit2.setItem(this.kit2.firstEmpty(), i);
      }
    }
    getInvManager().Fill(this.kit, getInvManager().toFill(Material.THIN_GLASS, " "));
    getInvManager().Fill(this.kit2, getInvManager().toFill(Material.THIN_GLASS, " "));
    p.openInventory(this.kit);
  }

  public void setFirstBar(Inventory inv, boolean inv2) {
    if (!inv2) {
      ItemStack v = newItem(Material.THIN_GLASS, 0, " ");
      inv.setItem(0, newItem(Material.CARPET, 8, " "));
      inv.setItem(1, v);
      inv.setItem(2, v);
      inv.setItem(3, v);
      inv.setItem(4, v);
      inv.setItem(5, v);
      inv.setItem(6, v);
      inv.setItem(7, v);
      inv.setItem(8, newItem(Material.CARPET, 1, ChatColor.GOLD + "Passar Pagina"));
    } else {
      ItemStack v = newItem(Material.THIN_GLASS, 0, " ");
      inv.setItem(0, newItem(Material.CARPET, 1, ChatColor.GOLD + "Voltar Pagina"));
      inv.setItem(1, v);
      inv.setItem(2, v);
      inv.setItem(3, v);
      inv.setItem(4, v);
      inv.setItem(5, v);
      inv.setItem(6, v);
      inv.setItem(7, v);
      inv.setItem(8, newItem(Material.CARPET, 8, " "));
    }
  }

  public ItemStack newItem(Material mat, int data, String name) {
    ItemStack i = new ItemStack(mat, 0, (byte)data);
    ItemMeta m = i.getItemMeta();
    m.setDisplayName(name);
    i.setItemMeta(m);
    return i;
  }

  public ItemStack toFill(Material mat, String name) {
    ItemStack i = new ItemStack(mat);
    ItemMeta im = i.getItemMeta();
    im.setDisplayName(name);
    i.setItemMeta(im);
    return i;
  }

  public void Fill(Inventory inv, ItemStack toFill) {
    fillGUI(inv, inv.getSize(), toFill);
  }

  private static void fillGUI(Inventory inv, int count, ItemStack toFill) {
    for (int i = 0; i < inv.getSize(); i++)
      if (inv.getItem(i) == null) {
        if (count <= 0) break;
        inv.setItem(i, toFill);
        count--;
      }
  }
}