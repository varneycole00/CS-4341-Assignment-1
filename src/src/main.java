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
        GraphUtils.makeGraph(args[1]);
        // We could potentially build graph in here.
    }

}
