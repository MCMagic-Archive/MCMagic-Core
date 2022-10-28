package us.mcmagic.mcmagiccore.arcade;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Marc on 2/6/16
 */
public class GameCountdownEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private int number;


    public GameCountdownEvent(int number) {
        super();
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}