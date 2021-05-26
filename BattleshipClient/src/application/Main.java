package application;

import gameelements.Board;
import gameelements.EnemyBoard;
import gameelements.Ship;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.Commands;
import service.GameCommandDto;
import service.WebSocketService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static service.Commands.*;

public class Main extends Application {

    private int port = 2900;
    private String ipAddress = "localhost";
    private boolean running = false;
    private boolean playerTurn = false;
    private Board playerBoard;
    private EnemyBoard enemyBoard;
    private Scanner scanner;
    WebSocketService service;
    private final List<Integer> ships = Arrays.asList(2, 2, 3, 3, 4, 5);
    private int shipsToPlace = 6;

    private boolean enemyTurn = false;

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

    private Parent createContent() throws Exception {
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

            playerMove(cell.x, cell.y);

            enemyMove();
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Board.Cell cell = (Board.Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(ships.get(shipsToPlace - 1), event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                if (--shipsToPlace == 0) {
                    service.sendMap(playerBoard.getMapForServer());
                    startGame();
                }
            }
        });

        VBox vbox = new VBox(50, enemyBoard, playerBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    private void playerMove(int x, int y) {

        playerTurn = false;

        String response = service.shot(x, y);

        EnemyBoard.Cell shotCell = enemyBoard.getCell(x, y);

        if (VICTORY.text().equals(response)) {
            shotCell.hitAndSink();
            System.out.println("VICTORY! QUITING IN 10 SEC.");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            end();
        }
        else if (HIT_AND_SINK.text().equals(response)) {
            shotCell.hitAndSink();
        }
        else if (HIT.text().equals(response)) {
            shotCell.hit();
        }
        else if (MISS.text().equals(response)) {
            shotCell.miss();
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
        running = true;
        if (!service.isPlayerStarting()) {
            playerTurn = false;
            enemyMove();
        }
    }

    private void initializeConnectionAndJoinRoom() throws Exception {
        service = new WebSocketService(ipAddress, port);
        scanner = new Scanner(System.in);

        service.receive();

        do {
            List<String> availableRooms = service.list();

            System.out.print("Avaiable rooms: | ");
            availableRooms.forEach(roomName -> System.out.print(roomName + " | "));

            System.out.println("Insert room name: ");

        } while (!service.sit(scanner.next()));

        service.waitForOtherPlayerAndStart();
    }

    private void end() {
        service.quit();
        scanner.close();
        exit(0);
    }
}