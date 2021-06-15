package gameelements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javafx.scene.paint.Color.GRAY;

public class Board extends Parent {
    private VBox rows = new VBox();
    public int ships = 6;

    public Board(EventHandler<? super MouseEvent> handler) {
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell cell = new Cell(x, y, this);
                cell.setOnMouseClicked(handler);
                row.getChildren().add(cell);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    public boolean placeShip(Ship ship, int x, int y) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.getType();

            if (ship.isVertical()) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    cell.setFill(GRAY);
                }
            } else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    cell.setFill(GRAY);
                }
            }
            return true;
        }
        return false;
    }

    public List<String> getMapForServer() {
        List<String> list = new ArrayList<>();

        StringBuilder stringBuilder;

        for (int y = 0; y < 10; y++) {
            stringBuilder = new StringBuilder();
            for (int x = 0; x < 10; x++) {
                stringBuilder.append(isNull(getCell(x, y).ship) ? 'o' : 'x');
            }
            list.add(stringBuilder.toString());
        }

        return list;
    }

    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[]{
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int) p.getX(), (int) p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }

    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.getType();

        if (ship.isVertical()) {

            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i))
                    return false;

                Cell cell = getCell(x, i);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        } else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cell cell = getCell(i, y);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }

        return true;
    }

    private void sink(int x, int y) {
        Cell cell = getCell(x, y);
        cell.wasSink = true;
        cell.setFill(Color.BLACK);
        notSunkNeighbours(getNeighbors(x, y))
                .forEach(neighbour -> sink(neighbour.x, neighbour.y));
    }

    private List<Cell> notSunkNeighbours(Cell[] neighbours) {
        return Arrays.stream(neighbours)
                .filter(neighbour -> !neighbour.wasSink && nonNull(neighbour.ship))
                .collect(Collectors.toList());
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public class Cell extends Rectangle {
        public int x, y;
        public Ship ship = null;
        public boolean wasShot = false;
        public boolean wasSink = false;

        private Board board;

        public Cell(int x, int y, Board board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.BLUE);
            setStroke(Color.BLACK);
        }

        public boolean shoot() {
            wasShot = true;
            setFill(Color.WHITESMOKE);

            if (nonNull(ship)) {
                ship.hit();
                setFill(Color.RED);
                if (!ship.isAlive()) {
                    board.ships--;
                    sink(x, y);
                }
                return true;
            }
            return false;
        }
    }
}