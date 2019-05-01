package BrainGame;

import java.util.HashMap;


public class Neuron {
    int id;
    HashMap<Neuron, DistanceTimePair> children = new HashMap<>();
    
    public Neuron(int id) {
        this.id = id;
    }

    public void addConnection(Neuron other, int distance, int time) {
        DistanceTimePair pair = new DistanceTimePair(distance, time);
        this.children.put(other, pair);
    }
}
