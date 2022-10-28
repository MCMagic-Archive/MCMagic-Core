package us.mcmagic.mcmagiccore.resource;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Created by Marc on 3/18/15
 */
public class ResourceStatusEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private PackStatus status;

    public ResourceStatusEvent(PackStatus status, Player player) {
        super(player);
        this.status = status;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PackStatus getStatus() {
        return status;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
