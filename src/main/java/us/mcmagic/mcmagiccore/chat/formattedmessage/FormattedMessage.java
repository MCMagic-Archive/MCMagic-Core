package us.mcmagic.mcmagiccore.chat.formattedmessage;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

public class FormattedMessage {

    private final List<MessageSection> messageSections;

    public FormattedMessage(final String firstPartText) {
        messageSections = new ArrayList<>();
        messageSections.add(new MessageSection(firstPartText));
    }

    public FormattedMessage color(final ChatColor color) {
        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color");
        }
        latest().color = color;
        return this;
    }

    public FormattedMessage style(final ChatColor... styles) {
        for (final ChatColor style : styles) {
            if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }
        latest().styles = styles;
        return this;
    }

    public FormattedMessage file(final String path) {
        onClick("open_file", path);
        return this;
    }

    public FormattedMessage link(final String url) {
        onClick("open_url", url);
        return this;
    }

    public FormattedMessage suggest(final String command) {
        onClick("suggest_command", command);
        return this;
    }

    public FormattedMessage command(final String command) {
        onClick("run_command", command);
        return this;
    }

    public FormattedMessage achievementTooltip(final String name) {
        onHover("show_achievement", "achievement." + name);
        return this;
    }

    public FormattedMessage itemTooltip(final String itemJSON) {
        onHover("show_item", itemJSON);
        return this;
    }

    public FormattedMessage itemTooltip(final ItemStack itemStack) {
        return itemTooltip(CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).toString());
    }

    public FormattedMessage tooltip(final String text) {
        final String[] lines = text.split("\\n");
        if (lines.length <= 1) {
            onHover("show_text", text);
        } else {
            itemTooltip(makeMultilineTooltip(lines));
        }
        return this;
    }

    public FormattedMessage then(final Object obj) {
        messageSections.add(new MessageSection(obj.toString()));
        return this;
    }

    public String toJSONString() {
        final JSONStringer json = new JSONStringer();
        try {
            if (messageSections.size() == 1) {
                latest().writeJson(json);
            } else {
                json.object().key("text").value("").key("extra").array();
                for (final MessageSection part : messageSections) {
                    part.writeJson(json);
                }
                json.endArray().endObject();
            }
        } catch (final JSONException e) {
            throw new RuntimeException("invalid message");
        }
        return json.toString();
    }

    public void send(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(toJSONString())));
    }

    private MessageSection latest() {
        return messageSections.get(messageSections.size() - 1);
    }

    private String makeMultilineTooltip(final String[] lines) {
        final JSONStringer json = new JSONStringer();
        try {
            json.object().key("id").value(1);
            json.key("tag").object().key("display").object();
            json.key("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
            json.key("Lore").array();
            for (int i = 1; i < lines.length; i++) {
                final String line = lines[i];
                json.value(line.isEmpty() ? " " : line.replace("\"", "\\\""));
            }
            json.endArray().endObject().endObject().endObject();
        } catch (final JSONException e) {
            throw new RuntimeException("invalid tooltip");
        }
        return json.toString();
    }

    private void onClick(final String name, final String data) {
        final MessageSection latest = latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
    }

    private void onHover(final String name, final String data) {
        final MessageSection latest = latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
    }

}
