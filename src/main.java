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

        System.out.println("input path to file");
        String file = in.nextLine();

        System.out.println("input heuristic (integer 1-6)");
        int heuristic = Integer.parseInt(in.nextLine());

        long startTime = System.currentTimeMillis();

        try {
            GraphUtils.makeGraph(file, heuristic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        NumberFormat formatter = new DecimalFormat("#0.00000");
        System.out.print("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");

    }

}
