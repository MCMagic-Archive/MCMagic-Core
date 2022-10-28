package us.mcmagic.mcmagiccore.audioserver;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import us.mcmagic.mcmagiccore.MCMagicCore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Marc on 6/15/15
 */
public class AudioServer {
    public static final String AUDIOSERVER_PLUGIN_CHANNEL = "AudioServer";
    private static int audioid = 0;

    private final List<AudioArea> audioAreas = new ArrayList<>();
    private File areasFile;
    private FileConfiguration areasConfig;

    public AudioServer() {
        initialize();
    }

    public static int getAudioid() {
        return audioid++;
    }

    public List<AudioArea> getAudioAreas() {
        return audioAreas;
    }

    public File getAreaFile() {
        return areasFile;
    }

    public FileConfiguration getAreaConfigFile() {
        return areasConfig;
    }

    public void initialize() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(MCMagicCore.getInstance(), "AudioServer");
        parseAreas();
    }

    public void parseAreas() {
        try {
            firstRun();
            audioAreas.clear();
            if (areasConfig.getConfigurationSection("areas") == null) {
                return;
            }
            Set<String> areaNames = areasConfig.getConfigurationSection("areas").getKeys(false);
            for (String areaname : areaNames) {
                try {
                    ConfigurationSection currentArea = areasConfig.getConfigurationSection("areas." + areaname);
                    World world = Bukkit.getWorld(currentArea.getString("world"));
                    AudioArea area;
                    if (currentArea.getString("region") != null) {
                        ProtectedRegion region = WorldGuardPlugin.inst().getRegionManager(world).getRegion(currentArea.getString("region"));
                        if (region == null) {
                            Bukkit.getLogger().warning("Region '" + currentArea.getString("region") + "' not found in WorldGuard!");
                            continue;
                        }
                        area = new AudioArea(areaname, currentArea.getString("soundname"),
                                currentArea.getInt("fadetime"), currentArea.getDouble("volume"), currentArea.getString("type"),
                                region, currentArea.getBoolean("enabled"), currentArea.getBoolean("startonenter"),
                                currentArea.getInt("vehicletype"), currentArea.getBoolean("repeat"), world);
                    } else {
                        Location corner_1 = new Location(world, currentArea.getConfigurationSection("corner_1").getInt("x"),
                                currentArea.getConfigurationSection("corner_1").getInt("y"),
                                currentArea.getConfigurationSection("corner_1").getInt("z"));
                        Location corner_2 = new Location(world, currentArea.getConfigurationSection("corner_2").getInt("x"),
                                currentArea.getConfigurationSection("corner_2").getInt("y"),
                                currentArea.getConfigurationSection("corner_2").getInt("z"));
                        area = new AudioArea(areaname, currentArea.getString("soundname"),
                                currentArea.getInt("fadetime"), currentArea.getDouble("volume"), currentArea.getString("type"),
                                corner_1, corner_2, currentArea.getBoolean("enabled"), currentArea.getBoolean("startonenter"),
                                currentArea.getInt("vehicletype"), currentArea.getBoolean("repeat"), world);
                    }
                    if (world == null) {
                        area.setEnabled(false);
                    }
                    audioAreas.add(area);
                } catch (Exception e) {
                    Bukkit.getLogger().warning("Failed to load area " + areaname + ": " + e.getMessage());
                }
            }

            Bukkit.getLogger().info("AudioServer loaded a total of " + audioAreas.size() + " Audio Areas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(UUID uuid) {
        for (AudioArea area : getAudioAreas()) {
            area.removePlayer(uuid);
        }
    }

    public void saveAreas() {
        for (AudioArea area : audioAreas)
            area.saveToConfig(areasConfig);
    }

    public void loadYamls() {
        try {
            areasConfig.load(areasFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void firstRun() throws Exception {
        if (areasFile == null) {
            areasFile = new File("plugins/MCMagicCore/audio.yml");
        }
        if (!areasFile.exists()) {
            areasFile.createNewFile();
        }
        if (areasConfig == null) {
            areasConfig = new YamlConfiguration();
        }
        loadYamls();
    }

    public boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0);
    }

    public int getIntFromString(String str, int defval) {
        int ret = defval;
        if (!isEmpty(str))
            try {
                ret = Integer.parseInt(str);
            } catch (NumberFormatException ne) {
                ret = defval;
            }
        return ret;
    }

    public float getFloatFromString(String str, float defval) {
        float ret = defval;
        if (!isEmpty(str))
            try {
                ret = Float.parseFloat(str);
            } catch (NumberFormatException ne) {
                ret = defval;
            }
        return ret;
    }

    public double getDoubleFromString(String str, double defval) {
        double ret = defval;
        if (!isEmpty(str))
            try {
                ret = Double.parseDouble(str);
            } catch (NumberFormatException ne) {
                ret = defval;
            }
        return ret;
    }

    public AudioArea getByName(String name) {
        for (AudioArea area : audioAreas) {
            if (area.getAreaName().equalsIgnoreCase(name)) {
                return area;
            }
        }
        return null;
    }

    public void addArea(AudioArea area) {
        this.audioAreas.add(area);
    }

    public void removeArea(AudioArea area) {
        if (area == null) {
            return;
        }
        int i = 0;
        for (AudioArea a : getAudioAreas()) {
            if (a == null) {
                continue;
            }
            if (a.getUniqueId().equals(area.getUniqueId())) {
                this.audioAreas.remove(i);
                break;
            }
            i++;
        }
    }

    public void reloadAreas() {
        parseAreas();
    }
}