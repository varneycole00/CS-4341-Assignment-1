import java.util.ArrayList;

public class Node implements Comparable<Node> {

    private static int idCounter = 0;
    public int id;

    public Node parentNode = null;
    public List<edge> neighbors;

    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;

    public double h;

    Node(double h) {
        this.h = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
    }

    @Override
    public int compareTo(Node n) {
        return Double.compare(this.f, n.f);
    }

    public static class Edge {
        Edge(int weight, Node node) {
            this.weight = weight;
            this.node = node;
        }
        public int weight;
        public Node node;
    }
}
