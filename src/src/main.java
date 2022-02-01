import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {



    public static void main(String[] args) {
        // Main code block initiating execution
        Scanner in = new Scanner(System.in);

        System.out.println("input path to file");
        String file = in.nextLine();

        System.out.println("input heuristic (integer 1-6)");
        int heuristic = Integer.valueOf(in.nextLine());

        try {
            GraphUtils.makeGraph(file, heuristic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // We could potentially build graph in here.
    }

}
