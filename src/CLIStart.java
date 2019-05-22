/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import BrainGame.Brain;
import BrainGame.Path;
import BrainGame.Search;
import BrainGame.tools.AlreadyConnectedException;
import BrainGame.tools.NoConnectionException;
import java.util.Scanner;

/**
 *
 * @author alvin
 */
public class CLIStart {

    public static void main(String[] args) throws AlreadyConnectedException, NoConnectionException {
        Scanner s = new Scanner(System.in);
        System.out.print("How many node do you want to have in your graph? ");
        int size = s.nextInt();
        Brain b = new Brain(size);
        int source = 0;
        int dest = 0;
        boolean edit = false;

        while (source != -1 || dest!=-1) {
            System.out.print("Enter the node you want to connect, along with the destenation and the distance & time to get there(press -1 to quit)");
            source = s.nextInt();
            if (source == -1) {
                break;
            }
            if(source < 0) {
                System.out.println("Entered number is too small");
                continue;
            }
            if (source > b.getSize() - 1) {
                System.out.println("Entered number is too big ");
                continue;
            }
            dest = s.nextInt();
             if (dest == -1) {
                break;
            }
            if(dest < 0) {
                System.out.println("Entered number is too small");
                continue;
            }
            if (dest > b.getSize() - 1) {
                System.out.println("Entered number is too big ");
                continue;
            }
            int dist = s.nextInt();
            int time = s.nextInt();
            if (b.checkConnection(source, dest)) {
                System.out.print(source + " and " + dest + " already have a connection , do you want to edit it? (press -1 to decline)");
                {
                    int ans = s.nextInt();
                    if (ans == -1) {
                        continue;
                    } else {
                        edit = true;
                    }
                }
            }

            if (edit) {
                b.editConnection(source, dest, dist, time);
                System.out.println("Connection between " + source + " and " + dest + " has been updated!");
            } else {
                b.connect(source, dest, dist, time);
                System.out.println("Connection between " + source + " and " + dest + " sucessfully estabblished!");
            }
        }

        System.out.println("Now you can know the distance and time of the best path between two nodes.");
        source = 0;
        dest = 0;
        while (source != -1 || dest!=-1) {
            System.out.print("Enter the source node and destination ");
            source = s.nextInt();
            if (source == -1) {
                break;
            }
            if(source < 0) {
                System.out.println("Entered number is too small");
                continue;
            }
            if (source > b.getSize() - 1) {
                System.out.println("Entered source node is too big ");
                continue;
            }
            dest = s.nextInt();
             if (dest == -1) {
                break;
            }
            if(dest < 0) {
                System.out.println("Entered number is too small");
                continue;
            }
            if (dest > b.getSize() - 1) {
                System.out.println("Entered destination node is too big ");
                continue;
            }
            Path path = Search.search(b, source, dest);

            System.out.printf("The shortest path will take %d seconds, and its total distance is %s\n", path.getTime(), path.getDistance());
            if(path.getPath().size() == 0){
                System.out.printf("There is not path to get from %d to %d\n", source, dest);
            }else {
                System.out.println("The path from start to end is: ");
                for (Brain.Neuron neuron : path.getPath()) {
                    System.out.print(neuron.id + " ");
                }
                System.out.println();
            }
        }

    }
}
