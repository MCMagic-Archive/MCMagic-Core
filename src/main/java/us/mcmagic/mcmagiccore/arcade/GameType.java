package us.mcmagic.mcmagiccore.arcade;

/**
 * Created by Marc on 2/11/15
 */
public enum GameType {
    TRON(1, "tron", "Tron"), CAPTURETHEFLAG(2, "ctf", "Capture The Flag"), PIXIEDUST(3, "pixie", "Pixie Dust Shootout");
    private int id;
    private String name;
    private String displayName;

    GameType(int id, String name, String displayName) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}