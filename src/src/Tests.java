import java.util.Arrays;

public class Tests {
    public static void main(String[] args) throws Exception {
        Node[][] graph;
        GraphUtils.makeGraph("src/src/sample.txt");
        graph = GraphUtils.getGraph();

        GameState.setInstance();
        Node.aStar(graph[2][2], graph[0][1], "min");
        Node.printPath(graph[0][1]);

    }
}
