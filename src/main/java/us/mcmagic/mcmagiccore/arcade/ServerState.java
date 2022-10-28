package us.mcmagic.mcmagiccore.arcade;

/**
 * Created by Marc on 2/20/15
 */
public enum ServerState {
    ONLINE(new Integer[]{1, 0, 0, 0}), INGAME(new Integer[]{1, 0, 1, 0}), SPECTATING(new Integer[]{1, 0, 0, 1}),
    RESTARTING(new Integer[]{0, 1, 0, 0}), OFFLINE(new Integer[]{0, 0, 0, 0});
    private Integer[] integerArray;

    ServerState(Integer[] integerArray) {
        this.integerArray = integerArray;
    }

    public Integer[] getArray() {
        return integerArray;
    }
}