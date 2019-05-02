package BrainGame;

import java.util.HashMap;

public class Neuron {

    int id;
    HashMap<Neuron, DistanceTimePair> children = new HashMap<>();

    public Neuron(int id) {
        this.id = id;
    }

    public void addConnection(Neuron other, int distance, int time) {
        addConnection(other, distance, time, true);
    }

    private void addConnection(Neuron other, int distance, int time, boolean addToNext) {
        DistanceTimePair pair = new DistanceTimePair(distance, time);
        this.children.put(other, pair);

        if (addToNext) {
            other.addConnection(this, distance, time, false);
        }
    }
}
