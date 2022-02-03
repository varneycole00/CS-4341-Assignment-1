import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

public class BoardGeneration {
    public static void main(String[] args) throws Exception {
        int width = 150;
        int height = 150;

        int startX = 0;
        int startY = 0;
        int endX = width - 1;
        int endY = height - 1;
//        int startX = ThreadLocalRandom.current().nextInt(0, width);
//        int startY = ThreadLocalRandom.current().nextInt(0, height);
//        int endX = ThreadLocalRandom.current().nextInt(0, width);
//        int endY = ThreadLocalRandom.current().nextInt(0, height);

        StringBuilder out = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == startX && y == startY) {
                    out.append('S').append('\t');
                } else if (x == endX && y == endY) {
                    out.append('G').append('\t');
                } else {
                    int num = ThreadLocalRandom.current().nextInt(1, 9);
                    out.append(num).append('\t');
                }
            }

            out.append('\n');
        }

//        System.out.println(out.toString());
        Files.write(Paths.get("src/board1.txt"), out.toString().getBytes(StandardCharsets.UTF_8));

        int dx = endX - startX;
        int dy = endY - startY;

        System.out.println("SG Distance " + Math.sqrt((dx * dx) + (dy * dy)));
    }
}
