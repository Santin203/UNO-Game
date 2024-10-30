import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class ClientGUI {
    public JFrame frame;
    private IPlayer clientPlayer;
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
        createFrame();

        // Create button panel for the bottom of handPanel
        JPanel buttonPanel = createButtonPanel();

        //Create Grid panel for player's hand
        JPanel cardsPanel = createCardsPanel();

        //Create Scrollable panel for storing cards
        JScrollPane cardsScrollPane = createScrollablePane(cardsPanel);

        // Create panel for player's hand, containing buttonPanel
        JPanel handPanel = createHandPanel(buttonPanel, cardsScrollPane);

        // Create Button to Call Uno
        JButton unoButton = createUnoButton();
        //unoButton.setEnabled(false);      //Change buttons availability to faslse
        buttonPanel.add(unoButton);

        // Create panel for other players, game pile, and discard pile
        JPanel gamePanel = createGamePanel();

        // Use a JsplitPane to divide the handPanel and gamePanel with a proportional split
        JSplitPane mainPane = createMainPane(handPanel, gamePanel);

        // Add the split pane to the main frame
        frame.add(mainPane, BorderLayout.CENTER);
        // Display the window
        frame.setVisible(true);
    }

    private void createFrame() {
        frame = new JFrame("UNO Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());
        // Load the image and get a section of it for the cards and Icon
        BufferedImage spriteSheet = openImage("UNO_Cards.png");

        BufferedImage iconImage = spriteSheet.getSubimage(320, 192, 32, 48);
        frame.setIconImage(iconImage);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 0, 0));
        buttonPanel.setPreferredSize(new Dimension(300, 100));
        return buttonPanel;
    }

    private BufferedImage openImage(String Path) {
        BufferedImage spriteSheet = null;
        try {
            spriteSheet = ImageIO.read(new File(Path));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Image file not found: " + Path, "Error", JOptionPane.ERROR_MESSAGE);
        }
       
        BufferedImage iconImage = spriteSheet.getSubimage(320, 192, 32, 48);
        frame.setIconImage(iconImage);
        return spriteSheet;
    }

    private JPanel createCardsPanel() {
        int cardWidth = 64;
        int cardHeight = 96;

        JPanel cardsPanel = new JPanel(new GridBagLayout());
        cardsPanel.setBackground(new Color(24,24,24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2); // Spacing between cells
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0; // Allow panel to grow horizontally

        BufferedImage spriteSheet = openImage("UNO_Cards.png");

        for (int i = 1; i <= 80; i++) {
            BufferedImage buttonImage = spriteSheet.getSubimage(0, 0, 32, 48);
            ImageIcon buttonIcon = new ImageIcon(buttonImage.getScaledInstance(32, 48, Image.SCALE_SMOOTH));

            JButton button = new JButton(buttonIcon);
            button.setPreferredSize(new Dimension(cardWidth, cardHeight)); // Fixed size
            button.setBackground(new Color(0, 0, 0));

           // Calculate column and row based on buttonCount
           gbc.gridx = i % 3; // Column index
           gbc.gridy = i / 3; // Row index
           cardsPanel.add(button, gbc);
        }
        return cardsPanel;
    }

    private JScrollPane createScrollablePane(JPanel panel) {
        JScrollPane cardsScrollPane = new JScrollPane(panel);
        cardsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        cardsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cardsScrollPane.setPreferredSize(new Dimension(200, cardsScrollPane.getPreferredSize().height));
        cardsScrollPane.setBackground(new Color(0, 0, 0));
        return cardsScrollPane;
    }

    private JPanel createHandPanel(JPanel buttonPanel, JScrollPane cardsScrollPane) {
        JPanel handPanel = new JPanel(new BorderLayout());
        handPanel.setBackground(new Color(191, 11, 17));
        handPanel.add(buttonPanel, BorderLayout.SOUTH); // Place buttonPanel at the bottom
        handPanel.add(cardsScrollPane);
        return handPanel;
    }

    private JButton createUnoButton() {
        JButton unoButton = new JButton("UNO!");
        // Format unoButton
        unoButton.setBackground(new Color(191, 11, 17));
        unoButton.setForeground(Color.BLACK);
        unoButton.setPreferredSize(new Dimension(60, 30));
        unoButton.setFocusable(false);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2); // Line border with width 2
        unoButton.setBorder(border);
        //unoButton.setEnabled(false);      //Change buttons availability to faslse
        return unoButton;
    }

    private JPanel createGamePanel() {
        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(new Color(123, 8, 12));
        return gamePanel;
    }

    private JSplitPane createMainPane(JPanel handPanel, JPanel gamePanel) {
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
        return mainPane;
    }
    

    // Method to show color selection dialog
    public String showColorSelectionDialog() {
        String[] colors = {"Red", "Green", "Blue", "Yellow"};
        String selectedColor = (String) JOptionPane.showInputDialog(
            frame,
            "Choose a color:",
            "Color Selection",
            JOptionPane.PLAIN_MESSAGE,
            null,
            colors,
            colors[0]
        );

        return selectedColor != null ? selectedColor.toLowerCase() : null;
    }

    // Method to trigger when a ChangeColor card is played
    public void playChangeColorCard(ChangeColorDecorator card) {
        String selectedColor = showColorSelectionDialog();
        if (selectedColor != null) {
            card.changeColor(selectedColor);
            updateMessageArea("Color changed to: " + selectedColor);
        }
    }

    // Update the message area with server updates
    public void updateMessageArea(String message) {
        if (messageArea == null) {
            messageArea = new JTextArea();
            messageArea.setEditable(false);
            frame.add(new JScrollPane(messageArea), BorderLayout.CENTER); // Add scrollable message area
        }
        messageArea.append("Update from server: " + message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength()); // Auto-scroll to bottom
    }

    public void updateCurrentPlayer(IPlayer serverPlayer) {
        clientPlayer = serverPlayer;
    }

    public int getCardYCoordinates(ICard card) {
        return 0;
    }

    public int getCardXCoordinates(ICard card) {
        return 0;

    }
}
