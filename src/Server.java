import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public void start() {
        try {
            serverSocket = new ServerSocket(3000);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread receiver = new Thread(() -> {
                try {
                    String msg = in.readLine();
                    while (msg != null) {
                        System.out.println("Client: " + msg);
                        msg = in.readLine();
                    }
                    System.out.println("Client disconnected");
                    out.close();
                    clientSocket.close();
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiver.start();

            Thread sender = new Thread(() -> {
                try {
                    while (true) {
                        System.out.print("Server: ");
                        String msg = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            sender.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (!clientSocket.isClosed()) {
            out.println("Server: " + message);
            out.flush();
        }
    }

    public void handleDisconnection() {
        System.out.println("Client disconnected");
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
