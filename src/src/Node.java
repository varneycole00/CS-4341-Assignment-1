import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Node implements Comparable<Node> {

    public static Node[][] graph = GraphUtils.getGraph();

    private static int idCounter = 0;
    public int id;
    public int difficulty;
    public boolean bash = false;
    public boolean turnedPreviously = false;

    public Node parent = null;
    public List<Edge> neighbors;


    public double AStarEstimate = Double.MAX_VALUE;
    public double timeTraveled = Double.MAX_VALUE;
    public double timeRemainingEstimate;
    public int xPos;
    public int yPos;
    Robot robot;
    public int turned2Prev;

    Node(double h, int difficulty, int xPos, int yPos, Robot robot) {
        this.timeRemainingEstimate = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
        this.difficulty = difficulty;
        this.xPos = xPos;
        this.yPos = yPos;
        this.robot = robot;
        this.turned2Prev = 0;
    }

    Node(double h, int difficulty, int xPos, int yPos) {
        this.timeRemainingEstimate = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
        this.difficulty = difficulty;
        this.xPos = xPos;
        this.yPos = yPos;
        this.turned2Prev = 0;
    }


    @Override
    public int compareTo(Node n) {
        return Double.compare(this.AStarEstimate, n.AStarEstimate);
    }

    public static class Edge {
        Edge(int difficulty, Node node, Direction direction, boolean bash) {
            this.difficulty = difficulty;
            this.node = node;
            this.direction = direction;
            this.bash = bash;
        }
        public int difficulty;
        public Node node;
        public Direction direction;
        public boolean bash;
    }

    public void addBranch(int difficulty, Node node, Direction direction, boolean bash) {
        Edge newEdge = new Edge(difficulty, node, direction, bash);
        neighbors.add(newEdge);
    }

    /* Todo: adjust this method to take in a flag and calculate heuristics differently
        depending on the A* mode) */
    public double calculateHeuristic(Node target, int mode) throws Exception {
        switch(mode) {

            case 1:
                // Mode 'zero' where always zero
                return 0;
            case 2:
                // Mode 'min' (vertical, horizontal) that takes the smaller
                return calculateProvided(target, 2);
            case 3:
                // Mode: 'max' (vertical, horizontal) that takes the larger
                return calculateProvided(target, 3);
            case 4:
                // Mode: 'sum' takes vertical + horizontal distance
                return calculateProvided(target, 4);
            case 5:
                // Mode: 'TBA' that is non-admissible by multiplying 'sum' by 3
                return 3 * calculateProvided(target,5);


                // TODO: Mode: 'TBA' admissible and dominates 'sum'

            default:
                return this.timeRemainingEstimate;

        }
    }

    public double calculateProvided(Node target, int mode) throws Exception {
        int horizontalEstimate = 0;
        int verticalEstimate = 0;

        // acquire desired x and y positioning
        int xTarget = target.xPos;
        int yTarget = target.yPos;

        // start x and y pos
        int xStart = this.xPos;
        int yStart = this.yPos;

        // take note of robot positioning at current node
        Direction startDirection = this.robot.robotDirection;
        Direction robotDirection = startDirection;

        // calculate estimate of horizontal movements only
        if(xStart > xTarget) {
            // Todo: maybe ?? see if robot is facing in the right direction and if not adjust estimate
            if(robotDirection != Direction.WEST)
                horizontalEstimate++;
            for(int i = xStart ; i >= xTarget ; i--) {
                horizontalEstimate += graph[yStart][i].difficulty;
            }
        }
        else if(xStart < xTarget) {
            // Todo: maybe ?? see if robot is facing in the right direction and if not adjust estimate
            if(robotDirection != Direction.EAST)
                horizontalEstimate++;
            for(int i = xStart ; i <= xTarget ; i++) {
                horizontalEstimate += graph[yStart][i].difficulty;
            }
        }
        // calculate estimate of vertical movements only
        if(yStart > yTarget) {
            // Todo: maybe ?? see if robot is facing in the right direction and if not adjust estimate
            if(robotDirection != Direction.NORTH)
                verticalEstimate++;
            for(int i = yStart ; i >= yTarget ; i--) {
                verticalEstimate += graph[i][xStart].difficulty;
            }
        }
        else if(yStart < yTarget) {
            // Todo: maybe ?? see if robot is facing in the right direction and if not adjust estimate
            if(robotDirection != Direction.SOUTH)
                verticalEstimate++;
            for(int i = yStart ; i <= yTarget ; i++) {
                verticalEstimate += graph[i][xStart].difficulty;
            }
        }
        // return minimum of the two
        switch(mode) {
            case 2:
                if(horizontalEstimate <= verticalEstimate) {
                    return horizontalEstimate;
                } else {
                    return verticalEstimate;
                }
            case 3:
                if(horizontalEstimate >= verticalEstimate) {
                    return horizontalEstimate;
                }
                else {
                    return verticalEstimate;
                }
            case 4:
                return verticalEstimate + horizontalEstimate;
            default:
                return (int)this.timeRemainingEstimate;
        }
    }

    /**
     * aStar takes in a start node and an end node and uses the aStar algorithm to
     * determine the quickest path
     * @param start
     * @param target
     * @return
     */
    public static Node aStar(Node start, Node target, int mode) throws Exception {
        // Priority Queue is just a heap built using priorities.
        PriorityQueue<Node> expanded = new PriorityQueue<>();
        PriorityQueue<Node> toExpand = new PriorityQueue<>();

        start.AStarEstimate = start.timeTraveled + start.calculateHeuristic(target, mode);
        toExpand.add(start);

        while(!toExpand.isEmpty()){
            Node n = toExpand.peek();
            if(n == target){
                return n;
            }

            for(Node.Edge edge : n.neighbors) {
                Node node = edge.node;
                double totalWeight = n.timeTraveled + edge.difficulty;
                boolean bash = edge.bash;

                if(!toExpand.contains(node) && !expanded.contains(node)){
                    node.parent = n;
                    node.timeTraveled = totalWeight;
                    node.robot = new Robot(edge.direction);
                    node.bash = bash;

                    if(n.robot.robotDirection != edge.direction) {
                        node.timeTraveled += node.difficulty * .5;
                        n.turnedPreviously = true;
                    }

                    // Handling turns
                    // handleTurns(node, edge);


                    // handling Bash
                    // handleBash(node, edge);

                     // Will likely have to handle direction change somewhere!!
                    node.AStarEstimate = node.timeTraveled + node.calculateHeuristic(target, mode);
                    toExpand.add(node);
                } else {
                    if(totalWeight < node.timeTraveled){
                        node.parent = n;
                        node.robot.robotDirection = edge.direction;
                        node.timeTraveled = totalWeight;
                        node.bash = bash;
                        if(node.parent.robot.robotDirection != edge.direction) {
                            node.timeTraveled += node.difficulty * .5;
                            n.turnedPreviously = true;
                        }

                        // adds to time for turning
                        // handleTurns(node, edge);

                        // Handling bash
                        //handleBash(node, edge);

                        node.AStarEstimate = node.timeTraveled + node.calculateHeuristic(target, mode);


                        if(expanded.contains(node)){
                            expanded.remove(node);
                            toExpand.add(node);
                        }
                    }
                }
            }

            toExpand.remove(n);
            expanded.add(n);
        }
        return null;
    }

    public static void handleTurns(Node node, Edge edge) {
        if(edge.direction != node.parent.robot.robotDirection) {
//            int turns = node.parent.robot.calculateShortestTurns(edge.direction);
            node.timeTraveled += node.parent.difficulty * .5;
            node.turned2Prev = 1;
            node.robot.robotDirection = edge.direction;
        }
    }
    public static void handleBash(Node node, Edge edge) {
        try {
            if (node.robot.robotDirection == node.parent.robot.robotDirection &&
                node.parent.robot.robotDirection == node.parent.parent.robot.robotDirection &&
                node.parent.difficulty > 3) {
                node.timeTraveled -= (node.parent.difficulty - 3);
                node.parent.robot.setBashed2Prev(true);
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
    }

    // TODO: configure to meet assignment conditions
    public static void printPath(Node target){

    // String output = GameState.getInstance().generateOutputString(target);
    // System.out.println(output);
//        while(target != null) {
//           target = target.parent;
//        }

        Node n = target;

        if (n == null)
            return;

        List<Node> nodes = new ArrayList<Node>();

        while (n.parent != null) {
            nodes.add(n);
            n = n.parent;
        }
        nodes.add(n);
        //Collections.reverse(nodes);

        for (Node node : nodes) {

            if(node.parent == null) {
                System.out.println("");
                System.out.println("Time Traveled: " + node.timeTraveled + " Node Difficulty: " + node.difficulty);
                return;
            }
            printOut(node);
        }

    }
public static void printOut(Node node) {
    System.out.println("Time Traveled: " + node.timeTraveled + " Node Difficulty: " + node.difficulty);
    if (node.bash) {
        System.out.println("\t Robot Bashed");
    }
    if (node.robot.robotDirection != node.parent.robot.robotDirection) {
        System.out.println("\t Robot Turned ");
    }
}

}
