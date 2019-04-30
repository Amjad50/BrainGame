package BrainGame;

import java.util.HashMap;

/**
 *
 * @SudoCode
 */

public class Neuron {
    int id;
    Neuron neuron;
    HashMap<Neuron, Pair> children = new HashMap<>();
    
    public Neuron(int id, HashMap children) {
        this.id = id;
        Pair pair = Pair.createPair(distance, time);
        pair.getDistance();
        pair.getTime();
        children.put(neuron, pair);
    }

}
