package gameelements;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnemyBoard extends Parent {

    private VBox rows = new VBox();

    public EnemyBoard(EventHandler<? super MouseEvent> handler) {
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

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
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
                .filter(neighbour -> neighbour.wasShot && !neighbour.wasSink)
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
        public boolean wasShot = false;
        public boolean wasSink = false;

        private EnemyBoard board;

        public Cell(int x, int y, EnemyBoard board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.BLUE);
            setStroke(Color.BLACK);
        }

        public void miss() {
            setFill(Color.WHITESMOKE);
        }

        public void hit() {
            setFill(Color.RED);
            wasShot = true;
        }

        public void hitAndSink() {
            wasShot = true;
            sink(x,y);
        }
    }
}
