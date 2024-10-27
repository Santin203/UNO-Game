import java.awt.*;
import javax.swing.*;

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
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(1,2));

        //Create Panel for player's handPanel
        JPanel handPanel = new JPanel();
        handPanel.setBackground(new Color(191,11,17));
        handPanel.setPreferredSize(new Dimension(300,600));

        //Create Panel for other players, game pile and discard pile
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(new Color(123,8,12));
        gamePanel.setPreferredSize(new Dimension(600,600));

        //Create Button to Call Uno
        JButton unoButton = new JButton("UNO!");

        handPanel.add(unoButton);
        mainPanel.add(handPanel);
        mainPanel.add(gamePanel);
        /*
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
        */
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        /*
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
        */
        // Display the window
        frame.setVisible(true);
    }

    // Update the message area with server updates
    public void updateMessageArea(String message) {
        messageArea.append("Update from server: " + message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength()); // Auto-scroll to bottom
    }
}
