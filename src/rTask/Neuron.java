package rTask;

import java.util.HashMap;

/**
 *
 * @SudoCode
 */

public class Neuron {
    int distance, time;
    int id;
    Neuron neuron;
    HashMap<Neuron, Pair> children = new HashMap<>();
    
    public Neuron(Neuron neuron, int id, int distance, int time) {
        this.neuron = neuron;
        this.distance = distance;
        this.time = time;
        Pair<Integer, Integer> pair = Pair.createPair(distance, time);
        pair.getElement0();
        pair.getElement1();
        children.put(neuron, pair);
        this.id = id;
    }

}
