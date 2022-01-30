import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GraphUtils {

    static Node[][] graph;
    static Robot robot;

    public  GraphUtils(Node[][] graph) {
        this.graph = graph;
    }
    public static Node[][] getGraph() {
        return graph;
    }

    private static String[][] loadTerrain(String fileName) {
        List<String> lines = new ArrayList<String>();

        try{
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        String[][] fileArray = new String[lines.size()][];

        for(int i =0; i<lines.size(); i++){
            fileArray[i] = lines.get(i).split("\t"); //tab-separated
        }

        return fileArray;
    }

    public static void makeGraph(String fileName){
        String[][] fileArray = loadTerrain(fileName);
        graph = new Node[fileArray.length][fileArray[0].length];
        robot = new Robot(Direction.NORTH);
        int y = 0;
        for (String[] a : fileArray){
            int x = 0;
            for (String s : a){
                if (s.equals("S") || s.equals("s"))
                    graph[y][x] = new Node(Integer.MAX_VALUE, 1, x, y, robot); //Make robot
                else if (s.equals("G") || s.equals("g"))
                    graph[y][x] = new Node(Integer.MAX_VALUE, 1, x, y);
                else
                    graph[y][x] = new Node(Integer.MAX_VALUE, Integer.parseInt(s), x, y);
                x++;
            }
            y++;
        }
        findEdges();
    }

    private static void findEdges(){
        int y = 0;
        Node left;
        Node right;
        Node up;
        Node down;
        for (Node[] a : graph){
            int x = 0;
            for (Node n : a){
                try{
                    left = graph[y][x-1];}
                catch(Exception e){
                    left = null;
                }

                try{
                    right = graph[y][x+1];}
                catch(Exception e){
                    right = null;
                }

                try{
                    up = graph[y-1][x];}
                catch(Exception e){
                    up = null;
                }

                try{
                    down = graph[y+1][x];}
                catch(Exception e){
                    down = null;
                }

                if (left != null)
                    n.addBranch(left.difficulty, left, Direction.WEST);
                if (right != null)
                    n.addBranch(right.difficulty, right, Direction.EAST);
                if (up != null)
                    n.addBranch(up.difficulty, up, Direction.NORTH);
                if (down != null)
                    n.addBranch(down.difficulty, down, Direction.SOUTH);

                x++;
            }

            y++;
        }
    }

    // TODO: Test this bish
    public static Direction calculateDirection(int xStart, int yStart, int xEnd, int yEnd) throws Exception {
        if(xStart != xEnd && yStart != yEnd) {
            throw new Exception("Could not calculate direction");
        } if(xStart > xEnd) {
            return Direction.EAST;
        } if(xStart < xEnd) {
            return Direction.WEST;
        } if(yStart > yEnd) {
            return Direction.SOUTH;
        } if(yStart < yEnd) {
            return Direction.NORTH;
        }
        throw new Exception("could not calculate");
    }




//    private boolean adjMatrix[][];
//    private int numNodes;
//
//    // Initialize the matrix
//    public GraphUtils(int numNodes) {
//        this.numNodes = numNodes;
//        adjMatrix = new boolean[numNodes][numNodes];
//    }
//
//    // Add edges
//    public void addEdge(int i, int j) {
//        adjMatrix[i][j] = true;
//        adjMatrix[j][i] = true;
//    }
//
//    // Remove edges
//    public void removeEdge(int i, int j) {
//        adjMatrix[i][j] = false;
//        adjMatrix[j][i] = false;
//    }
//
//    // Print the matrix
//    public String toString() {
//        StringBuilder s = new StringBuilder();
//        for (int i = 0; i < numNodes; i++) {
//            s.append(i + ": ");
//            for (boolean j : adjMatrix[i]) {
//                s.append((j ? 1 : 0) + " ");
//            }
//            s.append("\n");
//        }
//        return s.toString();
//    }
//
//    public static void main(String args[]) {
//        GraphUtils g = new GraphUtils(4);
//
//        g.addEdge(0, 1);
//        g.addEdge(0, 2);
//        g.addEdge(1, 2);
//        g.addEdge(2, 0);
//        g.addEdge(2, 3);
//
//        System.out.print(g.toString());
//    }


}
