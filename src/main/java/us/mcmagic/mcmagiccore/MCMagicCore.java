package us.mcmagic.mcmagiccore;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.mcmagic.mcmagiccore.achievements.AchievementManager;
import us.mcmagic.mcmagiccore.arcade.GameManager;
import us.mcmagic.mcmagiccore.audioserver.AudioServer;
import us.mcmagic.mcmagiccore.bungee.WorldDownloadProtect;
import us.mcmagic.mcmagiccore.chat.ChatManager;
import us.mcmagic.mcmagiccore.commands.*;
import us.mcmagic.mcmagiccore.config.MCMagicConfig;
import us.mcmagic.mcmagiccore.dashboard.DashboardConnection;
import us.mcmagic.mcmagiccore.economy.Economy;
import us.mcmagic.mcmagiccore.listeners.*;
import us.mcmagic.mcmagiccore.permissions.PermManager;
import us.mcmagic.mcmagiccore.permissions.PermSqlUtil;
import us.mcmagic.mcmagiccore.player.User;
import us.mcmagic.mcmagiccore.resource.ResourceManager;
import us.mcmagic.mcmagiccore.scoreboard.ScoreboardUtility;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class MCMagicCore extends JavaPlugin {
    public static boolean canJoin = false;
    public static PermSqlUtil permSqlUtil = new PermSqlUtil();
    public static PermManager permManager = new PermManager();
    public static Economy economy;
    public static ResourceManager resourceManager;
    public static GameManager gameManager = new GameManager();
    public static ChatManager chatManager = new ChatManager();
    public static AudioServer audioServer;
    public static boolean serverStarting = true;
    private static HashMap<UUID, User> users = new HashMap<>();
    private static MCMagicConfig config;
    private static MCMagicCore instance;
    private static long startTime;
    private File configfile = new File("plugins/MCMagicCore/config.yml");
    public static AchievementManager achievementManager;
    public static DashboardConnection dashboardConnection;

    public static MCMagicConfig getMCMagicConfig() {
        return config;
    }

    public static MCMagicCore getInstance() {
        return instance;
    }

    public static HashMap<UUID, User> getUserMap() {
        return new HashMap<>(users);
    }

    public static List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public static void addUser(User user) {
        Preconditions.checkNotNull(user.getUniqueId());
        users.remove(user.getUniqueId());
        users.put(user.getUniqueId(), user);
    }

    public static User getUser(String name) {
        for (User user : new ArrayList<>(users.values())) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public static User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void deleteUser(UUID uuid) {
        users.remove(uuid);
    }

    public static void saveFile(File file, YamlConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEnable() {
        startTime = System.currentTimeMillis();
        instance = this;
        config = new MCMagicConfig(YamlConfiguration.loadConfiguration(configfile));
        try {
            dashboardConnection = new DashboardConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            getLogger().severe("Error connecting to Dashboard!");
            return;
        }
        resourceManager = new ResourceManager();
        economy = new Economy();
        try {
            System.out.println("Initializing Resource Manager...");
            resourceManager.initialize();
            System.out.println("Resource Manager Initialized!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        File folder = new File("plugins/MCMagicCore");
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (!configfile.exists()) {
            SetupConfigFile.execute();
        }
        registerCommands();
        registerListeners();
        ScoreboardUtility.start();
        PlayerJoinAndLeave.initialize();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "WDL|INIT", new WorldDownloadProtect());
        if (config.audioServer) {
            Bukkit.getLogger().info("Initializing Audio Server...");
            audioServer = new AudioServer();
            Bukkit.getLogger().info("Audio Server Initialized!");
        }
        permManager.setupPermissions();
        achievementManager = new AchievementManager();
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                getLogger().log(Level.INFO, "Players can now join.");
                serverStarting = false;
            }
        }, 40L);
    }

    public void onDisable() {
        permManager.attachments.clear();
        if (config.audioServer) {
            audioServer.saveAreas();
        }
    }

    public void refreshConfig() {
        config = new MCMagicConfig(YamlConfiguration.loadConfiguration(configfile));
    }

    public void registerCommands() {
        getCommand("ach").setExecutor(new Commandach());
        if (config.audioServer) {
            getCommand("audio").setExecutor(new Commandaudio());
            getCommand("audio").setAliases(Collections.singletonList("as"));
        }
        getCommand("balance").setExecutor(new Commandbalance());
        getCommand("balance").setAliases(Collections.singletonList("bal"));
        getCommand("mccore").setExecutor(new Commandmccore());
        getCommand("token").setExecutor(new Commandtoken());
        getCommand("tagtoggle").setExecutor(new Commandtagtoggle());
        getCommand("ot").setExecutor(new Commandot());
        getCommand("perm").setExecutor(new Commandperm());
        getCommand("perm").setTabCompleter(PermManager.getTabCompleter());
        getCommand("safestop").setExecutor(new Commandsafestop());
        getCommand("save").setExecutor(new Commandsave());
        getCommand("test").setExecutor(new Commandtest());
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerChat(), this);
        pm.registerEvents(new PlayerJoinAndLeave(), this);
        pm.registerEvents(new Achievements(), this);
        pm.registerEvents(new PlayerPickupItem(), this);
        if (config.audioServer) {
            pm.registerEvents(new PlayerMove(), this);
        }
    }
}