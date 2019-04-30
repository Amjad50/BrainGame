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
        Pair pair = new Pair(distance,time);
        children.put(neuron, pair);
        this.id = id;
    }

}
