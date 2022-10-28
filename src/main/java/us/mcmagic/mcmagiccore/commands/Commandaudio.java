package us.mcmagic.mcmagiccore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.audioserver.AudioArea;
import us.mcmagic.mcmagiccore.audioserver.AudioServer;
import us.mcmagic.mcmagiccore.audioserver.PacketHelper;
import us.mcmagic.mcmagiccore.dashboard.packets.audio.PacketPlayOnceGlobal;
import us.mcmagic.mcmagiccore.dashboard.packets.audio.PacketServerSwitch;
import us.mcmagic.mcmagiccore.permissions.Rank;

import java.util.List;

/**
 * Created by Marc on 6/15/15
 */
public class Commandaudio implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        List<AudioArea> areas = MCMagicCore.audioServer.getAudioAreas();
        Rank rank = sender instanceof Player ? MCMagicCore.getUser(((Player) sender).getUniqueId()).getRank() : Rank.DEVELOPER;
        if (args.length < 1) {
            return true;
        }
        if (args[0].equalsIgnoreCase("loginsync")) {
            Player player = (Player) sender;
            for (AudioArea area : areas) {
                area.removePlayer(player.getUniqueId());
                area.addPlayerIfInside(player);
            }
            return true;
        }
        if (rank.getRankId() < Rank.CASTMEMBER.getRankId()) {
            return true;
        }
        if (args[0].equalsIgnoreCase("enable")) {
            if (args.length >= 2) {
                if (areas.size() > 0) {
                    for (AudioArea area : areas) {
                        if (area.getAreaName().equalsIgnoreCase(args[1])) {
                            area.setEnabled(true);
                            sender.sendMessage("§2Area state enabled!");
                            return true;
                        }
                    }
                    sender.sendMessage("§4The area [§c" + args[1] + "§4] was not found!");
                } else {
                    sender.sendMessage("§4There are no audio areas yet!");
                }
            } else sender.sendMessage("§4Usage: /audio enable <areaname>");

        } else if (args[0].equalsIgnoreCase("disable")) {
            if (args.length >= 2) {
                if (areas.size() > 0) {
                    for (AudioArea area : areas) {
                        if (area.getAreaName().equalsIgnoreCase(args[1])) {
                            area.setEnabled(false);
                            area.removeAllPlayers(true);
                            sender.sendMessage("§2Area state disabled!");
                            return true;
                        }
                    }
                    sender.sendMessage("§4The area [" + args[1] + "] was not found!");
                } else {
                    sender.sendMessage("§4There are no audio areas yet!");
                }
            } else sender.sendMessage("§4Usage: /audio disable <areaname>");

        } else if (args[0].equalsIgnoreCase("disableforplayer")) {
            if (args.length >= 3) {
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    sender.sendMessage("That player is not online");
                    return true;
                }
                if (areas.size() > 0) {
                    for (AudioArea area : areas) {
                        if (area.getAreaName().equalsIgnoreCase(args[1])) {
                            area.disableToPlayer(player);
                            return true;
                        }
                    }
                    sender.sendMessage("§4The area [" + args[1] + "] was not found!");
                } else {
                    sender.sendMessage("§4There are no audio areas yet!");
                }
            } else {
                sender.sendMessage("§4Usage: /audio disableforplayer <areaname> <player>");
            }
        } else if (args[0].equalsIgnoreCase("syncareatoplayer")) {
            if (args.length >= 4) {
                Player player = Bukkit.getPlayer(args[3]);
                if (player != null) {
                    if (areas.size() > 0) {
                        for (AudioArea area : areas) {
                            if (area.getAreaName().equalsIgnoreCase(args[1])) {
                                if (args.length >= 5) {
                                    double margin = Double.parseDouble(args[4]);
                                    area.sync(MCMagicCore.audioServer.getFloatFromString(args[2], 0.0F), player, margin);
                                } else {
                                    area.sync(MCMagicCore.audioServer.getFloatFromString(args[2], 0.0F), player);
                                }

                                return true;
                            }
                        }
                        sender.sendMessage("§4The area [" + args[1] + "] was not found!");
                    } else {
                        sender.sendMessage("§4There are no audio areas yet!");
                    }
                } else sender.sendMessage("§4That player wasn't found");
            } else {
                sender.sendMessage("§4Usage: /audio syncareatoplayer <areaname> <seconds> <player> [margin]");
            }
        } else if (args[0].equalsIgnoreCase("clearall")) {
            if (args.length >= 2) {
                float radius = MCMagicCore.audioServer.getFloatFromString(args[1], 1.0F);
                Location loc = null;
                if (sender instanceof BlockCommandSender) {
                    loc = ((BlockCommandSender) sender).getBlock().getLocation();
                } else if (sender instanceof Player) {
                    loc = ((Player) sender).getLocation();
                }
                for (Player plr : Bukkit.getOnlinePlayers()) {
                    if (plr.getLocation().distanceSquared(loc) <= radius * radius) {
                        PacketHelper.sendToPlayer(new PacketServerSwitch(MCMagicCore.getMCMagicConfig().serverName), plr);
                    }
                }
            } else {
                sender.sendMessage("§4Usage: /audio clearall <radius>");
            }
        } else if (args[0].equalsIgnoreCase("syncarea")) {
            if (args.length >= 3) {
                if (areas.size() > 0) {
                    for (AudioArea area : areas) {
                        if (area.getAreaName().equalsIgnoreCase(args[1])) {
                            if (args.length >= 4) {
                                float radius = MCMagicCore.audioServer.getFloatFromString(args[1], 1.0F);
                                Location loc = null;
                                if ((sender instanceof BlockCommandSender)) {
                                    loc = ((BlockCommandSender) sender).getBlock().getLocation();
                                } else if ((sender instanceof Player)) {
                                    loc = ((Player) sender).getLocation();
                                }

                                if (loc != null) {
                                    for (Player plr : Bukkit.getOnlinePlayers()) {
                                        if (plr.getLocation().distanceSquared(loc) <= radius * radius) {
                                            area.sync(MCMagicCore.audioServer.getFloatFromString(args[2], 0.0F), plr);
                                        }
                                    }
                                } else {
                                    sender.sendMessage("§4You don't seem to be able to perfom this command using a radius");
                                }
                            } else {
                                area.sync(MCMagicCore.audioServer.getFloatFromString(args[2], 0.0F));
                            }
                            return true;
                        }
                    }
                    sender.sendMessage("§4The area [" + args[1] + "] was not found!");
                } else {
                    sender.sendMessage("§4There are no audio areas yet!");
                }
            } else sender.sendMessage("§4Usage: /audio syncarea <areaname> <seconds>");

        } else if (args[0].equalsIgnoreCase("defaultenable")) {
            if (args.length >= 2) {
                if (areas.size() > 0) {
                    for (AudioArea area : areas) {
                        if (area.getAreaName().equalsIgnoreCase(args[1])) {
                            area.setDefaultState(true);
                            sender.sendMessage("§2Area state enabled!");
                            return true;
                        }
                    }
                    sender.sendMessage("§4The area [§c" + args[1] + "§4] was not found!");
                } else {
                    sender.sendMessage("§4There are no audio areas yet!");
                }
            } else sender.sendMessage("§4Usage: /audio defaultenable <areaname>");

        } else if (args[0].equalsIgnoreCase("defaultdisable")) {
            if (args.length >= 2) {
                if (areas.size() > 0) {
                    for (AudioArea area : areas) {
                        if (area.getAreaName().equalsIgnoreCase(args[1])) {
                            area.setDefaultState(false);
                            sender.sendMessage("§2Area state disabled!");
                            return true;
                        }
                    }
                    sender.sendMessage("§4The area [" + args[1] + "] was not found!");
                } else {
                    sender.sendMessage("§4There are no audio areas yet!");
                }
            } else sender.sendMessage("§4Usage: /audio defaultdisable <areaname>");

        } else if (args[0].equalsIgnoreCase("triggerarea")) {
            if (args.length >= 3) {
                if (Bukkit.getPlayer(args[2]) != null) {
                    if (areas.size() > 0) {
                        for (AudioArea area : areas) {
                            if (area.getAreaName().equalsIgnoreCase(args[1])) {
                                area.triggerPlayer(Bukkit.getPlayer(args[2]));

                                return true;
                            }
                        }
                        sender.sendMessage("§4The area [" + args[1] + "] was not found!");
                    } else {
                        sender.sendMessage("§4There are no audio areas yet!");
                    }
                } else sender.sendMessage("§4Couldn't find that player!");
            } else {
                sender.sendMessage("§4Usage: /audio triggerarea <areaname> <player>");
            }
        } else if (args[0].equalsIgnoreCase("play")) {
            if (args.length >= 4) {
                Location loc;
                String name;
                double volume;
                double radius;
                if (((sender instanceof BlockCommandSender)) || ((sender instanceof Player))) {
                    loc = (sender instanceof Player) ? ((Player) sender).getLocation() : ((BlockCommandSender) sender).getBlock().getLocation().add(0.5D, 0.0D, 0.5D);

                    name = args[1];
                    volume = MCMagicCore.audioServer.getFloatFromString(args[2], 5.0F);

                    radius = MCMagicCore.audioServer.getFloatFromString(args[3], 5.0F);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getLocation().distanceSquared(loc) < radius * radius) {
                            PacketHelper.sendToPlayer(new PacketPlayOnceGlobal(AudioServer.getAudioid(), name, (float) volume), player);
                        }
                    }
                } else {
                    sender.sendMessage("§4This command must be executed by either a player or commandblock");
                }
            } else {
                sender.sendMessage("§4Usage: /audio play <audioname> <volume> <radius>");
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (!sender.hasPermission("audioserver.list")) {
                sender.sendMessage("§4You don't own the right permissions to do this");
            } else if (areas.size() > 0) {
                sender.sendMessage("§3[Audio areas]");
                for (AudioArea area : areas) {
                    sender.sendMessage((area.isEnabled() ? "§a" : "§c") + area.getAreaName() + " §7- §b" + area.getSoundName() + (area.getRepeat() ? " §a► " : " §c► ") + "§aType: §b" + AudioArea.getType(area.getVehicleType()));
                }

            } else {
                sender.sendMessage("§4There are no audio areas yet!");
            }
        } else if (args[0].equalsIgnoreCase("setoptions")) {
            if (!sender.hasPermission("audioserver.setoptions")) {
                sender.sendMessage("§4You don't own the right permissions to do this");
            } else if (args.length >= 3) {
                String name = args[1];
                for (AudioArea area : areas) {
                    if (area.getAreaName().equalsIgnoreCase(name)) {
                        for (int i = 2; i < args.length; i++) {
                            sender.sendMessage(area.setOption(args[i]));
                        }

                        return true;
                    }
                }
                sender.sendMessage("§4Area §3" + name + "§4 wasn't found");
            } else {
                sender.sendMessage("§4Usage: /audio setoptions <areaname> [option:value] [...]");
            }
        } else if (args[0].equalsIgnoreCase("getoptions")) {
            if (!sender.hasPermission("audioserver.getoptions")) {
                sender.sendMessage("§4You don't own the right permissions to do this");
            } else if (args.length >= 2) {
                String name = args[1];
                for (AudioArea area : areas) {
                    if (area.getAreaName().equalsIgnoreCase(name)) {
                        area.sendOptions(sender);
                        return true;
                    }
                }
                sender.sendMessage("§4Area §3" + name + "§4 wasn't found");
            } else {
                sender.sendMessage("§4Usage: /audio getoptions <areaname>");
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!sender.hasPermission("audioserver.remove")) {
                sender.sendMessage("§4You don't own the right permissions to do this");
            } else if (args.length >= 2) {
                String name = args[1];
                for (AudioArea area : areas) {
                    if (area.getAreaName().equalsIgnoreCase(name)) {
                        MCMagicCore.audioServer.getAreaConfigFile().set("areas." + area.getAreaName(), null);

                        areas.remove(area);
                        sender.sendMessage("§aArea " + name + " succesfully removed!");
                        return true;
                    }
                }
                sender.sendMessage("§4Area §3" + name + "§4 wasn't found");
            } else {
                sender.sendMessage("§4Usage: /audio remove <areaname>");
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            MCMagicCore.audioServer.reloadAreas();
            sender.sendMessage(ChatColor.GREEN + "Areas reloaded!");
            sender.sendMessage(ChatColor.RED + "Be careful when using this command!");
        } else if (args[0].equalsIgnoreCase("save")) {
            MCMagicCore.audioServer.saveAreas();
            sender.sendMessage(ChatColor.GREEN + "Saved!");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Unknown Sub-Command");
        }
        return true;
    }
}