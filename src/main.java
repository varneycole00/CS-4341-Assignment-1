import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {



    public static void main(String[] args) {
        // Main code block initiating execution
        Scanner in = new Scanner(System.in);
        String file = "src/board10.txt";
        int heuristic = 6;


        try {
            GraphUtils.makeGraph(file, heuristic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
