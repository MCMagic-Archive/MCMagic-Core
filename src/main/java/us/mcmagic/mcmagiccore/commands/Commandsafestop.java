package us.mcmagic.mcmagiccore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.dashboard.packets.dashboard.PacketEmptyServer;

/**
 * Created by Marc on 5/2/15
 */
public class Commandsafestop implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String reason;
        if (args.length > 0) {
            reason = "";
            for (String s : args) {
                reason += s + " ";
            }
        } else {
            reason = Bukkit.getShutdownMessage();
        }
        sender.sendMessage(ChatColor.RED + "Shutting server...");
        for (World world : Bukkit.getWorlds()) {
            world.save();
        }
        MCMagicCore.serverStarting = true;
        PacketEmptyServer packet = new PacketEmptyServer(MCMagicCore.getMCMagicConfig().instanceName);
        MCMagicCore.dashboardConnection.send(packet);
        Bukkit.getScheduler().runTaskTimer(MCMagicCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() <= 0) {
                    MCMagicCore.dashboardConnection.stop();
                    Bukkit.shutdown();
                }
            }
        }, 0L, 40L);
        return true;
    }
}