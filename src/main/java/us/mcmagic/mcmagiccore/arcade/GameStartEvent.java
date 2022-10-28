package us.mcmagic.mcmagiccore.arcade;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Marc on 8/2/15
 */
public class GameStartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}