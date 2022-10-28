package us.mcmagic.mcmagiccore.uuidconverter;

import us.mcmagic.mcmagiccore.MCMagicCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDConverter {

    public static String convert(String uuid) {
        try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
            PreparedStatement sql = connection
                    .prepareStatement("SELECT username FROM player_data WHERE uuid=?");
            sql.setString(1, uuid);
            ResultSet result = sql.executeQuery();
            result.next();
            String name = result.getString("username");
            result.close();
            sql.close();
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static UUID convertFromName(String name) {
        try (Connection connection = MCMagicCore.permSqlUtil.getConnection()) {
            PreparedStatement sql = connection.prepareStatement("SELECT uuid FROM player_data WHERE username=?");
            sql.setString(1, name);
            ResultSet result = sql.executeQuery();
            result.next();
            String uuid = result.getString("uuid");
            result.close();
            sql.close();
            return UUID.fromString(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}