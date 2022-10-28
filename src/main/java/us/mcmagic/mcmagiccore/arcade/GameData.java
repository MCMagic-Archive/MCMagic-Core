package us.mcmagic.mcmagiccore.arcade;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Marc on 8/3/15
 */
public class GameData {
    private UUID uuid;
    private HashMap<String, Integer> ints = new HashMap<>();
    private HashMap<String, String> strings = new HashMap<>();

    public GameData(UUID uuid, HashMap<String, Integer> ints, HashMap<String, String> strings) {
        this.uuid = uuid;
        this.ints = ints;
        this.strings = strings;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public HashMap<String, Integer> getInts() {
        return new HashMap<>(ints);
    }

    public HashMap<String, String> getStrings() {
        return new HashMap<>(strings);
    }
}