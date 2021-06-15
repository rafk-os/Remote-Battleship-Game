package service;

public enum AudioPaths {

    DESTROYED("C:\\Users\\admin\\IdeaProjects\\Remote-Battleship-Game\\BattleshipClient\\src\\static\\sounds\\destroyed.wav"),
    LOOSE("C:\\Users\\admin\\IdeaProjects\\Remote-Battleship-Game\\BattleshipClient\\src\\static\\sounds\\loose.mp3"),
    SHOT("C:\\Users\\admin\\IdeaProjects\\Remote-Battleship-Game\\BattleshipClient\\src\\static\\sounds\\shot.wav"),
    WATER_SPLASH("C:\\Users\\admin\\IdeaProjects\\Remote-Battleship-Game\\BattleshipClient\\src\\static\\sounds\\water_splash.wav"),
    WIN("C:\\Users\\admin\\IdeaProjects\\Remote-Battleship-Game\\BattleshipClient\\src\\static\\sounds\\win.mp3");

    private final String message;

    AudioPaths(String message) {
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
