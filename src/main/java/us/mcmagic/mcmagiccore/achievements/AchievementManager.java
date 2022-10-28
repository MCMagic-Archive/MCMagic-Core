package us.mcmagic.mcmagiccore.achievements;

import org.bukkit.Bukkit;
import org.json.JSONArray;
import org.json.JSONObject;
import us.mcmagic.mcmagiccore.MCMagicCore;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Marc on 6/26/16
 */
public class AchievementManager {
    public String url = "https://spreadsheets.google.com/feeds/cells/14OHnSeMJVmtFnE7xIdCzMaE0GPOR3Sh4SyE-ZR3hQ7o/od6/public/basic?alt=json";
    private HashMap<Integer, MagicAchievement> achievements = new HashMap<>();
    private HashMap<UUID, List<Integer>> earned = new HashMap<>();

    public AchievementManager() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(MCMagicCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                reload();
            }
        }, 0L, 6000L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(MCMagicCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
                    if (earned.isEmpty()) {
                        return;
                    }
                    int amount = 0;
                    for (Map.Entry<UUID, List<Integer>> entry : new HashSet<>(earned.entrySet())) {
                        for (Integer i : entry.getValue()) {
                            amount++;
                        }
                    }
                    String statement = "INSERT INTO achievements (uuid, achid, time) VALUES ";
                    int i = 0;
                    HashMap<Integer, String> lastList = new HashMap<>();
                    for (Map.Entry<UUID, List<Integer>> entry : new HashSet<>(earned.entrySet())) {
                        if (entry == null || entry.getKey() == null || entry.getValue() == null) {
                            continue;
                        }
                        for (Integer in : new ArrayList<>(earned.remove(entry.getKey()))) {
                            statement += "(?, ?, ?)";
                            if (((i / 3) + 1) < amount) {
                                statement += ", ";
                            }
                            lastList.put(i += 1, entry.getKey().toString());
                            lastList.put(i += 1, in + "");
                            lastList.put(i += 1, (int) (System.currentTimeMillis() / 1000) + "");
                        }
                    }
                    PreparedStatement sql = connection.prepareStatement(statement + ";");
                    for (Map.Entry<Integer, String> entry : new HashSet<>(lastList.entrySet())) {
                        if (isInt(entry.getValue())) {
                            sql.setInt(entry.getKey(), Integer.parseInt(entry.getValue()));
                        } else {
                            sql.setString(entry.getKey(), entry.getValue());
                        }
                    }
                    sql.execute();
                    sql.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 100L);
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public void reload() {
        JSONObject obj = readJsonFromUrl(url);
        if (obj == null) {
            return;
        }
        JSONArray array = obj.getJSONObject("feed").getJSONArray("entry");
        achievements.clear();
        MagicAchievement lastAch = null;
        for (int i = 0; i < array.length(); i++) {
            JSONObject ob = array.getJSONObject(i);
            JSONObject content = ob.getJSONObject("content");
            JSONObject id = ob.getJSONObject("title");
            String column = id.getString("$t");
            Integer row = Integer.parseInt(column.substring(1, 2));
            switch (column.substring(0, 1).toLowerCase()) {
                case "a":
                    lastAch = new MagicAchievement(content.getInt("$t"), null, null);
                    break;
                case "b":
                    lastAch.setDisplayName(content.getString("$t"));
                    break;
                case "c":
                    lastAch.setDescription(content.getString("$t"));
                    achievements.put(lastAch.getId(), lastAch);
                    break;
            }
        }
    }

    public MagicAchievement getAchievement(int id) {
        return achievements.get(id);
    }

    public List<MagicAchievement> getAchievements() {
        return new ArrayList<>(achievements.values());
    }

    private static JSONObject readJsonFromUrl(String url) {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}