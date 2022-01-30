import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Node implements Comparable<Node> {

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

    public void addBranch(int weight, Node node, Direction direction) {
        Edge newEdge = new Edge(weight, node, direction);
        neighbors.add(newEdge);
    }

    /* Todo: adjust this method to take in a flag and calculate heuristics differently
        depending on the A* mode) */
    public double calculateHeuristic(Node target, String mode) {
        switch(mode) {
            case "default":
                return this.timeRemainingEstimate;
            case "zero":
                // Mode 'zero' where always zero
                return 0;
            case "min":
                // TODO: Mode 'min' (vertical, horizontal) that takes the smaller
                return minModeHeuristic(target);
            case "max":
                // TODO: Mode: 'max' (vertical, horizontal) that takes the larger
            case "sum":
                // TODO: Mode: 'sum' takes vertical + horizontal distance

                // TODO: Mode: 'TBA' admissible and dominates 'sum'
                // TODO: Mode: 'TBA' that is non-admissible by multiplying 'sum' and previous

        }
        return -1;
    }
    public int minModeHeuristic(Node target) {
        int horizontalEstimate = 0;
        int verticalEstimate = 0;

        // acquire desired x and y positioning
        int xTarget = target.xPos;
        int yTarget = target.yPos;
        // current x and y pos
        int xCurrent = this.xPos;
        int yCurrent = this.yPos;

        // take note of robot positioning at current node
        Direction robotDirection = this.robot.robotDirection;

        // calculate estimate of horizontal movements only

        // calculate estimate of vertical movements only
        // return minimum of the two
        return -1;
    }

    /**
     * aStar takes in a start node and an end node and uses the aStar algorithm to
     * determine the quickest path
     * @param start
     * @param target
     * @return
     */
    public static Node aStar(Node start, Node target, String mode) {
        // Priority Queue is just a heap built using priorities.
        PriorityQueue<Node> expanded = new PriorityQueue<>();
        PriorityQueue<Node> toExpand = new PriorityQueue<>();

        start.AStarEstimate = start.timeTraveled + start.calculateHeuristic(target, "Default");
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
                    node.AStarEstimate = node.timeTraveled + node.calculateHeuristic(target,"Default");
                    toExpand.add(node);
                } else {
                    if(totalWeight < node.timeTraveled){
                        node.parent = n;
                        node.timeTraveled = totalWeight;
                        node.AStarEstimate = node.timeTraveled + node.calculateHeuristic(target, "Default");

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
