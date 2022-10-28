package us.mcmagic.mcmagiccore.itemcreator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 1/18/15
 */
public class ItemCreator extends ItemStack {

    public ItemCreator(Material type) {
        this(type, 1);
    }

    public ItemCreator(Material type, int amount) {
        this(type, amount, (byte) 0);
    }

    public ItemCreator(Material type, int amount, byte data) {
        setType(type);
        setAmount(amount);
        setDurability((short) data);
    }

    public ItemCreator(Material type, String name) {
        this(type, name, new ArrayList<String>());
    }

    public ItemCreator(Material type, String name, List<String> lore) {
        this(type, 1, name, lore);
    }

    public ItemCreator(Material type, int amount, String name, List<String> lore) {
        this(type, amount, (byte) 0, name, lore);
    }

    public ItemCreator(Material type, int amount, byte data, String name, List<String> lore) {
        setType(type);
        setAmount(amount);
        setDurability((short) data);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(type);
        meta.setDisplayName(name);
        meta.setLore(lore);
        setItemMeta(meta);
    }

    public ItemCreator(String owner) {
        this(owner, owner, new ArrayList<String>());
    }

    public ItemCreator(String owner, String displayName) {
        this(owner, displayName, new ArrayList<String>());
    }

    public ItemCreator(String owner, String displayName, List<String> lore) {
        setType(Material.SKULL_ITEM);
        setAmount(1);
        setDurability((short) 3);
        SkullMeta sm = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
        sm.setOwner(owner);
        sm.setDisplayName(displayName);
        sm.setLore(lore);
        setItemMeta(sm);
    }
}