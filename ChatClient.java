import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient {

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Chat");
    private JTextField textField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 40);

    private boolean connected;
    private boolean nameAccepted;

    public ChatClient() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });

        connected = false;
        nameAccepted = false;
    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Chat",
            JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isNameAccepted() {
        return nameAccepted;
    }

    public void connectToServer() throws IOException {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 1234);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        connected = true;
    }

    public void enterScreenName() throws IOException {
        out.println("SUBMITNAME");
        out.println(getName());

        nameAccepted = true;
    }

    public void sendMessage() throws IOException {
        out.println("MESSAGE " + textField.getText());
    }

    public void disconnect() throws IOException {
        out.println("DISCONNECT");
        connected = false;
    }

    public void start() throws IOException {
        connectToServer();
        enterScreenName();

        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    SwingUtilities.invokeLater(() -> processMessage(line));
                }
                return null;
            }
        };

        worker.execute();
    }

    private void processMessage(String line) {
        if (line.startsWith("MESSAGE")) {
            messageArea.append(line.substring(8) + "\n");
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.start();
    }
}
