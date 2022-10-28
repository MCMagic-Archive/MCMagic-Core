package us.mcmagic.mcmagiccore.dashboard;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import us.mcmagic.mcmagiccore.MCMagicCore;
import us.mcmagic.mcmagiccore.dashboard.events.IncomingPacketEvent;
import us.mcmagic.mcmagiccore.dashboard.packets.BasePacket;
import us.mcmagic.mcmagiccore.dashboard.packets.dashboard.*;
import us.mcmagic.mcmagiccore.player.User;
import us.mcmagic.mcmagiccore.resource.CurrentPackReceivedEvent;
import us.mcmagic.mcmagiccore.scoreboard.ScoreboardUtility;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Created by Marc on 5/22/16
 */
public class DashboardConnection {
    protected WebSocketClient ws;
    private boolean attempted = false;
    private boolean save = true;

    public DashboardConnection() throws URISyntaxException {
        ws = new WebSocketClient(new URI("ws://socket.dashboard.mcmagic.us:7892"), new Draft_10()) {
            @Override
            public void onMessage(String message) {
                JsonObject object = (JsonObject) new JsonParser().parse(message);
                if (!object.has("id")) {
                    return;
                }
                int id = object.get("id").getAsInt();
                System.out.println(object.toString());
                switch (id) {
                    case 41: {
                        PacketOnlineCount packet = new PacketOnlineCount().fromJSON(object);
                        int count = packet.getCount();
                        ScoreboardUtility.setOnlineCount(count);
                        break;
                    }
                    case 49: {
                        PacketGetPack packet = new PacketGetPack().fromJSON(object);
                        UUID uuid = packet.getUniqueId();
                        String pack = packet.getPack();
                        User user = MCMagicCore.getUser(uuid);
                        if (user == null) {
                            return;
                        }
                        user.setResourcePack(pack);
                        CurrentPackReceivedEvent e = new CurrentPackReceivedEvent(user, pack);
                        Bukkit.getPluginManager().callEvent(e);
                        break;
                    }
                    case 50: {
                        PacketMention packet = new PacketMention().fromJSON(object);
                        UUID uuid = packet.getUniqueId();
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null) {
                            return;
                        }
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 50f, 1f);
                        break;
                    }
                    case 51: {
                        PacketAudioConnect packet = new PacketAudioConnect().fromJSON(object);
                        UUID uuid = packet.getUniqueId();
                        User user = MCMagicCore.getUser(uuid);
                        if (user == null) {
                            return;
                        }
                        user.giveAchievement(16);
                        break;
                    }
                }
                IncomingPacketEvent event = new IncomingPacketEvent(object.toString());
                Bukkit.getPluginManager().callEvent(event);
            }

            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("Successfully connected to Dashboard");
                DashboardConnection.this.send(new PacketConnectionType(PacketConnectionType.ConnectionType.INSTANCE).getJSON().toString());
                DashboardConnection.this.send(new PacketServerName(MCMagicCore.getMCMagicConfig().instanceName).getJSON().toString());
                attempted = false;
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (!save) {
                    return;
                }
                System.out.println(code + " Disconnected from Dashboard! Shutting server...");
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
                            Bukkit.shutdown();
                        }
                    }
                }, 0L, 40L);
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("Error in Dashboard connection");
                ex.printStackTrace();
            }

        };
        ws.connect();
    }

    public void send(String s) {
        ws.send(s);
    }

    public boolean isConnected() {
        return ws.getConnection() != null;
    }

    public void stop() {
        save = false;
        ws.close();
    }

    public void send(BasePacket packet) {
        send(packet.getJSON().toString());
    }

    public void playerChat(UUID uuid, String message) {
        PacketPlayerChat packet = new PacketPlayerChat(uuid, message);
        send(packet);
    }

    private String formatName(String s) {
        String ns = "";
        if (s.length() < 4) {
            for (char c : s.toCharArray()) {
                ns += Character.toString(Character.toUpperCase(c));
            }
            return ns;
        }
        Character last = null;
        for (char c : s.toCharArray()) {
            if (last == null) {
                last = c;
                ns += Character.toString(Character.toUpperCase(c));
                continue;
            }
            if (Character.toString(last).equals(" ")) {
                ns += Character.toString(Character.toUpperCase(c));
            } else {
                ns += Character.toString(c);
            }
            last = c;
        }
        return ns;
    }
}