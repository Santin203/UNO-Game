import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI {
    public JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;
    private JButton sendButton;
    public Client client;  // Reference to the Client

    public ClientGUI(Client client) {
        this.client = client;  // Link the client to the GUI
        initializeGUI();
    }

    // Initialize the GUI components
    private void initializeGUI() {
        // Create the main window
        frame = new JFrame("UNO Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Create message area (for server updates)
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);  // Make scrollable
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create input field and send button (for user input)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Add an action listener to send messages when the button is pressed
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                if (!message.isEmpty()) {
                    client.sendToServer(message);  // Send message to the server
                    inputField.setText("");  // Clear input field after sending
                }
            }
        });

        // Display the window
        frame.setVisible(true);
    }

    // Update the message area with server updates
    public void updateMessageArea(String message) {
        messageArea.append("Update from server: " + message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength()); // Auto-scroll to bottom
    }
}
