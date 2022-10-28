package us.mcmagic.mcmagiccore.achievements;

/**
 * Created by Marc on 6/26/16
 */
public class MagicAchievement {
    private int id;
    private String displayName;
    private String description;

    public MagicAchievement(int id, String displayName, String description) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    protected void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    protected void setDescription(String description) {
        this.description = description;
    }
}