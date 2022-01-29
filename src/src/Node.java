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

    Node(double h, int difficulty) {
        this.timeRemainingEstimate = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
        this.difficulty = difficulty;
    }

    @Override
    public int compareTo(Node n) {
        return Double.compare(this.AStarEstimate, n.AStarEstimate);
    }

    public static class Edge {
        Edge(int weight, Node node) {
            this.weight = weight;
            this.node = node;
        }
        public int weight;
        public Node node;
    }

    public void addBranch(int weight, Node node) {
        Edge newEdge = new Edge(weight, node);
        neighbors.add(newEdge);
    }

    /* Todo: adjust this method to take in a flag and calculate heuristics differently
        depending on the A* mode) */
    public double calculateHeuristic(Node target, String mode) {
        if (mode.equals("Default")) {
            return this.timeRemainingEstimate;
        } else {
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
                double totalWeight = n.timeTraveled + edge.weight;

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
