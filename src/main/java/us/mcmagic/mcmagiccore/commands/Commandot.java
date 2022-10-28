package us.mcmagic.mcmagiccore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.mcmagic.mcmagiccore.MCMagicCore;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Marc on 4/15/15
 */
public class Commandot implements CommandExecutor {

    private static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return "Now";
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        StringBuilder sb = new StringBuilder();
        int[] types = {1, 2, 5, 11, 12, 13};

        String[] names = {"Years", "Years", "Months", "Months", "Days",
                "Days", "Hours", "Hours", "Minutes", "Minutes", "Seconds",
                "Seconds"};

        int accuracy = 0;
        for (int i = 0; i < types.length; i++) {
            if (accuracy > 2) {
                break;
            }
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                accuracy++;
                sb.append(" ").append(diff).append(" ").append(names[(i * 2)]);
            }
        }
        if (sb.length() == 0) {
            return "Now";
        }
        return sb.toString().trim();
    }

    private static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future) && (!fromDate.after(toDate)) || (!future)
                && (!fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date(MCMagicCore.getStartTime()));
        String date = formatDateDiff(c, new GregorianCalendar());
        sender.sendMessage(ChatColor.GREEN + "This server " + ChatColor.AQUA + "(" +
                MCMagicCore.getMCMagicConfig().serverName + ") " + ChatColor.GREEN + "has been online for " + date + ".");
        return true;
    }
}
