package service;

public enum Commands {

    LIST("LIST"),
    NO_FREE_ROOM("NO_FREE_ROOM"),
    SIT("SIT"),
    JOINING_SUCCESSFUL("JOINING_SUCCESSFUL"),
    WAITING_FOR_SECOND_PLAYER("WAITING_FOR_SECOND_PLAYER"),
    GAME_IS_STARTING("GAME_IS_STARTING"),
    YOUR_OPPONENT_TURN("YOUR_OPPONENT_TURN"),
    VICTORY("VICTORY"),
    HIT_AND_SINK("HIT_AND_SINK"),
    MISS("MISS"),
    HIT("HIT"),
    SHOT("SHOT"),
    QUIT("QUIT");

    private final String message;

    Commands(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }

    public String text() {
        return message;
    }
}
