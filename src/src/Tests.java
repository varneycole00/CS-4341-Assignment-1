public class Tests {
    public static void main(String[] args) throws Exception {
        Node[][] graph;
        GraphUtils.makeGraph("src/src/board1.txt");
        graph = GraphUtils.getGraph();




        GameState.setInstance();
        Node goal = Node.aStar(graph[4][2], graph[0][10], "zero");
        Node.printPath(goal);

        for(int i = 0 ; i < 5; i ++ ) {
            for( int j = 0 ; j < 11; j++) {
                Node n = graph[i][j];
                if (wasUsedInPath(n.id, goal)) {
                    if(n.parent != null) {
                        if (n.turned2Prev == 1) {
                            System.out.print("T");
                        } else {
                            System.out.print("_");
                        }
                        if (n.robot.getBashed2Prev()) {
                            System.out.print("B");
                        } else {
                            System.out.print("_");
                        }
                    }
                    else {
                        System.out.print("__");
                    }
                    System.out.print("* ");
                } else {
                    double tt = graph[i][j].timeTraveled;
                    if (tt == Double.MAX_VALUE) {
                        System.out.print("MAX ");
                    } else {
                        System.out.print(String.format("%03.0f ", graph[i][j].timeTraveled));

                    }
                }

            }
            System.out.println();
        }


    }

    private static boolean wasUsedInPath(int id, Node node) {
        while (node != null) {
            if (id == node.id)
                return true;
            node = node.parent;
        }
        return false;
    }
}
