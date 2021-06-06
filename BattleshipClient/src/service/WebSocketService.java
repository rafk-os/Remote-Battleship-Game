package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet6Address;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static service.Commands.*;

public class WebSocketService {

    private final Socket clientSocket;
    private final BufferedReader input ;
    private final PrintWriter output;
    private final int msInMinute = 3600000;

    public WebSocketService(String ipAddress, int port) throws Exception {
        Inet6Address inet6Address = (Inet6Address) Inet6Address.getByName(ipAddress);
        this.clientSocket = new Socket(inet6Address, port);
        clientSocket.setSoTimeout(msInMinute);
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.output = new PrintWriter(clientSocket.getOutputStream());
    }

    public List<String> list() throws Exception {

        send(LIST.text());

        while (NO_FREE_ROOM.text().equals(receive())) {
            System.out.println("No free room. Next request in 5 seconds.");
            Thread.sleep(5000);
            send(LIST.text());
        }

        List<String> list = new ArrayList<>();

        String tmp;

        while (!".".equals(tmp = receive())) {
            list.add(tmp);
        }

        return list;
    }

    /**
     * Returns true when sited successfully
     */
    public String sit() {

        boolean wasChecked = false;
        String response;

        do {
            if (wasChecked) {
                System.out.println("Wrong room number! Insert the correct one!");
            }

            wasChecked = true;

            System.out.print("Insert room name: ");

            Scanner scanner = new Scanner(System.in);
            String room = scanner.next();

            send(SIT.text() + " " + room);

            response = receive();
        } while (WRONG_ROOM.text().equals(response));

        return response;
    }

    public void sendMap(List<String> map) {
        map.forEach(this::send);
        receive();
    }

    public boolean isPlayerStarting()  {
        return !receive().equals(YOUR_OPPONENT_TURN.text());
    }

    public void waitForOtherPlayerAndStart() throws Exception {
        if (receive().equals(WAITING_FOR_SECOND_PLAYER.text())) {
            receive();
        }
    }

    public GameCommandDto getTurnInfo() {
        return new GameCommandDto(receive());
    }

    public String shot(int x, int y) {
        send(new GameCommandDto(SHOT.text(), x, y).toServerMessage());
        return receive();
    }

    public void quit() {
        send(QUIT.text());
    }

    public void send(String message) {
        output.println(displayAndReturnCommand(message));
        output.flush();
    }

    public String receive() {
        try {
            return displayAndReturnReceivedMessage(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
            return "";
        }
    }

    private String displayAndReturnReceivedMessage(String string) {
        System.out.println("Received: " + string);
        return string;
    }

    private String displayAndReturnCommand(String string) {
        System.out.println("COMMAND: " + string);
        return string;
    }
}
