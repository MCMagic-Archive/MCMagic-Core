package us.mcmagic.mcmagiccore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Marc on 2/24/15
 * Put whatever you want in here so you can
 * test something. Guests don't have permission
 * for this command, so it can be anything.
 */
public class Commandtest implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }
}
