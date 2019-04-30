package BrainGame;

/**
 *
 * @SudoCode
 */

public class Pair<Integer> {

    private final Integer distance;
    private final Integer time;

    public static <Integer> Pair<Integer> createPair(Integer distance, Integer time) {
        return new Pair<Integer>(distance, time);
    }

    public Pair(Integer distance, Integer time) {
        this.distance = distance;
        this.time = time;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getTime() {
        return time;
    }

}

