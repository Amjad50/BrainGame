package BrainGame.tools;

public class AlreadyConnectedException extends Exception {
    public AlreadyConnectedException(int source, int distention) {
        super(String.format("Neuron(%d) is already connected to Neuron(%d)", source, distention));
    }
}
