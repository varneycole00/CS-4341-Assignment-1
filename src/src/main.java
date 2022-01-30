import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class main {

    public static void main(String[] args) {
        // Main code block initiating execution

        // We could potentially build graph in here.
    }

    public static String[][] loadTerrain(String fileName) {
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

}
