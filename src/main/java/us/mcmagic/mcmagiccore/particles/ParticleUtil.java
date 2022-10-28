package us.mcmagic.mcmagiccore.particles;

import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleUtil {

    public static void spawnParticle(ParticleEffect effect, Location center, float offsetX, float offsetY, float offsetZ,
                                     float speed, int amount) {
        effect.display(offsetX, offsetY, offsetZ, speed, amount, center, 50.0);
    }

    public static void spawnParticleArcade(Player source, ParticleEffect effect, Location center, float offsetX,
                                           float offsetY, float offsetZ, float speed, int amount) {
        effect.displayArcade(source, offsetX, offsetY, offsetZ, speed, amount,
                center, 150.0);
    }

    public static void spawnParticleForPlayer(ParticleEffect effect, Location center, float offsetX, float offsetY,
                                              float offsetZ, float speed, int amount, Player player) {
        effect.display(offsetX, offsetY, offsetZ, speed, amount, center, 50.0, player);
    }

    public static void displayBlockCrack(int id, byte data, float offsetX,
                                         float offsetY, float offsetZ, int amount, Location center) {
        ParticleEffect.displayBlockCrack(id, data, offsetX, offsetY, offsetZ,
                amount, center, 150.0);
    }

    public static EnumParticle particleFromString(String string) {
        switch (string) {
            case "enchantmenttable":
                return EnumParticle.ENCHANTMENT_TABLE;
            case "townaura":
                return EnumParticle.TOWN_AURA;
            case "crit":
                return EnumParticle.CRIT;
            case "magiccrit":
                return EnumParticle.CRIT_MAGIC;
            case "smoke":
                return EnumParticle.SMOKE_NORMAL;
            case "mobspell":
                return EnumParticle.SPELL_MOB;
            case "mobspellambient":
                return EnumParticle.SPELL_MOB_AMBIENT;
            case "spell":
                return EnumParticle.SPELL;
            case "instantspell":
                return EnumParticle.SPELL_INSTANT;
            case "witchmagic":
                return EnumParticle.SPELL_WITCH;
            case "note":
                return EnumParticle.NOTE;
            case "portal":
                return EnumParticle.PORTAL;
            case "explode":
                return EnumParticle.EXPLOSION_NORMAL;
            case "hugeexplosion":
                return EnumParticle.EXPLOSION_HUGE;
            case "largeexplosion":
                return EnumParticle.EXPLOSION_LARGE;
            case "fireworksspark":
                return EnumParticle.FIREWORKS_SPARK;
            case "bubble":
                return EnumParticle.WATER_BUBBLE;
            case "suspend":
                return EnumParticle.SUSPENDED;
            case "depthsuspend":
                return EnumParticle.SUSPENDED_DEPTH;
            case "flame":
                return EnumParticle.FLAME;
            case "lava":
                return EnumParticle.LAVA;
            case "footstep":
                return EnumParticle.FOOTSTEP;
            case "splash":
                return EnumParticle.WATER_SPLASH;
            case "wake":
                return EnumParticle.WATER_WAKE;
            case "largesmoke":
                return EnumParticle.SMOKE_LARGE;
            case "cloud":
                return EnumParticle.CLOUD;
            case "reddust":
                return EnumParticle.REDSTONE;
            case "snowballpoof":
                return EnumParticle.SNOWBALL;
            case "dripwater":
                return EnumParticle.DRIP_WATER;
            case "driplava":
                return EnumParticle.DRIP_LAVA;
            case "snowshovel":
                return EnumParticle.SNOW_SHOVEL;
            case "slime":
                return EnumParticle.SLIME;
            case "heart":
                return EnumParticle.HEART;
            case "angryvillager":
                return EnumParticle.VILLAGER_ANGRY;
            case "happyvillager":
                return EnumParticle.VILLAGER_HAPPY;
            default:
                return null;
        }
    }
}