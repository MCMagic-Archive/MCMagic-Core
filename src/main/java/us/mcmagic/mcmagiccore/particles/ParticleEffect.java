package us.mcmagic.mcmagiccore.particles;

import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public enum ParticleEffect {
    /**
     * A particle effect which is displayed by exploding tnt and creepers:
     * <ul>
     * <li>It looks like a crowd of gray balls which are fading away
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    HUGE_EXPLOSION("hugeexplosion"),
    /**
     * A particle effect which is displayed by exploding ghast fireballs and
     * wither skulls:
     * <ul>
     * <li>It looks like a gray ball which is fading away
     * <li>The speed value slightly influences the size of this particle effect
     * </ul>
     */
    LARGE_EXPLODE("largeexplode"),
    /**
     * A particle effect which is displayed by launching fireworks:
     * <ul>
     * <li>It looks like a white star which is sparkling
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    FIREWORKS_SPARK("fireworksSpark"),
    /**
     * A particle effect which is displayed by swimming entities and arrows in
     * water:
     * <ul>
     * <li>It looks like a bubble
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    BUBBLE("bubble", true),
    /**
     * A particle effect which is displayed by water:
     * <ul>
     * <li>It looks like a tiny blue square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SUSPEND("suspend", true),
    /**
     * A particle effect which is displayed by air when close to bedrock and the
     * in the void:
     * <ul>
     * <li>It looks like a tiny gray square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    DEPTH_SUSPEND("depthSuspend"),
    /**
     * A particle effect which is displayed by mycelium:
     * <ul>
     * <li>It looks like a tiny gray square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    TOWN_AURA("townaura"),
    /**
     * A particle effect which is displayed when landing a critical hit and by
     * arrows:
     * <ul>
     * <li>It looks like a light brown cross
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    CRIT("crit"),
    /**
     * A particle effect which is displayed when landing a hit with an enchanted
     * weapon:
     * <ul>
     * <li>It looks like a cyan star
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    MAGIC_CRIT("magicCrit"),
    /**
     * A particle effect which is displayed by primed tnt, torches, droppers,
     * dispensers, end portals, brewing stands and monster spawners:
     * <ul>
     * <li>It looks like a little gray cloud
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    SMOKE("smoke"),
    /**
     * A particle effect which is displayed by entities with active potion
     * effects:
     * <ul>
     * <li>It looks like a colored swirl
     * <li>The speed value causes the particle to be colored black when set to 0
     * </ul>
     */
    MOB_SPELL("mobSpell"),
    /**
     * A particle effect which is displayed by entities with active potion
     * effects applied through a beacon:
     * <ul>
     * <li>It looks like a transparent colored swirl
     * <li>The speed value causes the particle to be always colored black when
     * set to 0
     * </ul>
     */
    MOB_SPELL_AMBIENT("mobSpellAmbient"),
    /**
     * A particle effect which is displayed when splash potions or bottles o'
     * enchanting hit something:
     * <ul>
     * <li>It looks like a white swirl
     * <li>The speed value causes the particle to only move upwards when set to
     * 0
     * </ul>
     */
    SPELL("spell"),
    /**
     * A particle effect which is displayed when instant splash potions hit
     * something:
     * <ul>
     * <li>It looks like a white cross
     * <li>The speed value causes the particle to only move upwards when set to
     * 0
     * </ul>
     */
    INSTANT_SPELL("instantSpell"),
    /**
     * A particle effect which is displayed by witches:
     * <ul>
     * <li>It looks like a purple cross
     * <li>The speed value causes the particle to only move upwards when set to
     * 0
     * </ul>
     */
    WITCH_MAGIC("witchMagic"),
    /**
     * A particle effect which is displayed by note blocks:
     * <ul>
     * <li>It looks like a colored note
     * <li>The speed value causes the particle to be colored green when set to 0
     * </ul>
     */
    NOTE("note"),
    /**
     * A particle effect which is displayed by nether portals, endermen, ender
     * pearls, eyes of ender, ender chests and dragon eggs:
     * <ul>
     * <li>It looks like a purple cloud
     * <li>The speed value influences the spread of this particle effect
     * </ul>
     */
    PORTAL("portal"),
    /**
     * A particle effect which is displayed by enchantment tables which are
     * nearby bookshelves:
     * <ul>
     * <li>It looks like a cryptic white letter
     * <li>The speed value influences the spread of this particle effect
     * </ul>
     */
    ENCHANTMENT_TABLE("enchantmenttable"),
    /**
     * A particle effect which is displayed by exploding tnt and creepers:
     * <ul>
     * <li>It looks like a white cloud
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    EXPLODE("explode"),
    /**
     * A particle effect which is displayed by torches, active furnaces, magma
     * cubes and monster spawners:
     * <ul>
     * <li>It looks like a tiny flame
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    FLAME("flame"),
    /**
     * A particle effect which is displayed by lava:
     * <ul>
     * <li>It looks like a spark
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    LAVA("lava"),
    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It looks like a transparent gray square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    FOOTSTEP("footstep"),
    /**
     * A particle effect which is displayed by swimming entities, rain dropping
     * on the ground and shaking wolves:
     * <ul>
     * <li>It looks like a blue drop
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SPLASH("splash"),
    /**
     * A particle effect which is displayed on water when fishing:
     * <ul>
     * <li>It looks like a blue droplet
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    WAKE("wake"),
    /**
     * A particle effect which is displayed by fire, minecarts with furnace and
     * blazes:
     * <ul>
     * <li>It looks like a large gray cloud
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    LARGE_SMOKE("largesmoke"),
    /**
     * A particle effect which is displayed when a mob dies:
     * <ul>
     * <li>It looks like a large white cloud
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    CLOUD("cloud"),
    /**
     * A particle effect which is displayed by redstone ore, powered redstone,
     * redstone torches and redstone repeaters:
     * <ul>
     * <li>It looks like a tiny colored cloud
     * <li>The speed value causes the particle to be colored red when set to 0
     * </ul>
     */
    RED_DUST("reddust"),
    /**
     * A particle effect which is displayed when snowballs or eggs hit
     * something:
     * <ul>
     * <li>It looks like a tiny part of the snowball icon
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SNOWBALL_POOF("snowballpoof"),
    /**
     * A particle effect which is displayed by blocks beneath a water source:
     * <ul>
     * <li>It looks like a blue drip
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    DRIP_WATER("dripWater"),
    /**
     * A particle effect which is displayed by blocks beneath a lava source:
     * <ul>
     * <li>It looks like an orange drip
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    DRIP_LAVA("dripLava"),
    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It looks like a tiny white cloud
     * <li>The speed value influences the velocity at which the particle flies
     * off
     * </ul>
     */
    SNOW_SHOVEL("snowshovel"),
    /**
     * A particle effect which is displayed by slimes:
     * <ul>
     * <li>It looks like a tiny part of the slimeball icon
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SLIME("slime"),
    /**
     * A particle effect which is displayed when breeding and taming animals:
     * <ul>
     * <li>It looks like a red heart
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    HEART("heart"),
    /**
     * A particle effect which is displayed when attacking a villager in a
     * village:
     * <ul>
     * <li>It looks like a cracked gray heart
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    ANGRY_VILLAGER("angryVillager"),
    /**
     * A particle effect which is displayed when using bone meal and trading
     * with a villager in a village:
     * <ul>
     * <li>It looks like a green star
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    HAPPY_VILLAGER("happyVillager");

    private static final Map<String, ParticleEffect> NAME_MAP = new HashMap<>();

    static {
        for (ParticleEffect effect : values()) {
            NAME_MAP.put(effect.name, effect);
        }
    }

    private final String name;
    private final boolean requiresWater;

    ParticleEffect(String name, boolean requiresWater) {
        this.name = name;
        this.requiresWater = requiresWater;
    }

    ParticleEffect(String name) {
        this(name, false);
    }

    public static ParticleEffect fromString(String name) {
        for (Entry<String, ParticleEffect> entry : NAME_MAP.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }

    private static boolean isWater(Location location) {
        Material material = location.getBlock().getType();
        return material == Material.WATER
                || material == Material.STATIONARY_WATER;
    }

    @SuppressWarnings("deprecation")
    private static boolean isBlock(int id) {
        Material material = Material.getMaterial(id);
        return material != null && material.isBlock();
    }

    public static void displayIconCrack(int id, byte data, float offsetX,
                                        float offsetY, float offsetZ, float speed, int amount,
                                        Location center, double range) {
        new ParticleEffectPacket("iconcrack_" + id + "_" + data, offsetX,
                offsetY, offsetZ, speed, amount).sendTo(center, range);
    }

    public static void displayIconCrack(int id, byte data, float offsetX,
                                        float offsetY, float offsetZ, float speed, int amount,
                                        Location center, List<Player> players) {
        new ParticleEffectPacket("iconcrack_" + id + "_" + data, offsetX,
                offsetY, offsetZ, speed, amount).sendTo(center, players);
    }

    public static void displayBlockCrack(int id, byte data, float offsetX,
                                         float offsetY, float offsetZ, int amount, Location center,
                                         double range) throws IllegalArgumentException {
        if (!isBlock(id)) {
            throw new IllegalArgumentException("Invalid block id");
        }
        new ParticleEffectPacket("blockcrack_" + id + "_" + data, offsetX,
                offsetY, offsetZ, 0, amount).sendTo(center, range);
    }

    public static void displayBlockCrack(int id, byte data, float offsetX,
                                         float offsetY, float offsetZ, int amount, Location center,
                                         List<Player> players) throws IllegalArgumentException {
        if (!isBlock(id)) {
            throw new IllegalArgumentException("Invalid block id");
        }
        new ParticleEffectPacket("blockcrack_" + id + "_" + data, offsetX,
                offsetY, offsetZ, 0, amount).sendTo(center, players);
    }

    public static void displayBlockDust(int id, byte data, float offsetX,
                                        float offsetY, float offsetZ, float speed, int amount,
                                        Location center, double range) throws IllegalArgumentException {
        if (!isBlock(id)) {
            throw new IllegalArgumentException("Invalid block id");
        }
        new ParticleEffectPacket("blockdust_" + id + "_" + data, offsetX,
                offsetY, offsetZ, speed, amount).sendTo(center, range);
    }

    public static void displayBlockDust(int id, byte data, float offsetX,
                                        float offsetY, float offsetZ, float speed, int amount,
                                        Location center, List<Player> players)
            throws IllegalArgumentException {
        if (!isBlock(id)) {
            throw new IllegalArgumentException("Invalid block id");
        }
        new ParticleEffectPacket("blockdust_" + id + "_" + data, offsetX,
                offsetY, offsetZ, speed, amount).sendTo(center, players);
    }

    public String getName() {
        return name;
    }

    public boolean getRequiresWater() {
        return requiresWater;
    }

    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range)
            throws IllegalArgumentException {
        if (requiresWater && !isWater(center)) {
            throw new IllegalArgumentException(
                    "There is no water at the center location");
        }
        new ParticleEffectPacket(name, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
    }

    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center,
                        double range, Player player) throws IllegalArgumentException {
        if (requiresWater && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticleEffectPacket(name, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, player);
    }

    public void displayArcade(Player source, float offsetX, float offsetY,
                              float offsetZ, float speed, int amount, Location center,
                              double range) throws IllegalArgumentException {
        if (requiresWater && !isWater(center)) {
            throw new IllegalArgumentException(
                    "There is no water at the center location");
        }
        new ParticleEffectPacket(name, offsetX, offsetY, offsetZ, speed, amount)
                .sendToArcade(source, center, range);
    }

    public void display(float offsetX, float offsetY, float offsetZ,
                        float speed, int amount, Location center, List<Player> players)
            throws IllegalArgumentException {
        if (requiresWater && !isWater(center)) {
            throw new IllegalArgumentException(
                    "There is no water at the center location");
        }
        new ParticleEffectPacket(name, offsetX, offsetY, offsetZ, speed, amount)
                .sendTo(center, players);
    }

    public static final class ParticleEffectPacket {
        private static Constructor<?> packetConstructor;
        private static Method getHandle;
        private static Field playerConnection;
        private static Method sendPacket;
        private static boolean initialized;
        private final String name;
        private final float offsetX;
        private final float offsetY;
        private final float offsetZ;
        private final float speed;
        private final int amount;
        private Object packet;

        public ParticleEffectPacket(String name, float offsetX, float offsetY,
                                    float offsetZ, float speed, int amount)
                throws IllegalArgumentException {
            initialize();
            if (speed < 0) {
                throw new IllegalArgumentException("The speed is lower than 0");
            }
            if (amount < 1) {
                throw new IllegalArgumentException("The amount is lower than 1");
            }
            this.name = name;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.speed = speed;
            this.amount = amount;
        }

        public static void initialize() throws VersionIncompatibleException {
            if (initialized) {
                return;
            }
            try {
                int version = Integer.parseInt(Character.toString(ReflectionUtils.PackageType
                        .getServerVersion().charAt(3)));
                Class<?> packetClass = ReflectionUtils.PackageType.MINECRAFT_SERVER
                        .getClass(version < 7 ? "Packet63WorldParticles"
                                : ReflectionUtils.PacketType.PLAY_OUT_WORLD_PARTICLES.getName());
                packetConstructor = ReflectionUtils.getConstructor(packetClass);
                getHandle = ReflectionUtils.getMethod("CraftPlayer",
                        ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
                playerConnection = ReflectionUtils
                        .getField("EntityPlayer", ReflectionUtils.PackageType.MINECRAFT_SERVER,
                                false, "playerConnection");
                sendPacket = ReflectionUtils.getMethod(
                        playerConnection.getType(), "sendPacket",
                        ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("Packet"));
            } catch (Exception exception) {
                throw new VersionIncompatibleException(
                        "Your current bukkit version seems to be incompatible with this library",
                        exception);
            }
            initialized = true;
        }

        public static boolean isInitialized() {
            return initialized;
        }

        public static EnumParticle getParticle(String name) {
            switch (name.toLowerCase()) {
                case "barrier":
                    return EnumParticle.BARRIER;
                case "blockcrack":
                    return EnumParticle.BLOCK_CRACK;
                case "blockdust":
                    return EnumParticle.BLOCK_DUST;
                case "cloud":
                    return EnumParticle.CLOUD;
                case "crit":
                    return EnumParticle.CRIT;
                case "critmagic":
                    return EnumParticle.CRIT_MAGIC;
                case "driplava":
                    return EnumParticle.DRIP_LAVA;
                case "dripwater":
                    return EnumParticle.DRIP_WATER;
                case "enchantmenttable":
                    return EnumParticle.ENCHANTMENT_TABLE;
                case "hugeexplosion":
                    return EnumParticle.EXPLOSION_HUGE;
                case "largeexplosion":
                    return EnumParticle.EXPLOSION_LARGE;
                case "explode":
                    return EnumParticle.EXPLOSION_NORMAL;
                case "fireworksspark":
                    return EnumParticle.FIREWORKS_SPARK;
                case "flame":
                    return EnumParticle.FLAME;
                case "footstep":
                    return EnumParticle.FOOTSTEP;
                case "heart":
                    return EnumParticle.HEART;
                case "itemcrack":
                    return EnumParticle.ITEM_CRACK;
                case "itemtake":
                    return EnumParticle.ITEM_TAKE;
                case "lava":
                    return EnumParticle.LAVA;
                case "mobappearance":
                    return EnumParticle.MOB_APPEARANCE;
                case "note":
                    return EnumParticle.NOTE;
                case "portal":
                    return EnumParticle.PORTAL;
                case "reddust":
                    return EnumParticle.REDSTONE;
                case "slime":
                    return EnumParticle.SLIME;
                case "largesmoke":
                    return EnumParticle.SMOKE_LARGE;
                case "smoke":
                    return EnumParticle.SMOKE_NORMAL;
                case "snowshovel":
                    return EnumParticle.SNOW_SHOVEL;
                case "snowball":
                    return EnumParticle.SNOWBALL;
                case "spell":
                    return EnumParticle.SPELL;
                case "instantspell":
                    return EnumParticle.SPELL_INSTANT;
                case "mobspell":
                    return EnumParticle.SPELL_MOB;
                case "mobspellambient":
                    return EnumParticle.SPELL_MOB_AMBIENT;
                case "witchmagic":
                    return EnumParticle.SPELL_WITCH;
                case "suspended":
                    return EnumParticle.SUSPENDED;
                case "suspendeddepth":
                    return EnumParticle.SUSPENDED_DEPTH;
                case "townaura":
                    return EnumParticle.TOWN_AURA;
                case "angryvillager":
                    return EnumParticle.VILLAGER_ANGRY;
                case "happyvillager":
                    return EnumParticle.VILLAGER_HAPPY;
                case "bubble":
                    return EnumParticle.WATER_BUBBLE;
                case "droplet":
                    return EnumParticle.WATER_DROP;
                case "splash":
                    return EnumParticle.WATER_SPLASH;
                case "wake":
                    return EnumParticle.WATER_WAKE;
                default:
                    return null;
            }
        }

        public void sendTo(Location center, Player player) throws PacketInstantiationException, PacketSendingException {
            if (packet == null) {
                try {
                    packet = packetConstructor.newInstance();
                    ReflectionUtils.setValue(packet, true, "a", getParticle(name));
                    ReflectionUtils.setValue(packet, true, "b", (float) center.getX());
                    ReflectionUtils.setValue(packet, true, "c", (float) center.getY());
                    ReflectionUtils.setValue(packet, true, "d", (float) center.getZ());
                    ReflectionUtils.setValue(packet, true, "e", offsetX);
                    ReflectionUtils.setValue(packet, true, "f", offsetY);
                    ReflectionUtils.setValue(packet, true, "g", offsetZ);
                    ReflectionUtils.setValue(packet, true, "h", speed);
                    ReflectionUtils.setValue(packet, true, "i", amount);
                    ReflectionUtils.setValue(packet, true, "j", true);
                    ReflectionUtils.setValue(packet, true, "k", new int[0]);
                } catch (Exception exception) {
                    throw new PacketInstantiationException("Packet instantiation failed", exception);
                }
            }
            try {
                sendPacket.invoke(
                        playerConnection.get(getHandle.invoke(player)), packet);
            } catch (Exception exception) {
                throw new PacketSendingException(
                        "Failed to send the packet to player '"
                                + player.getName() + "'", exception);
            }
        }

        public void sendTo(Location center, List<Player> players)
                throws IllegalArgumentException {
            if (players.isEmpty()) {
                throw new IllegalArgumentException("The player list is empty");
            }
            for (Player player : players) {
                sendTo(center, player);
            }
        }

        public void sendTo(Location center, double range)
                throws IllegalArgumentException {
            if (range < 1) {
                throw new IllegalArgumentException("The range is lower than 1");
            }
            String worldName = center.getWorld().getName();
            double squared = range * range;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().getName().equals(worldName)
                        || player.getLocation().distanceSquared(center) > squared) {
                    continue;
                }
                sendTo(center, player);
            }
        }

        public void sendToArcade(Player p, Location center, double range)
                throws IllegalArgumentException {
            if (range < 1) {
                throw new IllegalArgumentException("The range is lower than 1");
            }
            String worldName = center.getWorld().getName();
            double squared = range * range;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getUniqueId() == p.getUniqueId()) {
                    sendTo(center, player);
                    continue;
                }
                if (player.canSee(p)) {
                    if (!player.getWorld().getName().equals(worldName)
                            || player.getLocation().distanceSquared(center) > squared) {
                        continue;
                    }
                    sendTo(center, player);
                }
            }
        }

        private static final class VersionIncompatibleException extends
                RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            public VersionIncompatibleException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        private static final class PacketInstantiationException extends
                RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            public PacketInstantiationException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        private static final class PacketSendingException extends
                RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            public PacketSendingException(String message, Throwable cause) {
                super(message, cause);
            }
        }
    }
}