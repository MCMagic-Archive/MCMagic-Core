package us.mcmagic.mcmagiccore.config;

import org.bukkit.configuration.file.YamlConfiguration;
import us.mcmagic.mcmagiccore.MCMagicCore;

import java.io.File;

/**
 * Created by Marc on 3/18/15
 */
public class MCMagicConfig {
    public boolean titleEnabled;
    public boolean tabEnabled;
    public boolean informationSidebarEnabled;
    public boolean rankTabEnabled;
    public String serverName;
    public String instanceName;
    public boolean handleChat;
    public boolean gameServer;
    public boolean resource;
    public String sqlConnectionUrl;
    public String sqlUser;
    public String sqlPassword;
    public String mapDirectory;
    public String pos;
    public boolean audioServer;

    public MCMagicConfig(YamlConfiguration config) {
        titleEnabled = config.getBoolean("title.enabled");
        handleChat = config.contains("handle-chat") && config.getBoolean("handle-chat");
        tabEnabled = config.getBoolean("tab.enabled");
        gameServer = config.getBoolean("game-server");
        serverName = config.getString("server-name");
        instanceName = config.getString("instance-name");
        if (instanceName == null || instanceName.equals("")) {
            instanceName = serverName;
        }
        resource = config.getBoolean("resource.enabled");
        audioServer = config.getBoolean("audio-server");
        if (!config.contains("scoreboard.sidebar.enabled")) {
            config.set("scoreboard.sidebar.enabled", false);
        } else {
            informationSidebarEnabled = config.getBoolean("scoreboard.sidebar.enabled");
        }
        if (!config.contains("scoreboard.ranktab.enabled")) {
            config.set("scoreboard.ranktab.enabled", false);
        } else {
            rankTabEnabled = config.getBoolean("scoreboard.ranktab.enabled");
        }
        sqlConnectionUrl = "jdbc:mysql://" + config.getString("sql.ip") + ":" + config.getString("sql.port") + "/" +
                config.getString("sql.database");
        sqlUser = config.getString("sql.username");
        sqlPassword = config.getString("sql.password");
        pos = config.getString("park-or-server");
        if (pos == null) {
            config.set("park-or-server", "Park");
            pos = "Park";
        }
        MCMagicCore.saveFile(new File("plugins/MCMagicCore/config.yml"), config);
    }
}
