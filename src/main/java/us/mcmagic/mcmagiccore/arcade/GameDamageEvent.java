package us.mcmagic.mcmagiccore.arcade;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by Marc on 9/14/15
 */
public class GameDamageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private double damage;
    private boolean cancelled = false;

    public GameDamageEvent(Player player, double damage) {
        super();
        this.player = player;
        this.damage = damage;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDamageReduced() {
        PlayerInventory inv = player.getInventory();
        ItemStack boots = inv.getBoots();
        ItemStack helmet = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack pants = inv.getLeggings();
        double red = 0.0;
        if (helmet != null) {
            if (helmet.getType() == Material.LEATHER_HELMET) {
                red = red + 0.04;
            } else if (helmet.getType() == Material.GOLD_HELMET) {
                red = red + 0.08;
            } else if (helmet.getType() == Material.CHAINMAIL_HELMET) {
                red = red + 0.08;
            } else if (helmet.getType() == Material.IRON_HELMET) {
                red = red + 0.08;
            } else if (helmet.getType() == Material.DIAMOND_HELMET) {
                red = red + 0.12;
            }
        }
        if (boots != null) {
            if (boots.getType() == Material.LEATHER_BOOTS) {
                red = red + 0.04;
            } else if (boots.getType() == Material.GOLD_BOOTS) {
                red = red + 0.04;
            } else if (boots.getType() == Material.CHAINMAIL_BOOTS) {
                red = red + 0.04;
            } else if (boots.getType() == Material.IRON_BOOTS) {
                red = red + 0.08;
            } else if (boots.getType() == Material.DIAMOND_BOOTS) {
                red = red + 0.12;
            }
        }
        if (pants != null) {
            if (pants.getType() == Material.LEATHER_LEGGINGS) {
                red = red + 0.08;
            } else if (pants.getType() == Material.GOLD_LEGGINGS) {
                red = red + 0.12;
            } else if (pants.getType() == Material.CHAINMAIL_LEGGINGS) {
                red = red + 0.16;
            } else if (pants.getType() == Material.IRON_LEGGINGS) {
                red = red + 0.20;
            } else if (pants.getType() == Material.DIAMOND_LEGGINGS) {
                red = red + 0.24;
            }
        }
        if (chest != null) {
            if (chest.getType() == Material.LEATHER_CHESTPLATE) {
                red = red + 0.12;
            } else if (chest.getType() == Material.GOLD_CHESTPLATE) {
                red = red + 0.20;
            } else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE) {
                red = red + 0.20;
            } else if (chest.getType() == Material.IRON_CHESTPLATE) {
                red = red + 0.24;
            } else if (chest.getType() == Material.DIAMOND_CHESTPLATE) {
                red = red + 0.32;
            }
        }
        return damage - red;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}