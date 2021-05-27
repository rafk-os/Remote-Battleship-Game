package service;

public class GameCommandDto {
    private String command;
    private char x;
    private int y;

    public GameCommandDto(String command, int x, int y) {
        this.command = command;
        this.x = (char) ((int)'A' + x);
        this.y = y;
    }

    public GameCommandDto(String message) {
        String[] tmp = message.split(" ");

        command = tmp[0];
        x = tmp[1].charAt(0);
        y = Character.getNumericValue(tmp[1].charAt(1));
    }

    String toServerMessage() {
        return command + ' ' + x + y;
    }

    public String getCommand() {
        return command;
    }

    public int getX() {
        return x-'A';
    }

    public int getY() {
        return y;
    }
}
