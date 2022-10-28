package us.mcmagic.mcmagiccore.audioserver;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.dashboard.packets.audio.PacketAreaStart;
import us.mcmagic.mcmagiccore.dashboard.packets.audio.PacketAreaStop;
import us.mcmagic.mcmagiccore.dashboard.packets.audio.PacketAudioSync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Marc on 6/15/15
 */
public class AudioArea {
    private UUID uniqueId = UUID.randomUUID();
    public static int VEHICLE_DEFAULT = 0;
    public static int VEHICLE_ONRIDE = 1;
    public static int VEHICLE_OFFRIDE = 2;
    private final String name;
    private final String type;
    private final int audioid = AudioServer.getAudioid();
    private final List<UUID> playersInArea = new ArrayList<>();
    private Location loc1;
    private Location loc2;
    private ProtectedRegion region;
    private World world;
    private String soundname = "";
    private int fadetime;
    private double volume;
    private boolean startonenter;
    private int vehicletype;
    private boolean repeat;
    private boolean enabled = true;
    private boolean defaultState = true;
    private boolean useRegion = true;

    public AudioArea(String name, String soundname, int fadetime, double volume, String type, Location loc1,
                     Location loc2, boolean enabled, boolean startonenter, int vehicletype, boolean repeat, World world) {
        this(name, soundname, fadetime, volume, type, new ProtectedCuboidRegion("as-" + name,
                new BlockVector(loc1.getX(), loc1.getY(), loc1.getZ()), new BlockVector(loc2.getX(), loc2.getY(),
                loc2.getZ())), enabled, startonenter, vehicletype, repeat, world);
        Location newLoc1 = loc1.clone();
        Location newLoc2 = loc2.clone();
        if (loc1.getX() > loc2.getX()) {
            newLoc1.setX(loc2.getX());
            newLoc2.setX(loc1.getX());
        }
        if (loc1.getY() > loc2.getY()) {
            newLoc1.setY(loc2.getY());
            newLoc2.setY(loc1.getY());
        }
        if (loc1.getZ() > loc2.getZ()) {
            newLoc1.setZ(loc2.getZ());
            newLoc2.setZ(loc1.getZ());
        }
        this.loc1 = newLoc1;
        this.loc2 = newLoc2;
        this.region = new ProtectedCuboidRegion("as-" + name, new BlockVector(this.loc1.getX(), this.loc1.getY(), this.loc1.getZ()),
                new BlockVector(this.loc2.getX(), this.loc2.getY(), this.loc2.getZ()));
        this.useRegion = false;
    }

    public AudioArea(String name, String soundname, int fadetime, double volume, String type, ProtectedRegion region,
                     boolean enabled, boolean startonenter, int vehicletype, boolean repeat, World world) {
        defaultState = enabled;
        this.enabled = enabled;
        this.soundname = soundname;
        this.fadetime = fadetime;
        this.volume = volume;
        this.type = type;
        this.name = name;
        this.startonenter = startonenter;
        this.vehicletype = vehicletype;
        this.repeat = repeat;
        this.region = region;
        this.world = world;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public static String getType(int type) {
        if (type == VEHICLE_ONRIDE)
            return "onride";
        if (type == VEHICLE_OFFRIDE)
            return "offride";
        return "default";
    }

    public void setArea(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getDefaultState() {
        return defaultState;
    }

    public void setDefaultState(boolean defaultState) {
        this.defaultState = defaultState;
    }

    public void triggerPlayer(Player player) {
        if (vehicletype != VEHICLE_DEFAULT) {
            if ((player.isInsideVehicle()) && (vehicletype == VEHICLE_OFFRIDE))
                return;
            if ((player.isInsideVehicle()) && (vehicletype != VEHICLE_ONRIDE)) {
                return;
            }
        }
        if (playersInArea.contains(player.getUniqueId()))
            PacketHelper.sendToPlayer(new PacketAreaStart(audioid, soundname, (float) volume, fadetime, repeat), player);
    }

    public void addPlayerIfInside(Player player) {
        for (AudioArea area : MCMagicCore.audioServer.getAudioAreas())
            if ((player.getWorld().equals(area.getWorld())) && (area.locIsInArea(player.getLocation())) && (area.isEnabled())) {
                area.addPlayer(player);
            }
    }

    public void addPlayer(Player player) {
        if ((player.isInsideVehicle()) && (vehicletype == VEHICLE_OFFRIDE)) {
            removePlayer(player.getUniqueId(), true);
            return;
        }
        if ((!player.isInsideVehicle()) && (vehicletype == VEHICLE_ONRIDE)) {
            removePlayer(player.getUniqueId(), true);
            return;
        }

        if (!playersInArea.contains(player.getUniqueId())) {
            playersInArea.add(player.getUniqueId());
            if (startonenter)
                PacketHelper.sendToPlayer(new PacketAreaStart(audioid, soundname, (float) volume, fadetime, repeat), player);
        }
    }

    public void sync(float time) {
        synchronized (playersInArea) {
            for (UUID uuid : playersInArea) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    PacketHelper.sendToPlayer(new PacketAudioSync(audioid, time), player);
                }
            }
        }
    }

    public void sync(float time, Player player) {
        synchronized (playersInArea) {
            PacketHelper.sendToPlayer(new PacketAudioSync(audioid, time), player);
        }
    }

    public void sync(float time, Player player, double margin) {
        synchronized (playersInArea) {
            PacketHelper.sendToPlayer(new PacketAudioSync(audioid, time, margin), player);
        }
    }

    public void removeAllPlayers(boolean sendpacket) {
        synchronized (playersInArea) {
            for (UUID uuid : playersInArea) {
                Player player = Bukkit.getPlayer(uuid);
                if (sendpacket) {
                    PacketHelper.sendToPlayer(new PacketAreaStop(audioid, fadetime), player);
                }
            }
            playersInArea.clear();
        }
    }

    public void removePlayer(UUID uuid) {
        removePlayer(uuid, true);
    }

    public void removePlayer(UUID uuid, boolean sendpacket) {
        removePlayer(uuid, sendpacket, fadetime);
    }

    public void removePlayer(UUID uuid, boolean sendpacket, int fadetime) {
        if ((playersInArea.remove(uuid)) && (sendpacket))
            PacketHelper.sendToPlayer(new PacketAreaStop(audioid, fadetime), Bukkit.getPlayer(uuid));
    }

    public void disableToPlayer(Player player) {
        if (playersInArea.contains(player.getUniqueId()))
            PacketHelper.sendToPlayer(new PacketAreaStop(audioid, fadetime), player);
    }

    public boolean isInArea(Player player) {
        return playersInArea.contains(player.getUniqueId());
    }

    public String getAreaName() {
        return name;
    }

    public String getSoundName() {
        return soundname;
    }

    public boolean getRepeat() {
        return repeat;
    }

    public int getVehicleType() {
        return vehicletype;
    }

    public List<UUID> getPlayersInArea() {
        return new ArrayList<>(playersInArea);
    }

    public boolean locIsInArea(Location loc) {
        if (region != null) {
            return region.contains(new Vector(loc.getX(), loc.getY(), loc.getZ()));
        }
        return (loc.getWorld().getName().equalsIgnoreCase(world.getName())) && (loc.getX() > loc1.getBlockX()) &&
                (loc.getX() < loc2.getBlockX()) && (loc.getY() > loc1.getBlockY()) && (loc.getY() < loc2.getBlockY()) &&
                (loc.getZ() > loc1.getBlockZ()) && (loc.getZ() < loc2.getBlockZ());
    }

    public void sendOptions(CommandSender sender) {
        sender.sendMessage("§6--- [" + name + "] ---");
        sender.sendMessage("§3Fade time: §e" + fadetime);
        sender.sendMessage("§9Volume: §e" + volume);
        sender.sendMessage("§3Type: §e" + type);
        sender.sendMessage("§9Soundname: §e" + soundname);
        sender.sendMessage("§3Enabled: §e" + enabled);
        sender.sendMessage("§9Start on enter: §e" + startonenter);
        sender.sendMessage("§3World: §e" + world.getName());
        sender.sendMessage("§3Repeat: §e" + repeat);
        sender.sendMessage("§3Type: §e" + getType(vehicletype));
        if (playersInArea.size() > 0)
            sender.sendMessage("§9Players: §e" + StringUtils.join(playersInArea, ", "));
        else
            sender.sendMessage("§9There are currently no players in this area");
    }

    public String setOption(String option) {
        String[] optionvalue = option.split(":");
        if (optionvalue.length == 2) {
            if (optionvalue[0].equalsIgnoreCase("startonenter")) {
                startonenter = ((optionvalue[1].equalsIgnoreCase("true")) || (optionvalue[1].equalsIgnoreCase("1")));
                return "§eThe option §6[startonenter]§e has been set to §6[" + startonenter + "]";
            }
            if (optionvalue[0].equalsIgnoreCase("enabled")) {
                enabled = ((optionvalue[1].equalsIgnoreCase("true")) || (optionvalue[1].equalsIgnoreCase("1")));
                return "§eThe option §6[enabled]§e has been set to §6[" + enabled + "]";
            }
            if (optionvalue[0].equalsIgnoreCase("repeat")) {
                repeat = ((optionvalue[1].equalsIgnoreCase("true")) || (optionvalue[1].equalsIgnoreCase("1")));
                return "§eThe option §6[repeat]§e has been set to §6[" + repeat + "]";
            }
            if (optionvalue[0].equalsIgnoreCase("fadetime")) {
                fadetime = MCMagicCore.audioServer.getIntFromString(optionvalue[1], 500);
                return "§eThe option §6[fadetime]§e has been set to §6[" + fadetime + "]";
            }
            if (optionvalue[0].equalsIgnoreCase("vehicletype")) {
                if (optionvalue[1].equalsIgnoreCase("default")) {
                    vehicletype = MCMagicCore.audioServer.getIntFromString(optionvalue[1], VEHICLE_DEFAULT);
                    return "§eThe option §6[vehicletype]§e has been set to §6[default]";
                }
                if (optionvalue[1].equalsIgnoreCase("onride")) {
                    vehicletype = MCMagicCore.audioServer.getIntFromString(optionvalue[1], VEHICLE_ONRIDE);
                    return "§eThe option §6[vehicletype]§e has been set to §6[onride]";
                }
                if (optionvalue[1].equalsIgnoreCase("offride")) {
                    vehicletype = MCMagicCore.audioServer.getIntFromString(optionvalue[1], VEHICLE_OFFRIDE);
                    return "§eThe option §6[vehicletype]§e has been set to §6[offride]";
                }
                vehicletype = MCMagicCore.audioServer.getIntFromString(optionvalue[1], VEHICLE_DEFAULT);
                return "§eThe option §6[vehicletype]§e has been set to §6[" + vehicletype + "]";
            }
            if (optionvalue[0].equalsIgnoreCase("volume")) {
                volume = MCMagicCore.audioServer.getDoubleFromString(optionvalue[1], 1.0D);
                return "§eThe option §6[volume]§e has been set to §6[" + volume + "]";
            }
            if (optionvalue[0].equalsIgnoreCase("soundname")) {
                soundname = optionvalue[1];
                return "§eThe option §6[soundname]§e has been set to §6[" + soundname + "]";
            }
            return "§eThe option §6[" + optionvalue[0] + "]§e isn't recognised!";
        }
        return "§eThe option §6[" + option + "]§e isn't using the correct syntax of option:value";
    }

    public void saveToConfig(FileConfiguration config) {
        ConfigurationSection areaSection = config.createSection("areas." + name);
        if (areaSection == null) {
            return;
        }
        try {
            areaSection.set("world", world.getName());
        } catch (Exception e) {
            areaSection.set("world", "");
        }
        areaSection.set("fadetime", fadetime);
        areaSection.set("volume", volume);
        areaSection.set("type", type);
        areaSection.set("soundname", soundname);
        areaSection.set("enabled", defaultState);
        areaSection.set("startonenter", startonenter);
        areaSection.set("repeat", repeat);
        areaSection.set("vehicletype", vehicletype);
        if (useRegion) {
            areaSection.set("region", region.getId());
        } else {
            ConfigurationSection corner_1 = areaSection.createSection("corner_1");
            corner_1.set("x", loc1.getBlockX());
            corner_1.set("y", loc1.getBlockY());
            corner_1.set("z", loc1.getBlockZ());
            ConfigurationSection corner_2 = areaSection.createSection("corner_2");
            corner_2.set("x", loc2.getBlockX());
            corner_2.set("y", loc2.getBlockY());
            corner_2.set("z", loc2.getBlockZ());
        }
        try {
            config.save(MCMagicCore.audioServer.getAreaFile());
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.broadcast("§4Failed to save Audio Area " + name, "audio.errornotify");
        }
    }

    public World getWorld() {
        return world;
    }
}