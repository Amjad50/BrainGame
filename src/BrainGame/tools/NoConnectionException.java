package BrainGame.tools;

public class NoConnectionException extends Exception {
    public NoConnectionException(int source, int distention) {
        super(String.format("There is no connection between Neuron(%d) and Neuron(%d)", source, distention));
    }
}