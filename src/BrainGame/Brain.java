package BrainGame;

import BrainGame.tools.AlreadyConnectedException;
import BrainGame.tools.NoConnectionException;

import java.util.HashMap;
import java.util.Objects;

public class Brain {

    private Neuron[] nodes;

    public Brain(int size) {
        nodes = new Neuron[size];
        initGraph();
    }

    private void initGraph() {
        for (int i = 0; i < getSize(); i++) {
            nodes[i] = new Neuron(i);
        }
    }

    /**
     *
     * @param source from 1 to N (size)
     * @param distention from 1 to N (size)
     * @param distance distance of the connection
     * @param time time takes to travel along the path to that connection
     * @throws AlreadyConnectedException if the same connection is already present, even if other numbers
     * check {@link #editConnection} to edit the values of your connection.
     */
    public void connect(int source, int distention, int distance, int time) throws AlreadyConnectedException {
        if(nodes[source].hasConnection(nodes[distention]))
            throw new AlreadyConnectedException(source, distention);

        nodes[source].addConnection(nodes[distention], distance, time);
    }

    public void editConnection(int source, int distention, int distance, int time) throws NoConnectionException {
        if(!nodes[source].hasConnection(nodes[distention]))
            throw new NoConnectionException(source, distention);

        nodes[source].children.replace(nodes[distance], new DistanceTimePair(distance, time));
    }

    public boolean checkConnection(int source, int distention) {
        return nodes[source].hasConnection(nodes[distention]);
    }

    public int getSize() {
        return nodes.length;
    }

    public Neuron getNeuron(int id) {
        Objects.checkIndex(id, nodes.length);
        return nodes[id];
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }

    class Neuron {
        int id;
        HashMap<Neuron, DistanceTimePair> children = new HashMap<>();

        private Neuron(int id) {
            this.id = id;
        }

        // to make other not able to make a new Neuron outside of Brain.
        private Neuron() {}

        private void addConnection(Neuron other, int distance, int time) {
            addConnection(other, distance, time, true);
        }

        private void addConnection(Neuron other, int distance, int time, boolean addToNext) {
            DistanceTimePair pair = new DistanceTimePair(distance, time);
            this.children.put(other, pair);

            if (addToNext) {
                other.addConnection(this, distance, time, false);
            }
        }

        private boolean hasConnection(Neuron neuron) {
            return children.containsKey(neuron);
        }

        @Override
        public String toString() {
            return "Neuron(" + id + ')';
        }
    }
}
