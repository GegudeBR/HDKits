package net.fightpvp.managers;

import java.util.ArrayList;
import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit
{
  private String name;
  private Material mat;
  private HashSet<Player> players = new HashSet();
  private ArrayList<ItemStack> items = new ArrayList();

  public Kit(String name) {
    this.name = name;
    ArrayList i = new ArrayList();
    this.items = i;
  }

  public String getName() {
    return this.name;
  }

  public Material getMaterial() {
    return this.mat;
  }

  public void setMaterial(Material m) {
    this.mat = m;
  }

  public void removePlayer(Player p) {
    this.players.remove(p);
  }

  public void addPlayer(Player p) {
    this.players.add(p);
  }

  public HashSet<Player> getPlayers() {
    return this.players;
  }

  public void setItems(ArrayList<ItemStack> i) {
    this.items = i;
  }

  public ArrayList<ItemStack> getItems() {
    return this.items;
  }
}