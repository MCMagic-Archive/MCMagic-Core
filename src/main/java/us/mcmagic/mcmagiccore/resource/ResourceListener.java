package us.mcmagic.mcmagiccore.resource;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minecraft.server.v1_8_R3.PacketPlayInResourcePackStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.mcmagic.mcmagiccore.MCMagicCore;

import java.lang.reflect.Field;

/**
 * Created by Marc on 3/6/15
 */
public class ResourceListener extends PacketAdapter {

    public ResourceListener(Plugin plugin, PacketType... types) {
        super(plugin, types);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player tp = event.getPlayer();
        try {
            PacketContainer packet = event.getPacket();
            StructureModifier<EnumWrappers.ResourcePackStatus> packStatus = packet.getResourcePackStatus();
            Field field = packStatus.getField(0);
            field.setAccessible(true);
            PacketPlayInResourcePackStatus.EnumResourcePackStatus status =
                    (PacketPlayInResourcePackStatus.EnumResourcePackStatus) field.get(packStatus.getTarget());
            switch (status) {
                case SUCCESSFULLY_LOADED:
                    Bukkit.getPluginManager().callEvent(new ResourceStatusEvent(PackStatus.LOADED, tp));
                    MCMagicCore.resourceManager.downloadingResult(tp.getUniqueId(), PackStatus.LOADED);
                    return;
                case DECLINED:
                    Bukkit.getPluginManager().callEvent(new ResourceStatusEvent(PackStatus.DECLINED, tp));
                    MCMagicCore.resourceManager.downloadingResult(tp.getUniqueId(), PackStatus.DECLINED);
                    return;
                case FAILED_DOWNLOAD:
                    Bukkit.getPluginManager().callEvent(new ResourceStatusEvent(PackStatus.FAILED, tp));
                    MCMagicCore.resourceManager.downloadingResult(tp.getUniqueId(), PackStatus.FAILED);
                    return;
                case ACCEPTED:
                    Bukkit.getPluginManager().callEvent(new ResourceStatusEvent(PackStatus.ACCEPTED, tp));
                    return;
                default:
                    MCMagicCore.resourceManager.downloadingResult(tp.getUniqueId(), null);
                    tp.sendMessage(ChatColor.RED + "There seems to be an Error, please report this to a Staff Member! (Error Code 100)");
            }
        } catch (Exception e) {
            tp.sendMessage(ChatColor.RED + "There seems to be an Error, please report this to a Staff Member! (Error Code 100)");
            e.printStackTrace();
        }
    }
}
