import BrainGame.Brain;
import BrainGame.Search;
import BrainGame.tools.AlreadyConnectedException;

public class TestMain {
    public static void main(String[] args) throws AlreadyConnectedException {

        Brain b = new Brain(5);
        b.connect(0, 1, 3, 4);
        b.connect(1, 2, 1, 5);
        b.connect(1, 3, 4, 5);
        b.connect(1, 4, 2, 1);
        b.connect(2, 4, 5, 2);
        b.connect(3, 4, 7, 1);
//        b.connect(3, 4, 7, 1);

        System.out.println(Search.search(b, 0, 4));
    }
}
