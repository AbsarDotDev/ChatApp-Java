import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner sc;

    public Client() {
        sc = new Scanner(System.in);
    }

    public void start() {
        try {
            clientSocket = new Socket("127.0.0.1", 3000);
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread receiver = new Thread(() -> {
                try {
                    String msg = in.readLine();
                    while (msg != null) {
                        System.out.println("Server: " + msg);
                        msg = in.readLine();
                    }
                    System.out.println("Server out of service");
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiver.start();

            // Wait for the receiver thread to be ready
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread sender = new Thread(() -> {
                while (true) {
                    System.out.print("Client: ");
                    String msg = sc.nextLine();
                    sendMessage(msg);
                }
            });
            sender.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public void disconnect() {
        try {
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnected from server");
    }

    public void handleServerDisconnection() {
        System.out.println("Server out of service");
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
