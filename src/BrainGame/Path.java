package BrainGame;

import java.util.LinkedList;

public class Path {

    LinkedList<Neuron> path = new LinkedList<>();
    int Distance, Time;

    public Path() {
        this.Distance = 0;
        this.Time = 0;
    }

    public Path(int Distance, int Time) {
        this.Distance = Distance;
        this.Time = Time;
    }

    public void add(Neuron e) {
        path.add(e);
    }

    public void addTime(int Time) {
        this.Time = Time;
    }

    public void addDistance(int Distance) {
        this.Distance = Distance;
    }

    public LinkedList<Neuron> getPath() {
        return path;
    }

    public void setPath(LinkedList<Neuron> path) {
        this.path = path;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int Distance) {
        this.Distance = Distance;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }

    public Neuron getFirst() {
        return path.getFirst();
    }

    public Neuron getLast() {
        return path.getLast();
    }
}
