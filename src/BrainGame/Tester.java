/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BrainGame;

import BrainGame.tools.AlreadyConnectedException;
import BrainGame.tools.NoConnectionException;
import java.util.Scanner;

/**
 *
 * @author alvin
 */
public class Tester {

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
            if (source > b.getSize() - 1) {
                System.out.println("Entered number is too big ");
                continue;
            }
            dest = s.nextInt();
             if (dest == -1) {
                break;
            }
            if (dest > b.getSize() - 1) {
                System.out.print("Entered number is too big ");
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
//            System.out.print("Insert which node you want to add connections (start from 0, press -1 to quit)");
//            n1 = s.nextInt();
//            if (n1 == -1) {
//                break;
//            }
//            if (n1 > b.getSize() - 1) {
//                System.out.println("Entered number is too big ");
//                continue;
//            } 
//            else {
//                System.out.print("To which node you want " + n1 + " to connect to?");
//                int n2 = s.nextInt();
//                if (n2 > b.getSize() - 1) {
//                    System.out.print("Entered number is too big ");
//                    continue;
//                }
//            if (b.checkConnection(n1, n2)) {
//                System.out.print(n1 + " and " + n2 + " already have a connection , do you want to edit it? (press -1 to decline)");
//                {
//                    int ans = s.nextInt();
//                    if (ans == -1) {
//                        continue;
//                    } else {
//                        edit = true;
//                    }
//                }
//            }
//                System.out.print("Enter the distance from " + n1 + " to " + n2 + ": ");
//                int dist = s.nextInt();
//                System.out.print("Enter the time it takes to go from " + n1 + " to " + n2 + ": ");
//                int time = s.nextInt();
//                if (edit) {
//                    b.editConnection(n1, n2, dist, time);
//                    System.out.println("Connection between " + n1 + " and " + n2 + " has been updated");
//                } else {
//                    b.connect(n1, n2, dist, time);
//
//                    System.out.println("Connection between " + n1 + " and " + n2 + " sucessfully created");
//                }
//
//            }
        }
        source = 0;
        dest = 0;
        while (source != -1 || dest!=-1) {
            System.out.print("Enter the source node and destination ");
            source = s.nextInt();
            if (source == -1) {
                break;
            }
            if (source > b.getSize() - 1) {
                System.out.println("Entered source node is too big ");
                continue;
            }
            dest = s.nextInt();
             if (dest == -1) {
                break;
            }
            if (dest > b.getSize() - 1) {
                System.out.print("Entered destination node is too big ");
                continue;
            }
            System.out.println(Search.search(b, source, dest));
        }

    }
}
