package us.mcmagic.mcmagiccore;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SetupConfigFile {

    public static void execute() {
        File configfile = new File("plugins/MCMagicCore/config.yml");
        try {
            configfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        YamlConfiguration config = YamlConfiguration
                .loadConfiguration(configfile);
        config.set("sql.ip", "ip");
        config.set("sql.port", "3306");
        config.set("sql.database", "MainServer");
        config.set("sql.username", "root");
        config.set("sql.password", "password");
        config.set("title.join.top", "&aWelcome to &bMCMagic");
        config.set("title.join.bottom", "&aA Family of Servers");
        config.set("title.stay", 50);
        config.set("title.fade.in", -1);
        config.set("title.fade.out", -1);
        config.set("title.enabled", false);
        config.set("tab.header", "&bMCMagic - &aA Family of Servers");
        config.set("tab.footer", "&bYou are at the &aTTC");
        config.set("tab.enabled", false);
        config.set("scoreboard.sidebar.enabled", false);
        config.set("scoreboard.ranktab.enabled", false);
        MCMagicCore.saveFile(configfile, config);
    }
}