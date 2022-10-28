package us.mcmagic.mcmagiccore.resource;

import net.minecraft.server.v1_8_R3.PacketPlayInResourcePackStatus;

/**
 * Created by Marc on 3/18/15
 */
public enum PackStatus {
    ACCEPTED, LOADED, FAILED, DECLINED;

    public PacketPlayInResourcePackStatus.EnumResourcePackStatus getNative() {
        switch (this) {
            case ACCEPTED:
                return PacketPlayInResourcePackStatus.EnumResourcePackStatus.ACCEPTED;
            case LOADED:
                return PacketPlayInResourcePackStatus.EnumResourcePackStatus.SUCCESSFULLY_LOADED;
            case FAILED:
                return PacketPlayInResourcePackStatus.EnumResourcePackStatus.FAILED_DOWNLOAD;
            case DECLINED:
                return PacketPlayInResourcePackStatus.EnumResourcePackStatus.DECLINED;
            default:
                return PacketPlayInResourcePackStatus.EnumResourcePackStatus.DECLINED;
        }
    }
}
