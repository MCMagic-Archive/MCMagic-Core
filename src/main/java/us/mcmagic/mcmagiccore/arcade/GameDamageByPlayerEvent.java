package us.mcmagic.mcmagiccore.arcade;

import org.bukkit.entity.Player;

/**
 * Created by Marc on 9/14/15
 */
public class GameDamageByPlayerEvent extends GameDamageEvent {
    private Player damager;

    public GameDamageByPlayerEvent(Player player, Player damager, double damage) {
        super(player, damage);
        this.damager = damager;
    }

    public Player getDamager() {
        return damager;
    }
}