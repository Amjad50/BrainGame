
package rTask;

/**
 *
 * @SudoCode
 */
public class Node<E> {

    E data;
    Node<E> next;
    Node<E> previous;

    public Node(E data) {
        this.data = data;
    }

    public Node() {

    }

    public E getData() {
        return data;
    }

}
