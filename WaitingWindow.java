import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class WaitingWindow {
    private JDialog waitingDialog;  // Dialog for waiting window
    private JLabel waitingLabel;     // Label to display number of clients
    private JButton startGameButton; // Button to start the game

    public WaitingWindow(Frame owner, ActionListener startGameListener) {
        waitingDialog = new JDialog(owner, "Waiting for Players", true);
        waitingDialog.setSize(300, 150);
        waitingDialog.setLayout(new BorderLayout());

        waitingLabel = new JLabel("Connected clients: 0", SwingConstants.CENTER);
        waitingDialog.add(waitingLabel, BorderLayout.CENTER);

        startGameButton = new JButton("Start Game");
        startGameButton.setEnabled(false); // Initially disabled
        waitingDialog.add(startGameButton, BorderLayout.SOUTH);

        startGameButton.addActionListener(startGameListener); // Link listener for starting the game

        waitingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        waitingDialog.setVisible(true);
    }

    // Update the waiting window with the number of connected clients
    public void updateWaitingClients(int count) {
        waitingLabel.setText("Connected clients: " + count);
        startGameButton.setEnabled(count > 1); // Enable button if more than 1 client is connected
    }

    // Method to close the waiting window
    public void close() {
        waitingDialog.dispose();
    }
}
