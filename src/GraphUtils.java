import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GraphUtils {

    static Node[][] graph;
    static Robot robot;
    public static int[] start = new int[2];
    public static int[] end = new int[2];

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

    public static void makeGraph(String fileName, int h) throws Exception {
        String[][] fileArray = loadTerrain(fileName);
        graph = new Node[fileArray.length][fileArray[0].length];
        robot = new Robot(Direction.NORTH);
        int y = 0;
        for (String[] a : fileArray){
            int x = 0;
            for (String s : a){
                if (s.equals("S") || s.equals("s")) {
                    graph[y][x] = new Node(Integer.MAX_VALUE, 1, x, y, robot); //Make robot
                    graph[y][x].timeTraveled = 0;
                    start = new int[]{y,x};
                }
                else if (s.equals("G") || s.equals("g")){
                    graph[y][x] = new Node(Integer.MAX_VALUE, 1, x, y);
                    end = new int[]{y,x};
                }
                else
                    graph[y][x] = new Node(Integer.MAX_VALUE, Integer.parseInt(s), x, y);
                x++;
            }
            y++;
        }
        findEdges();
        Node goal = Node.aStar(graph[start[0]][start[1]], graph[end[0]][end[1]], h);
        Node.printPath(goal);
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
                    n.addBranch(left.difficulty, left, Direction.WEST,false);
                if (right != null)
                    n.addBranch(right.difficulty, right, Direction.EAST,false);
                if (up != null)
                    n.addBranch(up.difficulty, up, Direction.NORTH,false);
                if (down != null)
                    n.addBranch(down.difficulty, down, Direction.SOUTH,false);

                addBashEdges(graph, n, x, y);

                x++;
            }

            y++;
        }
    }

    public static void addBashEdges(Node[][] graph, Node n, int x, int y) {
        Node bashLeft;
        Node bashRight;
        Node bashUp;
        Node bashDown;

        try{
            bashLeft = graph[y][x-2];
        } catch (Exception e) {
            bashLeft = null;
        }
        try{
            bashRight = graph[y][x+2];
        } catch (Exception e) {
            bashRight = null;
        }
        try{
            bashUp = graph[y-2][x];
        } catch (Exception e) {
            bashUp = null;
        }
        try{
            bashDown = graph[y+2][x];
        } catch (Exception e) {
            bashDown = null;
        }

        if (bashLeft != null) {
            n.addBranch(bashLeft.difficulty + 3, bashLeft, Direction.WEST, true);
        }
        if (bashRight != null) {
            n.addBranch(bashRight.difficulty + 3, bashRight, Direction.EAST, true);
        }
        if (bashUp != null) {
            n.addBranch(bashUp.difficulty + 3, bashUp, Direction.NORTH, true);
        }
        if (bashDown != null) {
            n.addBranch(bashDown.difficulty + 3, bashDown, Direction.SOUTH, true);
        }


    }


}
