public class Tests {
    public static void main(String[] args) throws Exception {
        GameState.setInstance();
        Node[][] graph;
        GraphUtils.makeGraph("src/board1.txt", 0);
        graph = GraphUtils.getGraph();


        Node goal = Node.aStar(graph[4][2], graph[0][10], 0);
        //Node.printPath(goal);

        for(int i = 0 ; i < 5; i ++ ) {
            for( int j = 0 ; j < 11; j++) {
                Node n = graph[i][j];
                if (wasUsedInPath(n.id, goal)) {
                    if(n.parent != null) {
                        if (n.robot.robotDirection != n.parent.robot.robotDirection) {
                            System.out.print("T");
                        } else {
                            System.out.print("_");
                        }
                        if (n.bash) {
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
                        System.out.print(String.format(Integer.toString(graph[i][j].difficulty)) + "\t");

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
