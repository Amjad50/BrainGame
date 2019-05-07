package BrainGame;

import java.util.HashMap;

public class Search {

    public static Path search(Neuron[] graph, Neuron start, Neuron end) {
        boolean[] passed = new boolean[graph.length];
        int[] time = new int[graph.length];
        int[] distance = new int[graph.length];
        int[] prev = new int[graph.length];
//        Initialize all node to not be visited yet
        for (int i = 0; i < graph.length; i++) {
            passed[i] = false;
            distance[i] = time[i] = Integer.MAX_VALUE;
        }
        time[start.id] = distance[start.id] = 0;
        prev[start.id] = -1;
//        Find the shortest path
        for (int cnt = 0; cnt < graph.length; cnt++) {
            int z = minTime(time, passed);

            passed[z] = true;

            for (HashMap.Entry<Neuron, DistanceTimePair> entry : graph[z].children.entrySet()) {
                if (!passed[entry.getKey().id] && time[z] != Integer.MAX_VALUE && time[z] + entry.getValue().getTime() < time[entry.getKey().id]) {
                    time[entry.getKey().id] = time[z] + entry.getValue().getTime();
                    distance[entry.getKey().id] = distance[z] + entry.getValue().getDistance();
                    prev[entry.getKey().id] = z;
                }
            }
        }

        Path path = new Path();
        path.addFirst(end);
        int u = end.id;
        while (prev[u] != -1)
            path.addFirst(graph[(u = prev[u])]);

        path.setDistance(distance[end.id]);
        path.setTime(time[end.id]);

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
