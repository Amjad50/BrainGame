package rTask;

import java.util.HashMap;
import javafx.util.Pair;

/**
 *
 * @Sudo
 */

public class Point {
    int distance, time;
    int id;
    Node node;
    HashMap<Node, Pair> children = new HashMap<>();
    
    public Point(Node node, int id, int distance, int time) {
        this.node = node;
        this.distance = distance;
        this.time = time;
        Pair pair = new Pair(distance,time);
        children.put(node, pair);
        this.id = id;
    }

}
