import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Node implements Comparable<Node> {

    public static Node[][] graph = GraphUtils.getGraph();

    private static int idCounter = 0;
    public int id;
    public int difficulty;

    public Node parent = null;
    public List<Edge> neighbors;


    public double AStarEstimate = Double.MAX_VALUE;
    public double timeTraveled = Double.MAX_VALUE;
    public double timeRemainingEstimate;
    public int xPos;
    public int yPos;
    Robot robot;
    public int turns;

    Node(double h, int difficulty, int xPos, int yPos, Robot robot) {
        this.timeRemainingEstimate = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
        this.difficulty = difficulty;
        this.xPos = xPos;
        this.yPos = yPos;
        this.robot = robot;
        this.turns = 0;
    }

    Node(double h, int difficulty, int xPos, int yPos) {
        this.timeRemainingEstimate = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
        this.difficulty = difficulty;
        this.xPos = xPos;
        this.yPos = yPos;
        this.turns = 0;
    }


    @Override
    public int compareTo(Node n) {
        return Double.compare(this.AStarEstimate, n.AStarEstimate);
    }

    public static class Edge {
        Edge(int difficulty, Node node, Direction direction) {
            this.difficulty = difficulty;
            this.node = node;
            this.direction = direction;
        }
        public int difficulty;
        public Node node;
        public Direction direction;
    }

    public void addBranch(int difficulty, Node node, Direction direction) {
        Edge newEdge = new Edge(difficulty, node, direction);
        neighbors.add(newEdge);
    }

    /* Todo: adjust this method to take in a flag and calculate heuristics differently
        depending on the A* mode) */
    public double calculateHeuristic(Node target, String mode) throws Exception {
        switch(mode) {

            case "zero":
                // Mode 'zero' where always zero
                return 0;
            case "min":
                // Mode 'min' (vertical, horizontal) that takes the smaller
                return calculateProvided(target, "min");
            case "max":
                // Mode: 'max' (vertical, horizontal) that takes the larger
                return calculateProvided(target, "max");
            case "sum":
                // Mode: 'sum' takes vertical + horizontal distance
                return calculateProvided(target, "sum");
            case "nonAdmissible":
                // Mode: 'TBA' that is non-admissible by multiplying 'sum' by 3
                return 3 * calculateProvided(target,"sum");


                // TODO: Mode: 'TBA' admissible and dominates 'sum'

            default:
                return this.timeRemainingEstimate;

        }
    }

    public double calculateProvided(Node target, String mode) throws Exception {
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
            case "min":
                if(horizontalEstimate <= verticalEstimate) {
                    return horizontalEstimate;
                } else {
                    return verticalEstimate;
                }
            case "max":
                if(horizontalEstimate >= verticalEstimate) {
                    return horizontalEstimate;
                }
                else {
                    return verticalEstimate;
                }
            case "sum":
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
    public static Node aStar(Node start, Node target, String mode) throws Exception {
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

                if(!toExpand.contains(node) && !expanded.contains(node)){
                    node.parent = n;
                    node.timeTraveled = totalWeight;
                    node.robot = new Robot(n.robot.robotDirection);


                    // Handling turns
                    handleTurns(node, edge);
                    node.robot.robotDirection = n.robot.robotDirection;


                    // handling Bash
                    handleBash(node, edge);

                     // Will likely have to handle direction change somewhere!!
                    node.AStarEstimate = node.timeTraveled + node.calculateHeuristic(target, mode);
                    toExpand.add(node);
                } else {
                    if(totalWeight < node.timeTraveled){
                        node.parent = n;
                        node.robot.setBashedPrev(false);
                        node.timeTraveled = totalWeight;
                        // adds to time for turning
                        handleTurns(node, edge);
                        node.robot.robotDirection = n.robot.robotDirection;

                        // Handling bash
                        handleBash(node, edge);

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
        if(edge.direction != node.robot.robotDirection) {
//            int turns = node.parent.robot.calculateShortestTurns(edge.direction);
            node.timeTraveled += node.parent.difficulty * .5;
            node.parent.turns = 1;
            node.robot.robotDirection = edge.direction;
        }
    }
    public static boolean handleBash(Node node, Edge edge) {
        try {
            if (node.robot.robotDirection == node.parent.robot.robotDirection &&
                node.parent.robot.robotDirection == node.parent.parent.robot.robotDirection &&
                node.parent.difficulty > 3) {
                node.timeTraveled -= (node.parent.difficulty - 3);
                node.robot.setBashedPrev(true);
                return true;
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
        return false;
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
        Collections.reverse(nodes);

        for (Node node : nodes) {
            System.out.println("Time Traveled: " + node.timeTraveled + " Node Difficulty: " + node.difficulty);
            if(node.turns > 0) {
                System.out.println("\tTurned " + node.turns + " times");
            }
            if(node.robot.getBashedPrev()) {
                System.out.println("\tBashed");
            }
        }

        System.out.println("");

    }


}
