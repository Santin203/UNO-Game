import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

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
        JFrame frame = new JFrame("UNO Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Create button panel for the bottom of handPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(24, 24, 24));
        buttonPanel.setPreferredSize(new Dimension(300, 100));

        // Create panel for player's hand, containing buttonPanel
        JPanel handPanel = new JPanel(new BorderLayout());
        handPanel.setBackground(new Color(191, 11, 17));
        handPanel.add(buttonPanel, BorderLayout.SOUTH); // Place buttonPanel at the bottom

        // Create Button to Call Uno
        JButton unoButton = new JButton("UNO!");
        // Format unoButton
        unoButton.setBackground(new Color(191, 11, 17));
        unoButton.setForeground(Color.BLACK);
        unoButton.setPreferredSize(new Dimension(60, 30));
        unoButton.setFocusable(false);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2); // Line border with width 2
        unoButton.setBorder(border);
        buttonPanel.add(unoButton);

        // Create panel for other players, game pile, and discard pile
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(new Color(123, 8, 12));

        // Use a JsplitPane to divide the handPanel and gamePanel with a proportional split
        JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, handPanel, gamePanel);
        mainPane.setResizeWeight(0.33); // Set initial proportion (33% for handPanel, 67% for gamePanel)
        mainPane.setContinuousLayout(true); // Smooth resizing
        mainPane.setBackground(new Color(0,0,0));
        mainPane.setDividerSize(5);

        // Customize the divider to change the slider color
        mainPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);
                        // Set the divider background color
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
            }
        });

        // Add the split pane to the main frame
        frame.add(mainPane, BorderLayout.CENTER);
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
