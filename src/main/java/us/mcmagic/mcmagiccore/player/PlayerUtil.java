package us.mcmagic.mcmagiccore.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;

/**
 * Created by Marc on 11/18/14
 */
public class PlayerUtil {

    public static Player findPlayer(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().contains(name.toLowerCase())) {
                return player;
            }
        }
        return null;
    }

    public static User getUser(String name) {
        for (User user : MCMagicCore.getUsers()) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }
}
