import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class ClientGUI {
    private JFrame frame ;
    private JPanel gamePanel;
    private JPanel handPanel;
    private JButton startButton;    
    private IPlayer clientPlayer;
    private JTextArea messageArea;
    public Client client;  
    private Map<String, Integer> playerInfo = new HashMap<>() ;

    public ClientGUI(Client client, IPlayer player) {
        this.client = client;
        this.clientPlayer = player;
        initializeGUI();
    }

    private void initializeGUI() {
        createFrame();
        
        JPanel buttonPanel = createButtonPanel();
        JPanel cardsPanel = createCardsPanel();
        JScrollPane cardsScrollPane = createScrollablePane(cardsPanel, false, 0, 0);
        createHandPanel(buttonPanel, cardsScrollPane);
        JButton unoButton = createUnoButton();
        
        buttonPanel.add(unoButton);
        startButton = createStartButton();
        buttonPanel.add(startButton);

        // Add action listener to start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    startButton.setVisible(false);
                    startButton.setEnabled(false);
                    Boolean startSignal = true;
                    client.sendToServer(startSignal);  // Send start signal to server
                }
            }
        });

        JPanel playerPanel = createPlayerGrid();
        JScrollPane playerScrollPane = createScrollablePane(playerPanel, true, 300, 200);

        createGamePanel();
        setMessageArea();
        gamePanel.add(playerScrollPane, BorderLayout.EAST);

        JSplitPane mainPane = createMainPane(handPanel, gamePanel);
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

    private JPanel createCardsPanel() {
        JPanel cardsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;

        ArrayList<ICard> currentHand = clientPlayer.getHand();

        //If player isn't initialized
        if(currentHand.isEmpty()) {
            return cardsPanel;
        }

        BufferedImage spriteSheet = openImage("UNO_Cards.png");
        for (int i = 0; i < currentHand.size(); i++) {
            int xCoordinates = getCardXCoordinates(currentHand.get(i));
            int yCoordinates = getCardYCoordinates(currentHand.get(i));
            JButton button = new JButton(new ImageIcon(spriteSheet.getSubimage(xCoordinates, yCoordinates, 32, 48)
                    .getScaledInstance(32, 48, Image.SCALE_SMOOTH)));
            button.setPreferredSize(new Dimension(64, 96));
            button.setBackground(new Color(0, 0, 0));
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            cardsPanel.add(button, gbc);
        }
        return cardsPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setPreferredSize(new Dimension(300, 100));
        return buttonPanel;
    }

    private JScrollPane createScrollablePane(JPanel panel, boolean customSize, int height, int width) {
        JScrollPane contentScrollPane = new JScrollPane(panel);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScrollPane.setPreferredSize(customSize ? new Dimension(width, height) : new Dimension(300, contentScrollPane.getPreferredSize().height));
        return contentScrollPane;
    }

    private void createHandPanel(JPanel buttonPanel, JScrollPane contentScrollPane) {
        handPanel = new JPanel(new BorderLayout());
        handPanel.setBackground(new Color(191, 11, 17));
        handPanel.add(buttonPanel, BorderLayout.SOUTH);
        handPanel.add(contentScrollPane);
    }

    private JButton createUnoButton() {
        JButton unoButton = new JButton("UNO!");
        formatButton(unoButton, 60, 30, new Color(196, 30, 30), Color.BLACK);
        unoButton.setVisible(false);
        unoButton.setEnabled(false);
        return unoButton;
    }

    private JButton createStartButton() {
        startButton = new JButton("Start Game");
        formatButton(startButton, 80, 40, new Color(196, 30, 30), new Color(255, 180, 19));
        startButton.setEnabled(false);
        return startButton;
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

    private JPanel createPlayerGrid() {
        JPanel playersPanel = new JPanel(new GridBagLayout());
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
        return playersPanel;
    }

    private JPanel createPlayerPanel(String playerName, int playerScore, BufferedImage spriteSheet) {
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setPreferredSize(new Dimension(64, 136));
        playerPanel.setBackground(new Color(24, 24, 24));
        
        JLabel imageLabel = new JLabel(new ImageIcon(spriteSheet.getSubimage(320, 192, 32, 48)
                .getScaledInstance(64, 96, Image.SCALE_SMOOTH)));
        JLabel numberLabel = new JLabel(String.valueOf(playerScore), SwingConstants.CENTER);
        JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        
        formatLabel(numberLabel, new Font("Arial", Font.BOLD, 16), Color.RED);
        formatLabel(nameLabel, new Font("Arial", Font.PLAIN, 12), Color.WHITE);
        
        playerPanel.add(numberLabel, BorderLayout.NORTH);
        playerPanel.add(imageLabel, BorderLayout.CENTER);
        playerPanel.add(nameLabel, BorderLayout.SOUTH);
        return playerPanel;
    }

    private void formatLabel(JLabel label, Font font, Color color) {
        label.setFont(font);
        label.setForeground(color);
    }

    private JSplitPane createMainPane(JPanel handPanel, JPanel gamePanel) {
        JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, handPanel, gamePanel);
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
        return mainPane;
    }

    private BufferedImage openImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Image file not found: " + path, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void setMessageArea() {
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
        gamePanel.remove(1);

        // Recreate player panel with updated info
        JPanel updatedPlayerPanel = createPlayerGrid();
        JScrollPane playerScrollPane = createScrollablePane(updatedPlayerPanel, true, 300, 200);

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
        handPanel.remove(1); // Assuming cardsPanel is the second component in handPanel
    
        // Recreate the cards panel with the updated info
        JPanel updatedCardsPanel = createCardsPanel();
        JScrollPane updatedCardsScrollPane = createScrollablePane(updatedCardsPanel, false, 0, 0);
    
        // Add the updated cards panel back to handPanel
        handPanel.add(updatedCardsScrollPane, BorderLayout.CENTER);
        // Refresh the frame to display updates
        frame.revalidate();
        frame.repaint();
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
            coordinates = 160;
        }
        else if(card instanceof DrawKDecorator drawKDecorator) {
            int drawCount = drawKDecorator.getDrawCount();
            if(drawCount == 2) {
                coordinates = 384;
            }
            else {
                coordinates = 0;
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

    public String showActionOptions(List<String> options) {
        String selectedAction = (String) JOptionPane.showInputDialog(
            frame,
            "Choose an action:",
            "Select Action",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options.toArray(),
            options.get(0)  // Default to the first option
        );
        return selectedAction;
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
