package net.fightpvp.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Tag
{
  private String name;
  List<ChatColor> colors = new ArrayList<ChatColor>();
  HashSet<Player> players = new HashSet<Player>();

  public Tag(String nome, ChatColor cor) {
    this.name = nome;
    this.colors.add(cor);
  }

  public List<ChatColor> colors() {
    return this.colors;
  }

  public Tag(String nome, ChatColor cor, ChatColor cor2) {
    this.name = nome;
    this.colors.add(cor);
    this.colors.add(cor2);
  }

  public ChatColor getColor(int index)
  {
    return (ChatColor)this.colors.get(index);
  }

  public void setColor(int index, ChatColor c) {
    this.colors.clear();
    this.colors.add(c);
  }

  public void setColor(int index, ChatColor c, ChatColor c2) {
    this.colors.clear();
    this.colors.add(c);
    this.colors.add(c2);
  }

  public String getName() {
    return this.name;
  }

  public HashSet<Player> getPlayers() {
    return this.players;
  }

  public void addPlayer(Player p) {
    if (!this.players.contains(p)) this.players.add(p); 
  }

  public void removePlayer(Player p)
  {
    if (this.players.contains(p)) this.players.remove(p); 
  }

  public boolean containsPlayer(Player p)
  {
    return this.players.contains(p);
  }
}