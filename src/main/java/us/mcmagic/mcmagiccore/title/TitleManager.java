package us.mcmagic.mcmagiccore.title;

import org.bukkit.configuration.file.FileConfiguration;
import us.mcmagic.mcmagiccore.MCMagicCore;

public class TitleManager {

    public static final int PROTOCOL_VERSION = 47;
    private static MCMagicCore plugin;

    public static FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public static void saveConfig() {
        plugin.saveConfig();
    }

    public static MCMagicCore getPlugin() {
        return plugin;
    }

    public static void setPlugin(MCMagicCore plugin) {
        TitleManager.plugin = plugin;
    }
}
