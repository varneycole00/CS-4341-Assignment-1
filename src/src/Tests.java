import java.util.Arrays;

public class Tests {
    public static void main(String[] args) throws Exception {
        Node[][] graph;
        GraphUtils.makeGraph("src/src/board1.txt");
        graph = GraphUtils.getGraph();
        System.out.println(Arrays.toString(graph[0]));
        System.out.println(Arrays.toString(graph[1]));




        GameState.setInstance();
        Node.printPath(Node.aStar(graph[4][2], graph[0][10], "zero"));

        for(int i = 0 ; i < 5; i ++ ) {
            for( int j = 0 ; j < 11; j++) {
                System.out.print(graph[i][j].timeTraveled + " ");
            }
            System.out.println();
        }


    }
}
