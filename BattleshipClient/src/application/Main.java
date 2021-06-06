package application;

import gameelements.Board;
import gameelements.EnemyBoard;
import gameelements.Ship;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.GameCommandDto;
import service.WebSocketService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.util.Objects.*;
import static service.Commands.*;

public class Main extends Application {

    private static final int PORT = 2900;
    private static final String IP_ADDRESS = "::1";
    private boolean running = false;
    private boolean playerTurn = false;
    private Board playerBoard;
    private EnemyBoard enemyBoard;
    private Scanner scanner;
    private WebSocketService service;
    private final List<Integer> ships = Arrays.asList(2, 2, 3, 3, 4, 5);
    private int shipsToPlace = 6;
    private EnemyBoard.Cell cellToShot = null;

    @Override
    public void start(Stage primaryStage) throws Exception {

        initializeConnectionAndJoinRoom();

        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

        enemyBoard = new EnemyBoard(event -> {
            if (!playerTurn) {
                return;
            }

            EnemyBoard.Cell cell = (EnemyBoard.Cell) event.getSource();

            if (cell.wasShot) {
                return;
            }

            cellToShot = cell;
        });

        playerBoard = new Board(event -> {
            if (running || shipsToPlace == 0)
                return;

            Board.Cell cell = (Board.Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(ships.get(shipsToPlace - 1), event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                shipsToPlace--;
            }
        });

        Button button = new Button("Start game");

        button.setOnAction(actionEvent -> {
            if (shipsToPlace==0) {
                startGame();
            }
        });

        button.setMinSize(50, 10);

        VBox vbox = new VBox(50, enemyBoard, playerBoard, button);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);

                    if (nonNull(cellToShot)) {
                        playerMove();
                        enemyMove();
                        cellToShot = null;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        return root;
    }

    private void playerMove() {
        playerTurn = false;
        String response = service.shot(cellToShot.x, cellToShot.y);

        if (VICTORY.text().equals(response)) {
            cellToShot.hitAndSink();
            System.out.println("VICTORY! QUITING IN 10 SEC.");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end();
        }
        else if (HIT_AND_SINK.text().equals(response)) {
            cellToShot.hitAndSink();
        }
        else if (HIT.text().equals(response)) {
            cellToShot.hit();
        }
        else if (MISS.text().equals(response)) {
            cellToShot.miss();
        }
        else {
            System.out.println("BAD SYNTAX? XD");
        }
    }

    private void enemyMove() {
        GameCommandDto message = service.getTurnInfo();
        playerBoard.getCell(message.getX(), message.getY()).shoot();

        if (VICTORY.text().equals(message.getCommand())) {

            System.out.println("YOUR OPPONENT WON! QUITING IN 10 SEC.");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end();
        }

        service.receive(); // YOUR_TURN message

        playerTurn = true;
    }

    private void startGame() {
        service.sendMap(playerBoard.getMapForServer());
        running = true;
        if (!service.isPlayerStarting()) {
            playerTurn = false;
            enemyMove();
        }
        else {
            playerTurn = true;
        }
    }

    private void initializeConnectionAndJoinRoom() throws Exception {
        service = new WebSocketService(IP_ADDRESS, PORT);
        scanner = new Scanner(System.in);

        service.receive();
        String response;

        do {
            List<String> availableRooms = service.list();

            System.out.print("Avaiable rooms: | ");
            availableRooms.forEach(roomName -> System.out.print(roomName + " | "));

            response = service.sit();

        } while (!JOINING_SUCCESSFUL.text().equals(response));

        service.waitForOtherPlayerAndStart();
    }

    private void end() {
        service.quit();
        scanner.close();
        exit(0);
    }
}