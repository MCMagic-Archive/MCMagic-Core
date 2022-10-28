package us.mcmagic.mcmagiccore.dashboard.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Marc on 9/18/16
 */
public class IncomingPacketEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String packet;

    public IncomingPacketEvent(String packet) {
        this.packet = packet;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getPacket() {
        return packet;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}