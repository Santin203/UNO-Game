import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private Map<String, Integer> playerInfo = new HashMap<>() {{
        put("1111",5); 
        put("2222",6); 
        put("3333",7);
        put("4444",5); 
        put("5555",6); 
        put("6666",7);
        put("7777",5); 
        put("8888",6); 
        put("9999",7);
        put("10101010",5); 
        put("11111111",6); 
        put("12121212",7);}};

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
        JScrollPane cardsScrollPane = createScrollablePane(cardsPanel, false, 0, 0);

        // Create panel for player's hand, containing buttonPanel
        JPanel handPanel = createHandPanel(buttonPanel, cardsScrollPane);

        // Create Button to Call Uno
        JButton unoButton = createUnoButton();
        //unoButton.setEnabled(false);      //Change buttons availability to faslse
        buttonPanel.add(unoButton);

        //Create Grid panel for other players
        JPanel playerPanel = createPlayerGrid();
        //Create Scrollable panel for other players
        JScrollPane playerScrollPane = createScrollablePane(playerPanel, true, 300, 200);

        // Create panel for other players, game pile, and discard pile
        JPanel gamePanel = createGamePanel();
        gamePanel.add(playerScrollPane, BorderLayout.EAST);
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        gamePanel.add(new JScrollPane(messageArea), BorderLayout.SOUTH); // Add scrollable message area
        messageArea.append("Test message: Test\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength()); // Auto-scroll to bottom

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

        for (int i = 0; i <= 80; i++) {
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

    private JScrollPane createScrollablePane(JPanel panel, boolean customSize, int height, int width) {
        JScrollPane contentScrollPane = new JScrollPane(panel);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        if (customSize) {
            contentScrollPane.setPreferredSize(new Dimension(width, height));
        }
        else {
            contentScrollPane.setPreferredSize(new Dimension(300, contentScrollPane.getPreferredSize().height));
        }
        contentScrollPane.setBackground(new Color(0, 0, 0));
        return contentScrollPane;
    }

    private JPanel createHandPanel(JPanel buttonPanel, JScrollPane contentScrollPane) {
        JPanel handPanel = new JPanel(new BorderLayout());
        handPanel.setBackground(new Color(191, 11, 17));
        handPanel.add(buttonPanel, BorderLayout.SOUTH); // Place buttonPanel at the bottom
        handPanel.add(contentScrollPane);
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
        JPanel gamePanel = new JPanel(new BorderLayout());
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
    public JPanel createPlayerGrid() {
        int playerWidth = 64;
        int playerHeight = 96;

        JPanel playersPanel = new JPanel(new GridBagLayout());
        playersPanel.setBackground(new Color(24,24,24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2); // Spacing between cells
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0; // Allow panel to grow horizontally

        BufferedImage spriteSheet = openImage("UNO_Cards.png");
        int i = 0;
        for (String key: playerInfo.keySet()) {
            // Get the image for each player (adjust the subimage extraction as needed)
            BufferedImage playerImage = spriteSheet.getSubimage(320, 192, 32, 48); // Modify dimensions as needed
            ImageIcon playerIcon = new ImageIcon(playerImage.getScaledInstance(playerWidth, playerHeight, Image.SCALE_SMOOTH));

            // Create a panel for the player with overlay text
            JPanel playerPanel = new JPanel() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(playerWidth, playerHeight + 40); // Extra space for labels
                }
            };
            playerPanel.setLayout(new BorderLayout());
            playerPanel.setBackground(new Color(24, 24, 24)); // Same background color

            // Create the image label
            JLabel imageLabel = new JLabel(playerIcon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Create the number label (over the image)
            JLabel numberLabel = new JLabel(String.valueOf(playerInfo.get(key))); // Replace with your desired number
            numberLabel.setFont(new Font("Arial", Font.BOLD, 16));
            numberLabel.setForeground(Color.RED);
            numberLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Create the name label (under the image)
            JLabel nameLabel = new JLabel(key); // Replace with your desired name
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Add the labels to the player panel
            playerPanel.add(imageLabel, BorderLayout.CENTER);
            playerPanel.add(numberLabel, BorderLayout.NORTH); // Number above the image
            playerPanel.add(nameLabel, BorderLayout.SOUTH); // Name below the image

            // Set fixed size for the player panel
            playerPanel.setPreferredSize(new Dimension(playerWidth, playerHeight));

            // Calculate column and row based on index
            gbc.gridx = i % 2; // Column index
            gbc.gridy = i / 2; // Row index
            playersPanel.add(playerPanel, gbc);
            i += 1;
        }
        return playersPanel;
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
            frame.add(new JScrollPane(messageArea), BorderLayout.SOUTH); // Add scrollable message area
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
