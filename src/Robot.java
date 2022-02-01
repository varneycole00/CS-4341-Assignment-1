public class Robot {
    private boolean bashed2Prev = false;
    Direction robotDirection;

    public Robot(Direction robotDirection) {
        this.robotDirection = robotDirection;
    }

    /**
     * Function used to update the value of the bashPerformed variable. When a bash move is performed,
     * the robot's next move must be a forward move.
     * @param bashed2Prev a boolean representing the new value of the bashPerformed variable
     */
    public void setBashed2Prev(boolean bashed2Prev) {
        this.bashed2Prev = bashed2Prev;
    }

    /**
     * Getter for bashPerformed.
     * @return a boolean value representing the state of the bashPerformed variable
     */
    public boolean getBashed2Prev() {
        return bashed2Prev;
    }

//    public int calculateShortestTurns(Direction newDirection) {
//        if (newDirection == robotDirection)
//            return 0;
//        else if ((robotDirection == Direction.NORTH && (newDirection == Direction.EAST || newDirection == Direction.WEST))
//                || (robotDirection == Direction.WEST && (newDirection == Direction.NORTH || newDirection == Direction.SOUTH))
//                || (robotDirection == Direction.SOUTH && (newDirection == Direction.EAST || newDirection == Direction.WEST))
//                || (robotDirection == Direction.EAST && (newDirection == Direction.NORTH || newDirection == Direction.SOUTH))) {
//            return 1;
//        } else {
//            return 2;
//        }
//    }




}