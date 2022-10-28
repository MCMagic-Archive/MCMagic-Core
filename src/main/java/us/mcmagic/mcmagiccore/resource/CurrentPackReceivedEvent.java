package us.mcmagic.mcmagiccore.resource;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.mcmagic.mcmagiccore.player.User;

/**
 * Created by Marc on 4/9/15
 */
public class CurrentPackReceivedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private User user;
    private String pack;

    public CurrentPackReceivedEvent(User user, String pack) {
        this.user = user;
        this.pack = pack;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public User getUser() {
        return user;
    }

    public String getPacks() {
        return pack;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}