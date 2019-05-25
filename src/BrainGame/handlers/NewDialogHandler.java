package BrainGame.handlers;

public interface NewDialogHandler {
    class Result {
        private int n_nodes;

        public Result(int n_nodes) {
            this.n_nodes = n_nodes;
        }

        public int getNumberOfNodes() {
            return n_nodes;
        }
    }
    void handle(Result result);
}
