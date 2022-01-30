import java.util.Arrays;

public class Tests {
    public static void main(String[] args) throws Exception {
        Node[][] graph;
        GraphUtils.makeGraph("src/src/sample.txt");
        graph = GraphUtils.getGraph();

        System.out.println(Arrays.deepToString(graph));

//        System.out.println("Neighbors to 0,0 have difficulties: ");
//        for(Node.Edge e : graph[1][1].neighbors) {
//            System.out.print(e.difficulty + ", ");
//        }

        Node.aStar(graph[2][2], graph[1][0], "min");
        Node.printPath(graph[1][0]);

    }
}
