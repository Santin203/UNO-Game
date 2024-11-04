import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Client implements Observer {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private IPlayer currentPlayer;
    private ClientGUI gui;
    private String lastAction;  // Store last action chosen by the client

    public Client(String address, int port, ClientGUI gui, IPlayer player) {
        this.gui = gui;  // Reference to the GUI
        this.currentPlayer = player;
        try {
            socket = new Socket(address, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to UNO server!");

            new Thread(new ServerListener()).start();  // Start listening for server updates
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to server.");
        }
    }

    public void sendToServer(Object message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Object message) {
        if(message instanceof String serverMessage) {
            if(serverMessage.equals("NAME_REQUEST")) {
                try {
                    output.writeObject("NAME_REQUEST "+currentPlayer.getName());  // Sends current player's name to server
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (serverMessage.equals("PLAYER_REQUEST")) {
                sendToServer(currentPlayer);
            }
            else {
                gui.updateMessageArea(serverMessage);
            }
        }
        else if (message instanceof IPlayer updatedPlayer) {
            gui.updateCurrentPlayer(updatedPlayer);
        }
        else if (message instanceof Boolean startSignal) {
            gui.updateStartButton(startSignal);
        }
        else if (message instanceof Map<?,?>) {
            // Cast to Map<String, Integer> if the map is of that type
            try {
                gui.updatePlayerInfo((Map<String, Integer>) message);
            } catch (ClassCastException e) {
                System.err.println("Received map is not of type Map<String, Integer>");
                e.printStackTrace();
            }
        }
        else if(message instanceof ICard topCard) {
            gui.updateTopCard(topCard);
        }
        else if(message instanceof List<?> options) {
            // Prompt the player and get the action
            String action = gui.promptAction((List<String>) options);
            lastAction = action;  // Store the chosen action

            // Send the selected action back to the server
            sendToServer(action);

            // Notify the server that the action has been set
            synchronized(this) {
                this.notify();
            }
        }
        else {
            System.out.println("Received unknown message from server: " + message);
        }
        else if(message instanceof ArrayList<?>) {
            gui.updatePlayableIndexes((ArrayList<Integer>) message);
        }
        // Update GUI with messages from the server
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Object message = input.readObject();
                    System.out.println("Received from server: " + message);
                    update(message);  // Call the update method
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        //Get unique ID for player and create new Player instance
        String userID = generateUserId();
        IPlayer player = new Player(userID);

        ClientGUI gui = new ClientGUI(null, player);  // Create GUI without client first
        Client client = new Client("localhost", 12345, gui, player);  // Pass GUI and Player to client

        gui.client = client;  // Link client to the GUI
    }

    public String getLastAction() {
        return lastAction;
    }

    private static String generateUserId() {
        Random random = new Random();
        // Generates a random integer between 0 and 999
        int userID1 = random.nextInt(1000);
        int userID2 = random.nextInt(1000);
        int userID3 = random.nextInt(1000);

        String userID1Text = String.valueOf(userID1);
        String userID2Text = String.valueOf(userID2);
        String userID3Text = String.valueOf(userID3);

        String userID = String.join("",userID1Text,userID2Text,userID3Text);
        return userID;
    }
    public IPlayer getPlayer() {
        return currentPlayer;
    }
}
