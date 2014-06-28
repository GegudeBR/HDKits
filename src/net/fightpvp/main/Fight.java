package net.fightpvp.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.fightpvp.comandos.Gamemodes;
import net.fightpvp.comandos.Invsee;
import net.fightpvp.comandos.Kill;
import net.fightpvp.comandos.Kits;
import net.fightpvp.comandos.Loja;
import net.fightpvp.comandos.Repair;
import net.fightpvp.comandos.Soup;
import net.fightpvp.comandos.Spawn;
import net.fightpvp.comandos.Suicide;
import net.fightpvp.comandos.Tags;
import net.fightpvp.comandos.Warp;
import net.fightpvp.comandos.toAdmins;
import net.fightpvp.configs.ConfigManager;
import net.fightpvp.kits.Anchor;
import net.fightpvp.kits.Archer;
import net.fightpvp.kits.Checkpoint;
import net.fightpvp.kits.Copycat;
import net.fightpvp.kits.Endermage;
import net.fightpvp.kits.Fisherman;
import net.fightpvp.kits.Gladiator;
import net.fightpvp.kits.Grappler;
import net.fightpvp.kits.Hulk;
import net.fightpvp.kits.Kangaroo;
import net.fightpvp.kits.Monk;
import net.fightpvp.kits.Poseidon;
import net.fightpvp.kits.PvP;
import net.fightpvp.kits.Snail;
import net.fightpvp.kits.Specialist;
import net.fightpvp.kits.Stomper;
import net.fightpvp.kits.Switcher;
import net.fightpvp.kits.Tank;
import net.fightpvp.kits.Thor;
import net.fightpvp.kits.Timelord;
import net.fightpvp.kits.Turtle;
import net.fightpvp.kits.Urgal;
import net.fightpvp.listeners.InvListener;
import net.fightpvp.listeners.Olhar;
import net.fightpvp.listeners.PlayerListener;
import net.fightpvp.listeners.Sponge;
import net.fightpvp.managers.ColorSigns;
import net.fightpvp.managers.Kit;
import net.fightpvp.managers.KitManager;
import net.fightpvp.managers.Nearfs;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Fight extends JavaPlugin implements Listener
{
  private static Fight instance = new Fight();
  KitManager kitmg = KitManager.getKitManager();
  public HashMap<Player, ItemStack[]> save = new HashMap<Player, ItemStack[]>();

  public Economy econ = null;
  public Permission perms = null;
  public Inventory items;
  public Inventory shop;
  public Inventory kits;
  ArrayList<Arrow> arrow = new ArrayList<Arrow>();
public Object weak;

  public HashMap<Player, ItemStack[]> getSave()
  {
    return this.save;
  }

  private boolean setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = getServer()
      .getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
      return false;
    }

    getServer().getPluginManager().registerEvents(this, this);
    this.econ = ((Economy)rsp.getProvider());
    return this.econ != null;
  }

  private boolean setupPermissions() {
    RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
    this.perms = ((Permission)rsp.getProvider());
    return this.perms != null;
  }

  public void onEnable() {
    if (!setupEconomy()) {
      getLogger().severe(String.format(ChatColor.RED + "[%s] - Disabilitado por falta de Vault !", new Object[] { getDescription().getName() }));
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    getServer().getPluginManager().registerEvents(this, this);
    PluginManager pm = Bukkit.getServer().getPluginManager();
    getConfig().options().copyDefaults(true);
    saveConfig();
    this.kitmg.setupKits(this);
    Tags.setupTags();
    setupPermissions();
    ConfigManager.getConfigs().setup(this);

    pm.registerEvents(new Stomper(this), this);
    pm.registerEvents(new Olhar(this), this);
    pm.registerEvents(new Spawn(this), this);
    pm.registerEvents(new Sponge(this), this);
    pm.registerEvents(new Soup(this), this);
    pm.registerEvents(new Switcher(this), this);
    pm.registerEvents(new Kangaroo(this), this);
    pm.registerEvents(new Tank(this), this);
    pm.registerEvents(new Snail(this), this);
    pm.registerEvents(new Grappler(this), this);
    pm.registerEvents(new Turtle(this), this);
    pm.registerEvents(new Endermage(this), this);
    pm.registerEvents(new Fisherman(this), this);
    pm.registerEvents(new Poseidon(this), this);
    pm.registerEvents(new Checkpoint(this), this);
    pm.registerEvents(new Specialist(this), this);
    pm.registerEvents(new Monk(this), this);
    pm.registerEvents(new Timelord(this), this);
    pm.registerEvents(new Anchor(this), this);
    pm.registerEvents(new Hulk(this), this);
    pm.registerEvents(new InvListener(this), this);
    pm.registerEvents(new PlayerListener(this), this);
    pm.registerEvents(new Loja(this), this);
    pm.registerEvents(new Urgal(this), this);
    pm.registerEvents(new toAdmins(this), this);
    pm.registerEvents(new Thor(this), this);
    pm.registerEvents(new Tags(this), this);
    pm.registerEvents(new Gladiator(this), this);
    pm.registerEvents(new Suicide(this), this);
    pm.registerEvents(new Nearfs(this), this);
    pm.registerEvents(new ColorSigns (this), this);

    getCommand("spawn").setExecutor(new Spawn(this));
    getCommand("warp").setExecutor(new Warp(this));
    getCommand("setspawn").setExecutor(new Spawn(this));
    getCommand("setwarp").setExecutor(new Warp(this));
    getCommand("delwarp").setExecutor(new Warp(this));
    getCommand("setitem").setExecutor(new Warp(this));
    getCommand("suicide").setExecutor(new Suicide(this));
    getCommand("kits").setExecutor(new Kits(this));
    getCommand("loja").setExecutor(new Loja(this));
    getCommand("kick").setExecutor(new toAdmins(this));
    getCommand("kill").setExecutor(new Kill(this));
    getCommand("c").setExecutor(new Gamemodes(this));
    getCommand("s").setExecutor(new Gamemodes(this));
    getCommand("new").setExecutor(new toAdmins(this));
    getCommand("ban").setExecutor(new toAdmins(this));
    getCommand("pardon").setExecutor(new toAdmins(this));
    getCommand("dia").setExecutor(new toAdmins(this));
    getCommand("noite").setExecutor(new toAdmins(this));
    getCommand("admin").setExecutor(new toAdmins(this));
    getCommand("tag").setExecutor(new Tags(this));
    getCommand("sopa").setExecutor(new Soup(this));
    getCommand("invsee").setExecutor(new Invsee(this));
    getCommand("repair").setExecutor(new Repair(this));

    getCommand("pvp").setExecutor(new PvP(this));
    getCommand("timelord").setExecutor(new Timelord(this));
    getCommand("thor").setExecutor(new Thor(this));
    getCommand("archer").setExecutor(new Archer(this));
    getCommand("stomper").setExecutor(new Stomper(this));
    getCommand("switcher").setExecutor(new Switcher(this));
    getCommand("kangaroo").setExecutor(new Kangaroo(this));
    getCommand("tank").setExecutor(new Tank(this));
    getCommand("snail").setExecutor(new Snail(this));
    getCommand("grappler").setExecutor(new Grappler(this));
    getCommand("turtle").setExecutor(new Turtle(this));
    getCommand("checkpoint").setExecutor(new Checkpoint(this));
    getCommand("endermage").setExecutor(new Endermage(this));
    getCommand("fisherman").setExecutor(new Fisherman(this));
    getCommand("poseidon").setExecutor(new Poseidon(this));
    getCommand("specialist").setExecutor(new Specialist(this));
    getCommand("monk").setExecutor(new Monk(this));
    getCommand("anchor").setExecutor(new Anchor(this));
    getCommand("hulk").setExecutor(new Hulk(this));
    getCommand("copycat").setExecutor(new Copycat(this));
    getCommand("gladiator").setExecutor(new Gladiator(this));
    getCommand("urgal").setExecutor(new Urgal(this));

    if (!ConfigManager.getConfigs().getKitsConfig().contains("kits")) {
      ConfigManager.getConfigs().getKitsConfig().createSection("kits");
    }

    for (Kit k : KitManager.getKitManager().getKitList()) {
      if (!ConfigManager.getConfigs().getKitsConfig().getConfigurationSection("kits").getKeys(false).contains(k.getName())) {
        k.getName();

        ConfigManager.getConfigs().getKitsConfig();

        List<String> arraysa = new ArrayList<String>();
        arraysa.add("Adicione na plugin.yml");


        }
      }
    }

  public void onDisable()
  {
  }

  public static Fight getPlugin() {
    return instance;
  }
  @EventHandler
  public void Break(BlockBreakEvent e) {
    Player p = e.getPlayer();
    if ((!p.hasPermission("fight.break")) && (p.getGameMode() != GameMode.CREATIVE))
      e.setCancelled(true);
  }

  @EventHandler
  public void onPlayerSopa(PlayerInteractEvent e) {
	Player p = e.getPlayer();
	Material mat = p.getItemInHand().getType();
	ItemStack pote = new ItemStack(Material.BOWL, 1);
	ItemMeta meta = pote.getItemMeta();
	meta.setDisplayName("§7Tigela");
	pote.setItemMeta(meta);		
	if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		if(mat == Material.MUSHROOM_SOUP) {
			double health = p.getHealth();
			if(health == 20){
				int food = p.getFoodLevel();
				if(food == 20){
					return;
				}
				int nfood = food + 7;
				if(nfood > 20) {
					p.setFoodLevel(20);
				} else {
					p.setFoodLevel(nfood);
				}
				e.setCancelled(true);
				p.getInventory().setItem(p.getInventory().getHeldItemSlot(), pote);
				return;
			} else {
				double nhealth = health + 7;
				if(nhealth > 20) {
					p.setHealth(20);
				} else {
					p.setHealth(nhealth);
				}
				e.setCancelled(true);
				p.getInventory().setItem(p.getInventory().getHeldItemSlot(), pote);
				return;
			}
		}
	}
  }

  @EventHandler
  public void FoodCancel(FoodLevelChangeEvent e) {
    e.setCancelled(true);
  }
  @EventHandler
  public void onEntityExplode(EntityExplodeEvent e) {
          for (Block b : e.blockList()) {
                  final BlockState state = b.getState();            
                 
                  int delay = 20;
                 
                  if ((b.getType() == Material.SAND) || (b.getType() == Material.GRAVEL)) {
                          delay += 1;
                  }
                 
                  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                          public void run() {
                                  state.update(true, false);
                          }
                  }, delay);
          }
  }
  
  @EventHandler
  public void Death(PlayerDeathEvent e) {
    if (((e.getEntity() instanceof Player)) && ((e.getEntity().getKiller() instanceof Player))) {
      Player p = e.getEntity();
      Player c = p.getKiller();
      if (!this.kitmg.hasAbility(c, "Copycat")) {
        Kit k = this.kitmg.getPlayerKit(p);
        k.removePlayer(p);
      } else {
        Kit k = this.kitmg.getPlayerKit(p);
        if (this.kitmg.hasKit(k.getName())) {
          c.sendMessage("§f§kkk §6§oAgora voce esta com o kit : §f§l" + k.getName() + " §f§kkk");
          k.addPlayer(c);
          this.kitmg.giveKit(c, k);
          for (int i = 0; i < 35; i++) {
            c.getInventory().addItem(new ItemStack[] { new ItemStack(Material.MUSHROOM_SOUP) });
          }
        }
        k.removePlayer(p);
      }
    }
  }

  @EventHandler
	public void onPlayerDropSword(PlayerDropItemEvent e){
		if(e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD || e.getItemDrop().getItemStack().getType() == Material.IRON_SWORD || e.getItemDrop().getItemStack().getType() == Material.GOLD_SWORD || e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD || e.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD ||  e.getItemDrop().getItemStack().getType() == Material.BOW ||
				e.getItemDrop().getItemStack().getType() == Material.ARROW || e.getItemDrop().getItemStack().getType() == Material.FIREWORK || e.getItemDrop().getItemStack().getType() == Material.FEATHER || e.getItemDrop().getItemStack().getType() == Material.REDSTONE_TORCH_ON || e.getItemDrop().getItemStack().getType() == Material.PORTAL || e.getItemDrop().getItemStack().getType() == Material.FISHING_ROD ||
				e.getItemDrop().getItemStack().getType() == Material.WOOD_AXE || e.getItemDrop().getItemStack().getType() == Material.SNOW_BALL || e.getItemDrop().getItemStack().getType() == Material.INK_SACK || e.getItemDrop().getItemStack().getType() == Material.SADDLE || e.getItemDrop().getItemStack().getType() == Material.ENDER_PEARL || e.getItemDrop().getItemStack().getType() == Material.BLAZE_ROD ||
				e.getItemDrop().getItemStack().getType() == Material.SUGAR || e.getItemDrop().getItemStack().getType() == Material.REDSTONE || e.getItemDrop().getItemStack().getType() == Material.IRON_FENCE || e.getItemDrop().getItemStack().getType() == Material.WATCH || e.getItemDrop().getItemStack().getType() == Material.LEASH || e.getItemDrop().getItemStack().getType() == Material.CHEST ||
				e.getItemDrop().getItemStack().getType() == Material.BOOK ||e.getItemDrop().getItemStack().getType() == Material.NETHER_FENCE || e.getItemDrop().getItemStack().getType() == Material.FLOWER_POT_ITEM) {
			e.setCancelled(true);
		}
	}

  @EventHandler
  public void Join(PlayerJoinEvent e)
  {
    Player p = e.getPlayer();
    String a = "--------------------------------";
    StringBuilder bs = new StringBuilder();
    for (int i = 0; i < 100; i++) {
      p.sendMessage(" ");
    }

    for (int i = 0; i < a.length() / 2; i++) {
      bs.append("§f-§a-");
    }

    p.sendMessage(bs.toString().trim());
    String msg = "§f" + p.getName() + 
      "\n \n§aInformamos q os vips serao devolvidos " + 
      "\nate amanha aguarde !    \n" + 
      "                                      §f- Staff !";
    p.sendMessage(msg);
    p.sendMessage(bs.toString().trim());
    e.setJoinMessage("§8[§a+§8] §f" + p.getName());
  }
  @EventHandler
  public void Pickup(PlayerPickupItemEvent e) {
    Item i = e.getItem();
    if (i.getItemStack().getType() != Material.MUSHROOM_SOUP)
      e.setCancelled(true);
  }

  @EventHandler
  public void DroparPotes(ItemSpawnEvent e) {
    if ((e.getEntity() instanceof Item)) {
      final Item dropped = e.getEntity();
      Vector velocity = dropped.getLocation().getDirection().multiply(0.25D).setY(0.8D);
      dropped.setVelocity(velocity);
      Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
        public void run() {
          dropped.remove();
          dropped.getWorld().playEffect(dropped.getLocation(), Effect.SMOKE, 10);
          dropped.getWorld().playSound(dropped.getLocation(), Sound.CHICKEN_EGG_POP, 10.0F, 0.0F);
        }
      }
      , 20L);
    }
  }

  public static void spawnRandomFirework(Location loc) {
    Firework fw = (Firework)loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
    FireworkMeta fwm = fw.getFireworkMeta();
    Random r = new Random();

    int rt = r.nextInt(4) + 1;
    FireworkEffect.Type type = FireworkEffect.Type.BALL;
    if (rt == 1) type = FireworkEffect.Type.BALL;
    if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
    if (rt == 3) type = FireworkEffect.Type.BURST;
    if (rt == 4) type = FireworkEffect.Type.CREEPER;
    if (rt == 5) type = FireworkEffect.Type.STAR;

    Color c1 = Color.RED;
    Color c2 = Color.YELLOW;
    Color c3 = Color.ORANGE;

    FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withColor(c2).withFade(c3).with(type).trail(r.nextBoolean()).build();
    fwm.addEffect(effect);

    int rp = r.nextInt(2) + 1;
    fwm.setPower(rp);

    fw.setFireworkMeta(fwm);
  }
  @EventHandler
  public void hit(ProjectileHitEvent e) {
    if ((e.getEntity() instanceof Arrow)) {
      Arrow a = (Arrow)e.getEntity();
      if (this.arrow.contains(a)) {
        spawnRandomFirework(a.getLocation());
        this.arrow.remove(a);
      }
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent ev) {
    Player p = ev.getPlayer();
    if (ev.getAction() != Action.RIGHT_CLICK_AIR) return;
    if (ev.getItem().getType() != Material.GOLD_HOE) return;
    Vector velo1 = p.getLocation().getDirection().normalize().multiply(2);
    Vector velo2 = p.getLocation().getDirection().normalize().multiply(2);
    Vector velo3 = p.getLocation().getDirection().normalize().multiply(2);
    Vector velo4 = p.getLocation().getDirection().normalize().multiply(2);
    Vector velo5 = p.getLocation().getDirection().normalize().multiply(2);
    Vector velo6 = p.getLocation().getDirection().normalize().multiply(2);
    Vector velo7 = p.getLocation().getDirection().normalize().multiply(2);
    Vector velo8 = p.getLocation().getDirection().normalize().multiply(2);
    Vector velo9 = p.getLocation().getDirection().normalize().multiply(2);
    velo1.add(new Vector(Math.random() * 0.25D - 0.0D, -0.25D, 0.0D));
    velo2.add(new Vector(Math.random() * 0.25D - 0.0D, 0.25D, 0.0D));
    velo3.add(new Vector(Math.random() * 0.25D - 0.0D, 0.0D, 0.0D));
    velo4.add(new Vector(Math.random() * 0.25D - 0.0D, 0.0D, -0.25D));
    velo5.add(new Vector(Math.random() * 0.25D - 0.0D, 0.0D, 0.25D));
    velo6.add(new Vector(Math.random() * 0.25D - 0.0D, 0.25D, 0.25D));
    velo7.add(new Vector(Math.random() * 0.25D - 0.0D, 0.25D, -0.25D));
    velo8.add(new Vector(Math.random() * 0.25D - 0.0D, -0.25D, -0.25D));
    velo9.add(new Vector(Math.random() * 0.25D - 0.0D, -0.25D, 0.25D));
    p.playSound(p.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
    p.getEyeLocation().getWorld().playEffect(p.getLocation(), Effect.SMOKE, 5);
    p.getLocation().getWorld().playEffect(p.getLocation(), Effect.GHAST_SHOOT, 1);
    p.getLocation().getWorld().playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 1);
    Arrow a = (Arrow)p.launchProjectile(Arrow.class);
    Arrow b = (Arrow)p.launchProjectile(Arrow.class);
    Arrow c = (Arrow)p.launchProjectile(Arrow.class);
    Arrow d = (Arrow)p.launchProjectile(Arrow.class);
    Arrow e = (Arrow)p.launchProjectile(Arrow.class);
    Arrow f = (Arrow)p.launchProjectile(Arrow.class);
    Arrow g = (Arrow)p.launchProjectile(Arrow.class);
    Arrow h = (Arrow)p.launchProjectile(Arrow.class);
    a.setVelocity(velo2);
    b.setVelocity(velo3);
    c.setVelocity(velo4);
    d.setVelocity(velo5);
    e.setVelocity(velo6);
    f.setVelocity(velo7);
    g.setVelocity(velo8);
    h.setVelocity(velo9);
    a.setFireTicks(2147483647);
    b.setFireTicks(2147483647);
    c.setFireTicks(2147483647);
    d.setFireTicks(2147483647);
    e.setFireTicks(2147483647);
    f.setFireTicks(2147483647);
    g.setFireTicks(2147483647);
    h.setFireTicks(2147483647);
    this.arrow.add(a);
    this.arrow.add(b);
    this.arrow.add(c);
    this.arrow.add(d);
    this.arrow.add(e);
    this.arrow.add(f);
    this.arrow.add(g);
    this.arrow.add(h);
    p.setExp(0.0F);
    p.setLevel(0);
  }
}