package BrainGame;

import java.util.HashMap;

public class Search {

    public static Path search(Brain brain, int start, int end) {
        boolean[] passed = new boolean[brain.getSize()];
        int[] time = new int[brain.getSize()];
        int[] distance = new int[brain.getSize()];
        int[] prev = new int[brain.getSize()];

//        Initialize all node to not be visited yet
        for (int i = 0; i < brain.getSize(); i++) {
            passed[i] = false;
            distance[i] = time[i] = Integer.MAX_VALUE;
            prev[i] = -1;
        }

        time[start] = distance[start] = 0;
        prev[start] = -1;

//        Find the shortest path
        for (int cnt = 0; cnt < brain.getSize(); cnt++) {
            int z = minTime(time, passed);

            passed[z] = true;

            for (HashMap.Entry<Brain.Neuron, DistanceTimePair> entry : brain.getNeuron(z).children.entrySet()) {
                if (!passed[entry.getKey().id] && time[z] != Integer.MAX_VALUE && distance[z] != Integer.MAX_VALUE) {
                    int newTime = time[z] + entry.getValue().getTime(),
                            newDistance = distance[z] + entry.getValue().getDistance();
                    if ((newTime < time[entry.getKey().id]) ||
                            (newTime == time[entry.getKey().id] && newDistance < distance[entry.getKey().id])) {
                        time[entry.getKey().id] = newTime;
                        distance[entry.getKey().id] = newDistance;
                        prev[entry.getKey().id] = z;
                    }
                }
            }
        }

        Path path = new Path();
        int u = end;
        if (prev[u] == -1)
            return path;

        path.addFirst(brain.getNeuron(end));
        while (prev[u] != -1)
            path.addFirst(brain.getNeuron((u = prev[u])));

        path.setDistance(distance[end]);
        path.setTime(time[end]);

        return path;
    }

    private static int minTime(int[] time, boolean[] passed) {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < passed.length; v++) {
            if (!passed[v] && time[v] <= min) {
                min = time[v];
                min_index = v;
            }
        }

        return min_index;
    }
}
