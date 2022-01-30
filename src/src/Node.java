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

    Node(double h, int difficulty, int xPos, int yPos, Robot robot) {
        this.timeRemainingEstimate = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
        this.difficulty = difficulty;
        this.xPos = xPos;
        this.yPos = yPos;
        this.robot = robot;
    }

    Node(double h, int difficulty, int xPos, int yPos) {
        this.timeRemainingEstimate = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
        this.difficulty = difficulty;
        this.xPos = xPos;
        this.yPos = yPos;
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


                // TODO: Mode: 'TBA' admissible and dominates 'sum'
                // TODO: Mode: 'TBA' that is non-admissible by multiplying 'sum' and previous
            default:
                return this.timeRemainingEstimate;

        }
    }

    public int calculateProvided(Node target, String mode) throws Exception {
        int horizontalEstimate = 0;
        int verticalEstimate = 0;

        // acquire desired x and y positioning
        int xTarget = target.xPos;
        int yTarget = target.yPos;

        // start x and y pos
        int xStart = this.xPos;
        int yStart = this.yPos;

        // current x and y pos
        int xCurrent = this.xPos;
        int yCurrent = this.yPos;

        // take note of robot positioning at current node
        Direction startDirection = this.robot.robotDirection;
        Direction robotDirection = startDirection;

        // calculate estimate of horizontal movements only
        if(xStart > xTarget) {
            // Todo: maybe ?? see if robot is facing in the right direction and if not adjust estimate
            for(int i = xStart ; i >= xTarget ; i--) {
                horizontalEstimate += graph[i][yStart].difficulty;
            }
        }
        else if(xStart < xTarget) {
            // Todo: maybe ?? see if robot is facing in the right direction and if not adjust estimate
            for(int i = xStart ; i <= xTarget ; i++) {
                horizontalEstimate += graph[i][yStart].difficulty;
            }
        }
        // calculate estimate of vertical movements only
        if(yStart > yTarget) {
            // Todo: maybe ?? see if robot is facing in the right direction and if not adjust estimate
            for(int i = yStart ; i >= yTarget ; i--) {
                verticalEstimate += graph[xStart][i].difficulty;
            }
        }
        else if(yStart < yTarget) {
            // Todo: maybe ?? see if robot is facing in the right direction and if not adjust estimate
            for(int i = yStart ; i <= yTarget ; i++) {
                verticalEstimate += graph[xStart][i].difficulty;
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
                return -1;
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

            for(Node.Edge edge : n.neighbors){
                Node node = edge.node;
                double totalWeight = n.timeTraveled + edge.difficulty;

                if(!toExpand.contains(node) && !expanded.contains(node)){
                    node.parent = n;
                    node.timeTraveled = totalWeight;
                    node.AStarEstimate = node.timeTraveled + node.calculateHeuristic(target, mode);
                    toExpand.add(node);
                } else {
                    if(totalWeight < node.timeTraveled){
                        node.parent = n;
                        node.timeTraveled = totalWeight;
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

    // TODO: configure to meet assignment conditions
    public static void printPath(Node target){
        Node n = target;

        if(n==null)
            return;

        List<Integer> ids = new ArrayList<>();

        while(n.parent != null){
            ids.add(n.id);
            n = n.parent;
        }
        ids.add(n.id);
        Collections.reverse(ids);

        for(int id : ids){
            System.out.print(id + " ");
        }

        System.out.println("");

    }


}
