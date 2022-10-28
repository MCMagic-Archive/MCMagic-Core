package us.mcmagic.mcmagiccore.title;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.title.events.TitleEvent;

public class TitleObject {

    private String rawTitle;
    private String rawSubtitle;

    private IChatBaseComponent title;
    private IChatBaseComponent subtitle;

    private int fadeIn = -1;
    private int stay = -1;
    private int fadeOut = -1;

    public TitleObject(String title, TitleType type) {
        IChatBaseComponent serializedTitle = IChatBaseComponent.ChatSerializer.a(TextConverter
                .convert(title));
        if (type == TitleType.TITLE) {
            rawTitle = title;
            this.title = serializedTitle;
        } else if (type == TitleType.SUBTITLE) {
            rawSubtitle = title;
            subtitle = serializedTitle;
        }
    }

    public TitleObject(String title, String subtitle) {
        rawTitle = title;
        rawSubtitle = subtitle;
        this.title = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(title));
        this.subtitle = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(subtitle));
    }

    public void send(Player player) {
        final TitleEvent event = new TitleEvent(player, this);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        PlayerConnection connection = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection;
        Packet packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packet);
        if (title != null) {
            PacketPlayOutTitle main = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, title);
            connection.sendPacket(main);
        }
        if (subtitle != null) {
            PacketPlayOutTitle sub = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitle);
            connection.sendPacket(sub);
        }
    }

    public String getTitle() {
        return rawTitle;
    }

    public TitleObject setTitle(String title) {
        rawTitle = title;
        this.title = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(title));
        return this;
    }

    public String getSubtitle() {
        return rawSubtitle;
    }

    public TitleObject setSubtitle(String subtitle) {
        rawSubtitle = subtitle;
        this.subtitle = IChatBaseComponent.ChatSerializer.a(TextConverter.convert(subtitle));
        return this;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public TitleObject setFadeIn(int i) {
        fadeIn = i;
        return this;
    }

    public int getStay() {
        return stay;
    }

    public TitleObject setStay(int i) {
        stay = i;
        return this;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public TitleObject setFadeOut(int i) {
        fadeOut = i;
        return this;
    }

    public enum TitleType {
        TITLE, SUBTITLE
    }
}
