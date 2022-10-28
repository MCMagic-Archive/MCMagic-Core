package us.mcmagic.mcmagiccore.title;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.title.events.TabTitleChangeEvent;

import java.lang.reflect.Field;

public class TabTitleObject {

    private String rawHeader;
    private String rawFooter;

    private IChatBaseComponent header;
    private IChatBaseComponent footer;

    public TabTitleObject(String title, Position position) {
        if (position == Position.HEADER) {
            rawHeader = title;
            header = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(title));
        } else if (position == Position.FOOTER) {
            rawFooter = title;
            footer = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(title));
        }
    }

    public TabTitleObject(String header, String footer) {
        this.header = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(header));
        this.footer = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(footer));
        rawHeader = header;
        rawFooter = footer;
    }

    public void send(Player player) {
        final TabTitleChangeEvent event = new TabTitleChangeEvent(player, this);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        CraftPlayer cplayer = (CraftPlayer) player;
        PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(header);

        try {
            Field field = headerPacket.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(headerPacket, footer);
        } catch (Exception events) {
            events.printStackTrace();
        } finally {
            cplayer.getHandle().playerConnection.sendPacket(headerPacket);
        }
        cplayer.getHandle().playerConnection.sendPacket(headerPacket);
    }

    public String getHeader() {
        return rawHeader;
    }

    public TabTitleObject setHeader(String header) {
        rawHeader = header;
        this.header = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(header));
        return this;
    }

    public String getFooter() {
        return rawFooter;
    }

    public TabTitleObject setFooter(String footer) {
        rawFooter = footer;
        this.footer = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(footer));
        return this;
    }

    public enum Position {
        HEADER, FOOTER
    }
}
