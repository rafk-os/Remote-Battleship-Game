package gameelements;

import javafx.scene.Parent;

public class Ship extends Parent {
    private int type;
    private boolean isVertical;
    private int health;

    public Ship(int type, boolean isVertical) {
        this.type = type;
        this.isVertical = isVertical;
        health = type;
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public int getType() {
        return type;
    }
}