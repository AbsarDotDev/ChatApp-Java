import java.io.IOException;

public class ChatClientTest {

    public static void main(String[] args) {
        ChatClient client = new ChatClient();

        // Display menu
        while (true) {
            System.out.println("1. Connect to the server");
            System.out.println("2. Enter screen name");
            System.out.println("3. Send a message");
            System.out.println("4. Exit");

            // Get user choice
            int choice = getUserChoice();

            // Perform action based on user choice
            try {
                switch (choice) {
                    case 1:
                        if (!client.isConnected()) {
                            client.connectToServer();
                            System.out.println("Connected to the server.");
                        } else {
                            System.out.println("Already connected to the server.");
                        }
                        break;
                    case 2:
                        if (client.isConnected() && !client.isNameAccepted()) {
                            client.enterScreenName();
                            System.out.println("Screen name submitted.");
                        } else {
                            System.out.println("Please connect to the server first.");
                        }
                        break;
                    case 3:
                        if (client.isConnected() && client.isNameAccepted()) {
                            client.sendMessage();
                        } else {
                            System.out.println("Please connect and enter screen name first.");
                        }
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        client.disconnect();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        try {
            return Integer.parseInt(System.console().readLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}