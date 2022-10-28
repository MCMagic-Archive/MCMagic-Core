package us.mcmagic.mcmagiccore.arcade.ctf;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import us.mcmagic.mcmagiccore.itemcreator.ItemCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 8/21/15
 */
public enum CTFKit {
    HADES(ChatColor.RED + "Hades"), BAYMAX(ChatColor.AQUA + "Baymax"), HERCULES(ChatColor.GOLD + "Hercules"),
    MERIDA(ChatColor.DARK_GREEN + "Merida"), BOLT(ChatColor.YELLOW + "Bolt");

    private final String name;

    CTFKit(String name) {
        this.name = name;
    }

    public static void initialize() {
        /**
         Special Abilities:
         Hades - Flame Burst
         Baymax - Healing
         Hercules - Damage Multiplier
         Merida - Arrow Spray
         Bolt - Super Bark
         */
    }

    public static CTFKit fromString(String msg) {
        String kname = msg.toLowerCase();
        switch (kname) {
            case "hades":
                return HADES;
            case "baymax":
                return BAYMAX;
            case "hercules":
                return HERCULES;
            case "merida":
                return MERIDA;
            case "bolt":
                return BOLT;
            default:
                return null;
        }
    }

    public String getName() {
        return name;
    }

    public List<ItemStack> getItems(int level) {
        List<ItemStack> list = new ArrayList<>();
        switch (this) {
            case HADES:
                switch (level) {
                    case 1:
                        list.add(new ItemCreator(Material.CHAINMAIL_CHESTPLATE, format("Hades Chestplate")));
                        list.add(new ItemCreator(Material.STONE_SWORD, format("Hades Sword")));
                        list.add(enchant(new ItemCreator(Material.STONE_PICKAXE, format("Hades Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.BREAD, 2));
                        break;
                    case 2:
                        list.add(enchant(new ItemCreator(Material.CHAINMAIL_CHESTPLATE, format("Hades Chestplate")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.STONE_SWORD, format("Hades Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(new ItemCreator(Material.STONE_PICKAXE, format("Hades Pickaxe")));
                        list.add(new ItemCreator(Material.BREAD, 3));
                        break;
                    case 3:
                        list.add(new ItemCreator(Material.IRON_CHESTPLATE, format("Hades Chestplate")));
                        list.add(enchant(new ItemCreator(Material.STONE_SWORD, format("Hades Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Hades Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.BREAD, 3));
                        break;
                    case 4:
                        list.add(enchant(new ItemCreator(Material.IRON_CHESTPLATE, format("Hades Chestplate")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(new ItemCreator(Material.IRON_SWORD, format("Hades Sword")));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Hades Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(new ItemCreator(Material.BREAD, 4));
                        break;
                    case 5:
                        list.add(enchant(new ItemCreator(Material.IRON_CHESTPLATE, format("Hades Chestplate")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_SWORD, format("Hades Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Hades Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(new ItemCreator(Material.BREAD, 4));
                        break;
                    case 6:
                        list.add(new ItemCreator(Material.DIAMOND_CHESTPLATE, format("Hades Chestplate")));
                        list.add(new ItemCreator(Material.DIAMOND_SWORD, format("Hades Sword")));
                        list.add(new ItemCreator(Material.DIAMOND_PICKAXE, format("Hades Pickaxe")));
                        list.add(new ItemCreator(Material.BREAD, 5));
                        break;
                    case 7:
                        list.add(enchant(new ItemCreator(Material.DIAMOND_CHESTPLATE, format("Hades Chestplate")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_SWORD, format("Hades Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_PICKAXE, format("Hades Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.BREAD, 5));
                        break;
                    default:
                        break;
                }
                break;
            case BAYMAX:
                switch (level) {
                    case 1:
                        list.add(new ItemCreator(Material.STONE_SWORD, format("Baymax Sword")));
                        list.add(enchant(new ItemCreator(Material.STONE_PICKAXE, format("Baymax Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(baymaxPotion(2));
                        list.add(new ItemCreator(Material.BREAD, 2));
                        break;
                    case 2:
                        list.add(enchant(new ItemCreator(Material.STONE_SWORD, format("Baymax Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(new ItemCreator(Material.IRON_PICKAXE, format("Baymax Pickaxe")));
                        list.add(baymaxPotion(3));
                        list.add(new ItemCreator(Material.BREAD, 3));
                        break;
                    case 3:
                        list.add(enchant(new ItemCreator(Material.STONE_SWORD, format("Baymax Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Baymax Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(baymaxPotion(4));
                        list.add(new ItemCreator(Material.BREAD, 3));
                        break;
                    case 4:
                        list.add(new ItemCreator(Material.IRON_SWORD, format("Baymax Sword")));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Baymax Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(baymaxPotion(5));
                        list.add(new ItemCreator(Material.BREAD, 4));
                        break;
                    case 5:
                        list.add(new ItemCreator(Material.CHAINMAIL_CHESTPLATE, format("Baymax Chestplate")));
                        list.add(enchant(new ItemCreator(Material.IRON_SWORD, format("Baymax Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Baymax Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(baymaxPotion(6));
                        list.add(new ItemCreator(Material.BREAD, 4));
                        break;
                    case 6:
                        list.add(enchant(new ItemCreator(Material.CHAINMAIL_CHESTPLATE, format("Baymax Chestplate")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(new ItemCreator(Material.DIAMOND_SWORD, format("Baymax Sword")));
                        list.add(new ItemCreator(Material.DIAMOND_PICKAXE, format("Baymax Pickaxe")));
                        list.add(baymaxPotion(7));
                        list.add(new ItemCreator(Material.BREAD, 5));
                        break;
                    case 7:
                        list.add(new ItemCreator(Material.IRON_CHESTPLATE, format("Baymax Chestplate")));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_SWORD, format("Baymax Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_PICKAXE, format("Baymax Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(baymaxPotion(8));
                        list.add(new ItemCreator(Material.BREAD, 5));
                        break;
                    default:
                        break;
                }
                break;
            case HERCULES:
                switch (level) {
                    case 1:
                        list.add(new ItemCreator(Material.CHAINMAIL_LEGGINGS, format("Hercules Leggings")));
                        list.add(new ItemCreator(Material.STONE_AXE, format("Hercules Axe")));
                        list.add(enchant(new ItemCreator(Material.STONE_PICKAXE, format("Hercules Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 2));
                        break;
                    case 2:
                        list.add(enchant(new ItemCreator(Material.CHAINMAIL_LEGGINGS, format("Hercules Leggings")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(new ItemCreator(Material.STONE_AXE, format("Hercules Axe")));
                        list.add(new ItemCreator(Material.IRON_PICKAXE, format("Hercules Pickaxe")));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 3));
                        break;
                    case 3:
                        list.add(new ItemCreator(Material.IRON_LEGGINGS, format("Hercules Leggings")));
                        list.add(new ItemCreator(Material.IRON_AXE, format("Hercules Axe")));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Hercules Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 4));
                        break;
                    case 4:
                        list.add(enchant(new ItemCreator(Material.IRON_LEGGINGS, format("Hercules Leggings")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_AXE, format("Hercules Axe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Hercules Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 4));
                        break;
                    case 5:
                        list.add(enchant(new ItemCreator(Material.IRON_LEGGINGS, format("Hercules Leggings")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_AXE, format("Hercules Axe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Hercules Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 4));
                        break;
                    case 6:
                        list.add(new ItemCreator(Material.DIAMOND_LEGGINGS, format("Hercules Leggings")));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_SWORD, format("Hercules Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(new ItemCreator(Material.DIAMOND_PICKAXE, format("Hercules Pickaxe")));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 5));
                        break;
                    case 7:
                        list.add(enchant(new ItemCreator(Material.DIAMOND_LEGGINGS, format("Hercules Leggings")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(new ItemCreator(Material.IRON_SWORD, format("Hercules Sword")));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_PICKAXE, format("Hercules Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 5));
                        break;
                    default:
                        break;
                }
                break;
            case MERIDA:
                switch (level) {
                    case 1:
                        list.add(new ItemCreator(Material.BOW, format("Merida Bow")));
                        list.add(new ItemCreator(Material.ARROW, 30));
                        list.add(enchant(new ItemCreator(Material.STONE_PICKAXE, format("Merida Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.BREAD, 2));
                        break;
                    case 2:
                        list.add(enchant(new ItemCreator(Material.BOW, format("Merida Bow")),
                                new Enchant(Enchantment.ARROW_DAMAGE, 1)));
                        list.add(new ItemCreator(Material.ARROW, 35));
                        list.add(new ItemCreator(Material.IRON_PICKAXE, format("Merida Pickaxe")));
                        list.add(new ItemCreator(Material.BREAD, 3));
                        break;
                    case 3:
                        list.add(enchant(new ItemCreator(Material.BOW, format("Merida Bow")),
                                new Enchant(Enchantment.ARROW_DAMAGE, 1)));
                        list.add(new ItemCreator(Material.ARROW, 40));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Merida Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.BREAD, 3));
                        break;
                    case 4:
                        list.add(enchant(new ItemCreator(Material.BOW, format("Merida Bow")),
                                new Enchant(Enchantment.ARROW_DAMAGE, 2)));
                        list.add(new ItemCreator(Material.ARROW, 45));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Merida Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(new ItemCreator(Material.BREAD, 4));
                        break;
                    case 5:
                        list.add(new ItemCreator(Material.CHAINMAIL_CHESTPLATE, format("Merida Chestplate")));
                        list.add(enchant(new ItemCreator(Material.BOW, format("Merida Bow")),
                                new Enchant(Enchantment.ARROW_DAMAGE, 2),
                                new Enchant(Enchantment.ARROW_KNOCKBACK, 1)));
                        list.add(new ItemCreator(Material.ARROW, 50));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Merida Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(new ItemCreator(Material.BREAD, 4));
                        break;
                    case 6:
                        list.add(enchant(new ItemCreator(Material.CHAINMAIL_CHESTPLATE, format("Merida Chestplate")),
                                new Enchant(Enchantment.PROTECTION_PROJECTILE, 1)));
                        list.add(enchant(new ItemCreator(Material.BOW, format("Merida Bow")),
                                new Enchant(Enchantment.ARROW_DAMAGE, 3),
                                new Enchant(Enchantment.ARROW_KNOCKBACK, 1)));
                        list.add(new ItemCreator(Material.ARROW, 55));
                        list.add(new ItemCreator(Material.DIAMOND_PICKAXE, format("Merida Pickaxe")));
                        list.add(new ItemCreator(Material.BREAD, 5));
                        break;
                    case 7:
                        list.add(enchant(new ItemCreator(Material.CHAINMAIL_CHESTPLATE, format("Merida Chestplate")),
                                new Enchant(Enchantment.PROTECTION_PROJECTILE, 1)));
                        list.add(enchant(new ItemCreator(Material.BOW, format("Merida Bow")),
                                new Enchant(Enchantment.ARROW_DAMAGE, 3),
                                new Enchant(Enchantment.ARROW_KNOCKBACK, 2)));
                        list.add(new ItemCreator(Material.ARROW, 64));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_PICKAXE, format("Merida Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.BREAD, 5));
                        break;
                    default:
                        break;
                }
                break;
            case BOLT:
                switch (level) {
                    case 1:
                        list.add(new ItemCreator(Material.CHAINMAIL_HELMET, format("Bolt Helmet")));
                        list.add(enchant(new ItemCreator(Material.CHAINMAIL_BOOTS, format("Bolt Boots")),
                                new Enchant(Enchantment.PROTECTION_FALL, 1)));
                        list.add(new ItemCreator(Material.STONE_SWORD, format("Bolt Sword")));
                        list.add(enchant(new ItemCreator(Material.STONE_PICKAXE, format("Bolt Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 2));
                        break;
                    case 2:
                        list.add(enchant(new ItemCreator(Material.CHAINMAIL_HELMET, format("Bolt Helmet")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.CHAINMAIL_BOOTS, format("Bolt Boots")),
                                new Enchant(Enchantment.PROTECTION_FALL, 1)));
                        list.add(enchant(new ItemCreator(Material.STONE_SWORD, format("Bolt Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(new ItemCreator(Material.IRON_PICKAXE, format("Bolt Pickaxe")));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 3));
                        break;
                    case 3:
                        list.add(new ItemCreator(Material.IRON_HELMET, format("Bolt Helmet")));
                        list.add(enchant(new ItemCreator(Material.IRON_BOOTS, format("Bolt Boots")),
                                new Enchant(Enchantment.PROTECTION_FALL, 2)));
                        list.add(enchant(new ItemCreator(Material.STONE_SWORD, format("Bolt Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Bolt Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 3));
                        break;
                    case 4:
                        list.add(enchant(new ItemCreator(Material.IRON_HELMET, format("Bolt Helmet")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_BOOTS, format("Bolt Boots")),
                                new Enchant(Enchantment.PROTECTION_FALL, 2)));
                        list.add(new ItemCreator(Material.IRON_SWORD, format("Bolt Sword")));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Bolt Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 4));
                        break;
                    case 5:
                        list.add(enchant(new ItemCreator(Material.IRON_HELMET, format("Bolt Helmet")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_BOOTS, format("Bolt Boots")),
                                new Enchant(Enchantment.PROTECTION_FALL, 3)));
                        list.add(enchant(new ItemCreator(Material.IRON_SWORD, format("Bolt Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(enchant(new ItemCreator(Material.IRON_PICKAXE, format("Bolt Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 2)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 4));
                        break;
                    case 6:
                        list.add(new ItemCreator(Material.DIAMOND_HELMET, format("Bolt Helmet")));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_BOOTS, format("Bolt Boots")),
                                new Enchant(Enchantment.PROTECTION_FALL, 3)));
                        list.add(new ItemCreator(Material.DIAMOND_SWORD, format("Bolt Sword")));
                        list.add(new ItemCreator(Material.DIAMOND_PICKAXE, format("Bolt Pickaxe")));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 5));
                        break;
                    case 7:
                        list.add(enchant(new ItemCreator(Material.DIAMOND_HELMET, format("Bolt Helmet")),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_BOOTS, format("Bolt Boots")),
                                new Enchant(Enchantment.PROTECTION_FALL, 3),
                                new Enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_SWORD, format("Bolt Sword")),
                                new Enchant(Enchantment.DAMAGE_ALL, 1)));
                        list.add(enchant(new ItemCreator(Material.DIAMOND_PICKAXE, format("Bolt Pickaxe")),
                                new Enchant(Enchantment.DIG_SPEED, 1)));
                        list.add(new ItemCreator(Material.COOKED_BEEF, 5));
                        break;
                    default:
                        break;
                }
                break;
        }
        return list;
    }

    private ItemStack baymaxPotion(int amount) {
        return new ItemCreator(Material.POTION, amount, (byte) 16453, format("Baymax Potion"), new ArrayList<String>());
    }

    private String format(String s) {
        return ChatColor.AQUA + s;
    }

    private ItemStack enchant(ItemStack item, Enchant... list) {
        for (Enchant e : list) {
            item.addEnchantment(e.getEnchantment(), e.getLevel());
        }
        return item;
    }

    public void setItems(Player player, int level) {
        PlayerInventory inv = player.getInventory();
        for (ItemStack i : getItems(level)) {
            if (isArmor(i)) {
                String n = i.getType().name().toLowerCase();
                if (n.contains("helmet")) {
                    inv.setHelmet(i);
                    continue;
                } else if (n.contains("chestplate")) {
                    inv.setChestplate(i);
                    continue;
                } else if (n.contains("leggings")) {
                    inv.setLeggings(i);
                    continue;
                } else if (n.contains("boot")) {
                    inv.setBoots(i);
                    continue;
                }
            }
            inv.addItem(i);
        }
    }

    private boolean isArmor(ItemStack i) {
        String n = i.getType().name().toLowerCase();
        return n.contains("helmet") || n.contains("chestplate") || n.contains("leggings") || n.contains("boot");
    }

    private class Enchant {
        private final Enchantment e;
        private final int level;

        public Enchant(Enchantment e, int level) {
            this.e = e;
            this.level = level;
        }

        public Enchantment getEnchantment() {
            return e;
        }

        public int getLevel() {
            return level;
        }
    }
}