import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {
    private static GameState instance = null;
    private int score = 0;
    private int numActions = 0;
    private int numNodeExpanded = 0;
    private List<String> outputText = new ArrayList<String>();

    /**
     * The constructor for GameState.
     */
    private GameState() {

    }

    /**
     * Getter for instance.
     * @return the current instance of GameState
     */
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    /**
     * Sets the current instance of GameState to null.
     */
    public static void setInstance() {
        instance = null;
    }

    public void buildOutputString(MovementType movementType) {
        outputText.add(movementType.toString());
    }

    public String generateOutputString(Node target) {



        String ret = "";

        for (String s: outputText) {
            ret = ret + s + "\n";
        }

        return ret;
    }
}
