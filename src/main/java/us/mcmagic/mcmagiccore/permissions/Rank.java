package us.mcmagic.mcmagiccore.permissions;

import org.bukkit.ChatColor;
import us.mcmagic.mcmagiccore.MCMagicCore;

import java.util.HashMap;

public enum Rank {
    OWNER("Owner", ChatColor.GOLD, ChatColor.YELLOW, true, 10),
    MAYOR("Mayor", ChatColor.GOLD, ChatColor.YELLOW, true, 10),
    MANAGER("Manager", ChatColor.GOLD, ChatColor.YELLOW, true, 10),
    DEVELOPER("Developer", ChatColor.YELLOW, ChatColor.YELLOW, true, 10),
    COORDINATOR("Coordinator", ChatColor.GREEN, ChatColor.GREEN, true, 9),
    CASTMEMBER("Cast Member", ChatColor.GREEN, ChatColor.GREEN, true, 8),
    EARNINGMYEARS("Earning My Ears", ChatColor.GREEN, ChatColor.GREEN, false, 7),
    CHARACTER("Character", ChatColor.BLUE, ChatColor.BLUE, false, 6),
    SPECIALGUEST("Special Guest", ChatColor.DARK_PURPLE, ChatColor.WHITE, false, 4),
    MCPROHOSTING("MCProHosting", ChatColor.RED, ChatColor.WHITE, false, 4),
    MINEDISNEY("MineDisney", ChatColor.DARK_PURPLE, ChatColor.WHITE, false, 4),
    CRAFTVENTURE("Craftventure", ChatColor.DARK_PURPLE, ChatColor.WHITE, false, 4),
    MAGICALDREAMS("MagicalDreams", ChatColor.DARK_PURPLE, ChatColor.WHITE, false, 4),
    ADVENTURERIDGE("AdventureRidge", ChatColor.DARK_PURPLE, ChatColor.WHITE, false, 4),
    ANCHORNETWORK("AnchorNetwork", ChatColor.DARK_PURPLE, ChatColor.WHITE, false, 4),
    SHAREHOLDER("Shareholder", ChatColor.LIGHT_PURPLE, ChatColor.WHITE, false, 3),
    DVCMEMBER("DVC", ChatColor.AQUA, ChatColor.WHITE, false, 2),
    GUEST("Guest", ChatColor.DARK_AQUA, ChatColor.WHITE, false, 1);

    public String name;
    public ChatColor tagColor;
    public ChatColor chatColor;
    public boolean op;
    public int rankId;

    Rank(String name, ChatColor tagColor, ChatColor chatColor, boolean op, int rankId) {
        this.name = name;
        this.tagColor = tagColor;
        this.chatColor = chatColor;
        this.op = op;
        this.rankId = rankId;
    }

    public static Rank fromString(String string) {
        String rankName = string.toLowerCase();
        switch (rankName) {
            case "owner":
                return OWNER;
            case "mayor":
                return MAYOR;
            case "manager":
                return MANAGER;
            case "developer":
                return DEVELOPER;
            case "technician":
                return DEVELOPER;
            case "moderator":
                return CASTMEMBER;
            case "coordinator":
                return COORDINATOR;
            case "castmember":
                return CASTMEMBER;
            case "earningmyears":
                return EARNINGMYEARS;
            case "character":
                return CHARACTER;
            case "specialguest":
                return SPECIALGUEST;
            case "mcprohosting":
                return MCPROHOSTING;
            case "craftventure":
                return CRAFTVENTURE;
            case "minedisney":
                return MINEDISNEY;
            case "magicaldreams":
                return MAGICALDREAMS;
            case "adventureridge":
                return ADVENTURERIDGE;
            case "anchornetwork":
                return ANCHORNETWORK;
            case "shareholder":
                return SHAREHOLDER;
            case "dvc":
                return DVCMEMBER;
            case "donor":
                return DVCMEMBER;
            case "dvcmember":
                return DVCMEMBER;
            case "newplayer":
                return GUEST;
            case "guest":
                return GUEST;
            default:
                return GUEST;
        }
    }

    public int getRankId() {
        return rankId;
    }

    public String getName() {
        return name;
    }

    public String getSqlName() {
        return name.toLowerCase().replaceAll(" ", "");
    }

    public String getNameWithBrackets() {
        return ChatColor.WHITE + "[" + getTagColor() + getName() + ChatColor.WHITE + "]";
    }

    public boolean getOp() {
        return op;
    }

    public HashMap<String, Boolean> getPermissions() {
        switch (this) {
            case OWNER:
                return MCMagicCore.permManager.ownerPerms;
            case MAYOR:
                return MCMagicCore.permManager.mayorPerms;
            case MANAGER:
                return MCMagicCore.permManager.managerPerms;
            case DEVELOPER:
                return MCMagicCore.permManager.devPerms;
            case COORDINATOR:
                return MCMagicCore.permManager.coordPerms;
            case CASTMEMBER:
                return MCMagicCore.permManager.cmPerms;
            case EARNINGMYEARS:
                return MCMagicCore.permManager.emePerms;
            case CHARACTER:
                return MCMagicCore.permManager.charPerms;
            case SPECIALGUEST:
                return MCMagicCore.permManager.sgPerms;
            case MCPROHOSTING:
                return MCMagicCore.permManager.sgPerms;
            case MINEDISNEY:
                return MCMagicCore.permManager.sgPerms;
            case CRAFTVENTURE:
                return MCMagicCore.permManager.sgPerms;
            case MAGICALDREAMS:
                return MCMagicCore.permManager.sgPerms;
            case ADVENTURERIDGE:
                return MCMagicCore.permManager.sgPerms;
            case ANCHORNETWORK:
                return MCMagicCore.permManager.sgPerms;
            case SHAREHOLDER:
                return MCMagicCore.permManager.dvcPerms;
            case DVCMEMBER:
                return MCMagicCore.permManager.dvcPerms;
            case GUEST:
                return MCMagicCore.permManager.guestPerms;
            default:
                return MCMagicCore.permManager.guestPerms;
        }
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public ChatColor getTagColor() {
        return tagColor;
    }
}
