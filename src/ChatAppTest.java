import java.util.Scanner;

public class ChatAppTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Server server = null;
        Client client = null;

        boolean running = true;

        while (running) {
            System.out.println("Menu:");
            System.out.println("1. Start Server");
            System.out.println("2. Start Client");
            System.out.println("3. Client sends message");
            System.out.println("4. Server sends message");
            System.out.println("5. Client disconnects");
            System.out.println("6. Server handles disconnection");
            System.out.println("7. Client handles server disconnection");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    server = new Server();
                    server.start();
                    System.out.println("Server started successfully\n");
                    break;
                case 2:
                    client = new Client();
                    client.start();
                    System.out.println("Client connected successfully\n");
                    break;
                case 3:
                    if (client != null) {
                        System.out.print("Enter a message to send from the client: ");
                        scanner.nextLine(); // Consume the newline character
                        String clientMessage = scanner.nextLine();
                        client.sendMessage(clientMessage);
                        System.out.println("Message sent successfully\n");
                    } else {
                        System.out.println("Client not started. Please start the client first.\n");
                    }
                    break;
                case 4:
                    if (server != null) {
                        System.out.print("Enter a message to send from the server: ");
                        String serverMessage = scanner.nextLine();
                        server.sendMessage(serverMessage);
                        System.out.println("Message sent successfully\n");
                    } else {
                        System.out.println("Server not started. Please start the server first.\n");
                    }
                    break;
                case 5:
                    if (client != null) {
                        client.disconnect();
                        System.out.println("Client disconnected successfully\n");
                    } else {
                        System.out.println("Client not started. Please start the client first.\n");
                    }
                    break;
                case 6:
                    if (server != null) {
                        server.handleDisconnection();
                        System.out.println("Server disconnection handled successfully\n");
                    } else {
                        System.out.println("Server not started. Please start the server first.\n");
                    }
                    break;
                case 7:
                    if (client != null) {
                        client.handleServerDisconnection();
                        System.out.println("Client server disconnection handled successfully\n");
                    } else {
                        System.out.println("Client not started. Please start the client first.\n");
                    }
                    break;
                case 8:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }

        // Close the scanner
        scanner.close();
    }
}
