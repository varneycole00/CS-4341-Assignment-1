import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class main {

    Node[][] nodes;
    Robot robot;

    public static void main(String[] args) {
        // Main code block initiating execution

        // We could potentially build graph in here.
    }

    public String[][] loadTerrain(String fileName) {
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

    public void makeGraph(String fileName){
        String[][] fileArray = loadTerrain(fileName);
        nodes = new Node[fileArray.length][fileArray[0].length];
        robot = new Robot(Direction.NORTH);
        int y = 1;
        for (String[] a : fileArray){
            int x = 1;
            for (String s : a){
                if (s == "s" || s == "S")
                    nodes[y][x] = Node(Integer.MAX_VALUE, 1, x, y, robot); //Make robot
                else if (s == "g" || s == "G")
                    nodes[y][x] = Node(Integer.MAX_VALUE, 1, x, y, Null);
                else
                    nodes[y][x] = Node(Integer.MAX_VALUE, Integer.valueOf(s), x, y, Null);
             x++;
            }
            y++;
        }
    }

}
