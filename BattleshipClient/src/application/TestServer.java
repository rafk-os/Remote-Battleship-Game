package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TestServer {

    private static int PORT = 2900;

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();
        PrintWriter output = new PrintWriter(socket.getOutputStream());
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Scanner scanner = new Scanner(System.in);
        String message;
        while (true) {

            System.out.print("Send/Receive (S/R): ");
            String option = scanner.next();
            if (option.equals("s")) {
                System.out.print("Insert message: ");
                scanner.nextLine();
                message = scanner.nextLine();
                output.println(message);
                output.flush();
            }
            else if (option.equals("r")) {
                System.out.println(input.readLine());
            }
        }
    }
}
