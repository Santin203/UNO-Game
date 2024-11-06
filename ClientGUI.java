import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class ClientGUI {
    private JFrame frame;
    private JPanel gamePanel;
    private JPanel handPanel;
    private JPanel buttonPanel;
    private JPanel cardsPanel;
    private JPanel playersPanel;
    private JPanel topCardPanel;
    private JPanel gameCardsPanel;
    private JScrollPane cardsScrollPane;
    private JScrollPane playerScrollPane;
    private JButton startButton;
    private JButton unoButton;
    private JButton challengeUnoButton;
    private JButton pickCardButton;
    private JTextArea messageArea;
    private JSplitPane mainPane;

    private IPlayer clientPlayer;
    public Client client;
    private Map<String, Integer> playerInfo = new HashMap<>();
    private ArrayList<Integer> playableIndexes = new ArrayList<>(){};

    public ClientGUI(Client client, IPlayer player) {
        this.client = client;
        this.clientPlayer = player;
        initializeGUI();
    }

    private void initializeGUI() {
        createFrame();
        
        createButtonPanel();
        createCardsPanel();
        cardsScrollPane = createScrollablePane(cardsPanel, false, 0, 0);
        createHandPanel(buttonPanel, cardsScrollPane);

        createUnoButton();
        createStartButton();
        createChallengeUnoButton();

        createPlayerGrid();
        playerScrollPane = createScrollablePane(playersPanel, true, 300, 200);

        createGamePanel();
        createGameCardsPanel();
        createPickCardButton();
        createTopCardPanel(null);
        createMessageArea();
        gamePanel.add(playerScrollPane, BorderLayout.EAST);

        createMainPane(handPanel, gamePanel);
        frame.add(mainPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void createFrame() {
        frame = new JFrame("UNO Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());
        frame.setIconImage(openImage("UNO_Cards.png").getSubimage(320, 192, 32, 48));
    }

    private void createCardsPanel() {
        cardsPanel = new JPanel(new GridBagLayout());
        cardsPanel.setBackground(new Color(191, 11, 17));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;

        ArrayList<ICard> currentHand = clientPlayer.getHand();

        if (currentHand.isEmpty()) {
            return;
        }

        BufferedImage spriteSheet = openImage("UNO_Cards.png");
        for (int i = 0; i < currentHand.size(); i++) {
            int xCoordinates = getCardXCoordinates(currentHand.get(i));
            int yCoordinates = getCardYCoordinates(currentHand.get(i));
            int cardIndex = i;
            JButton button = new JButton(new ImageIcon(spriteSheet.getSubimage(xCoordinates, yCoordinates, 32, 48)
                    .getScaledInstance(32, 48, Image.SCALE_SMOOTH)));
            button.setPreferredSize(new Dimension(64, 96));
            button.setBackground(new Color(0, 0, 0));
            if(!playableIndexes.isEmpty()) {
                if(playableIndexes.contains(-1))
                {
                    button.setEnabled(false);;
                }
                else if(!playableIndexes.contains(i)) {
                    button.setEnabled(false);
                }
            }
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (client != null) {
                        //button.setVisible(true);
                        //button.setEnabled(true);
                        //Send action to server
                        
                        client.actionSelected("playCard", cardIndex);
                    }
                }
            });
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            cardsPanel.add(button, gbc);
        }
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setPreferredSize(new Dimension(300, 100));
    }

    private JScrollPane createScrollablePane(JPanel panel, boolean customSize, int height, int width) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(customSize ? new Dimension(width, height) : new Dimension(300, scrollPane.getPreferredSize().height));
        return scrollPane;
    }

    private void createHandPanel(JPanel buttonPanel, JScrollPane contentScrollPane) {
        handPanel = new JPanel(new BorderLayout());
        handPanel.setBackground(new Color(191, 11, 17));
        handPanel.add(buttonPanel, BorderLayout.SOUTH);
        handPanel.add(contentScrollPane);
    }

    private void createUnoButton() {
        unoButton = new JButton("UNO!");
        formatButton(unoButton, 60, 30, new Color(196, 30, 30), new Color(255, 180, 19));
        unoButton.setVisible(false);
        unoButton.setEnabled(false);
        buttonPanel.add(unoButton);

        unoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    // unoButton.setVisible(false);
                    // unoButton.setEnabled(false);
                    //Send action to server
                    client.actionSelected("callUno", -1);
                }
            }
        });
    }

    private void createStartButton() {
        startButton = new JButton("Start Game");
        formatButton(startButton, 80, 40, new Color(196, 30, 30), new Color(255, 180, 19));
        startButton.setEnabled(false);
        buttonPanel.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    startButton.setVisible(false);
                    startButton.setEnabled(false);
                    Boolean startSignal = true;
                    client.sendToServer(startSignal);
                }
            }
        });
    }

    private void createChallengeUnoButton() {
        challengeUnoButton = new JButton("Punish UNO");
        formatButton(challengeUnoButton, 80, 40, new Color(196, 30, 30), new Color(255, 180, 19));
        challengeUnoButton.setVisible(false);
        challengeUnoButton.setEnabled(false);
        buttonPanel.add(challengeUnoButton);

        challengeUnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    // unoButton.setVisible(false);
                    // unoButton.setEnabled(false);
                    //Send action to server
                    client.actionSelected("callUno", -1);
                }
            }
        });
    }

    private void createPickCardButton() {
        BufferedImage spriteSheet = openImage("UNO_Cards.png");

        pickCardButton = new JButton(new ImageIcon(spriteSheet.getSubimage(320, 192, 32, 48)));
        pickCardButton.setPreferredSize(new Dimension(64, 96));
        pickCardButton.setBackground(new Color(0, 0, 0));
        pickCardButton.setVisible(false);
        pickCardButton.setEnabled(false);
        gameCardsPanel.add(pickCardButton, BorderLayout.NORTH);

        pickCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    // pickCardButton.setVisible(false);
                    // pickCardButton.setEnabled(false);
                    //Send action to server
                    client.actionSelected("pickCard", -1);
                }
            }
        });
    }

    private void formatButton(JButton button, int width, int height, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setPreferredSize(new Dimension(width, height));
        button.setFocusable(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(123, 8, 12));
    }

    private void createPlayerGrid() {
        playersPanel = new JPanel(new GridBagLayout());
        playersPanel.setBackground(new Color(24, 24, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;

        BufferedImage spriteSheet = openImage("UNO_Cards.png");
        int i = 0;
        for (var entry : playerInfo.entrySet()) {
            String name = entry.getKey();
            int cardAmount = entry.getValue();
            JPanel playerPanel = createPlayerPanel(name, cardAmount, spriteSheet);
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            playersPanel.add(playerPanel, gbc);
            i++;
        }
    }

    private JPanel createPlayerPanel(String playerName, int playerScore, BufferedImage spriteSheet) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(64, 136));
        panel.setBackground(new Color(24, 24, 24));
        
        JLabel imageLabel = new JLabel(new ImageIcon(spriteSheet.getSubimage(320, 192, 32, 48)
                .getScaledInstance(64, 96, Image.SCALE_SMOOTH)));
        JLabel numberLabel = new JLabel(String.valueOf(playerScore), SwingConstants.CENTER);
        JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        
        formatLabel(numberLabel, new Font("Arial", Font.BOLD, 16), Color.RED);
        formatLabel(nameLabel, new Font("Arial", Font.PLAIN, 12), Color.WHITE);
        
        panel.add(numberLabel, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(nameLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void formatLabel(JLabel label, Font font, Color color) {
        label.setFont(font);
        label.setForeground(color);
    }

    private void createTopCardPanel(ICard card) {
        topCardPanel = new JPanel(new BorderLayout());
        topCardPanel.setPreferredSize(new Dimension(64, 136));
        topCardPanel.setBackground(new Color(191, 11, 17));
        BufferedImage spriteSheet = openImage("UNO_Cards.png");
        JLabel imageLabel;
        if(card == null) {
            imageLabel = new JLabel(new ImageIcon(spriteSheet.getSubimage(320, 192, 32, 48)
                .getScaledInstance(64, 96, Image.SCALE_SMOOTH)));
        } else {
            int xCoordinates = getCardXCoordinates(card);
            int yCoordinates = getCardYCoordinates(card);
            imageLabel = new JLabel(new ImageIcon(spriteSheet.getSubimage(xCoordinates, yCoordinates, 32, 48)
                .getScaledInstance(64, 96, Image.SCALE_SMOOTH)));
        }
        topCardPanel.add(imageLabel);
        gameCardsPanel.add(topCardPanel, BorderLayout.CENTER);
    }

    private void createGameCardsPanel() {
        gameCardsPanel = new JPanel(new BorderLayout());
        gameCardsPanel.setPreferredSize(new Dimension(64, 136));
        gameCardsPanel.setBackground(new Color(191, 11, 17));
        gamePanel.add(gameCardsPanel, BorderLayout.CENTER);
    }

    private void createMainPane(JPanel handPanel, JPanel gamePanel) {
        mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, handPanel, gamePanel);
        mainPane.setResizeWeight(0.33);
        mainPane.setContinuousLayout(true);
        mainPane.setDividerSize(5);
        mainPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
            }
        });
    }

    private BufferedImage openImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Image file not found: " + path, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void createMessageArea() {
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(new Color(0, 0, 0, 200));
        messageArea.setForeground(Color.WHITE);
        gamePanel.add(new JScrollPane(messageArea), BorderLayout.SOUTH);
    }

    public void updateMessageArea(String message) {
        messageArea.append("Server: " + message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
        frame.revalidate();
        frame.repaint();
    }

    public void updatePlayerInfo(Map<String, Integer> players) {
        playerInfo = players;

        // Remove the old player panel
        gamePanel.remove(playerScrollPane);
        playerScrollPane = null;
        // Recreate player panel with updated info
        createPlayerGrid();
        playerScrollPane = createScrollablePane(playersPanel, true, 300, 200);

        // Add the updated player panel and messageArea to gamePanel
        gamePanel.add(playerScrollPane, BorderLayout.EAST);

        startButton.setVisible(false);
        startButton.setEnabled(false);
        // Refresh the frame to display updates
        frame.revalidate();
        frame.repaint();
    }

    public void updateStartButton(boolean startSignal) {
        startButton.setEnabled(startSignal);
        startButton.setVisible(startSignal);
        frame.revalidate();
        frame.repaint();
    }

    public void updateCurrentPlayer(IPlayer player) {
        clientPlayer = player;
        startButton.setVisible(false);
        startButton.setEnabled(false);
        updateCardsPanel();
    }

    public void updateCardsPanel() {
        // Remove the old cards panel from handPanel
        handPanel.remove(cardsScrollPane); // Assuming cardsPanel is the second component in handPanel
        // Recreate the cards panel with the updated info
        cardsScrollPane.removeAll();
        cardsPanel.removeAll();
        createCardsPanel();
        cardsScrollPane = createScrollablePane(cardsPanel, false, 0, 0);
    
        // Add the updated cards panel back to handPanel
        handPanel.add(cardsScrollPane, BorderLayout.CENTER);
        // Refresh the frame to display updates
        frame.revalidate();
        frame.repaint();
    }

    public void updatePlayableIndexes(ArrayList<Integer> serverPlayableIndexes) {
        playableIndexes = serverPlayableIndexes;
        updateCardsPanel();
    }

    public void updateTopCard(ICard topCard) {
        gameCardsPanel.remove(topCardPanel);
        createTopCardPanel(topCard);
        gameCardsPanel.add(topCardPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void updateActionButtons(List<String> options) {
        // Enable or gray out buttons based on options list
        unoButton.setEnabled(options.contains("callUno"));
        unoButton.setVisible(options.contains("callUno"));
        pickCardButton.setEnabled(options.contains("pickCard"));
        pickCardButton.setVisible(options.contains("pickCard"));
    }
    

    private int getCardXCoordinates(ICard card) {
        int coordinates = 0;
        if(card instanceof NumberDecorator numberDecorator) {
            String value = numberDecorator.getNumber();
            int integerValue = Integer.parseInt(value);
            coordinates = 32*integerValue;
        }
        else if(card instanceof SkipDecorator) {
            coordinates = 320;
        }
        else if(card instanceof ReverseDecorator) {
            coordinates = 352;
        }
        else if(card instanceof ChangeColorDecorator) {
            coordinates = 0;
        }
        else if(card instanceof DrawKDecorator drawKDecorator) {
            int drawCount = drawKDecorator.getDrawCount();
            if(drawCount == 2) {
                coordinates = 384;
            }
            else {
                coordinates = 160;
            }
        }
        return coordinates;
    }

    private int getCardYCoordinates(ICard card) {
        int coordinates = 0;
        String color = card.getColor();
        switch(color) {
            case "red":
                coordinates = 0;
                break;
            case "blue":
                coordinates = 48;
                break;
            case "yellow":
                coordinates = 96;
                break;
            case "green":
                coordinates = 144;
                break;
            case "black":
                coordinates = 192;
            default:
        }
        return coordinates;
    }
    // Method to show color selection dialog
    public String showColorSelectionDialog(String[] colors) {
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
    public void playChangeColorCard(ChangeColorDecorator card, String[] colors) {
        String selectedColor = showColorSelectionDialog(colors);
        if (selectedColor != null) {
            card.changeColor(selectedColor);
            updateMessageArea("Color changed to: " + selectedColor);
        }
    }
}
