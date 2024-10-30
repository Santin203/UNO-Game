import java.io.*;
import java.net.*;
import java.util.Random;

public class Client implements Observer {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private IPlayer currentPlayer;
    private ClientGUI gui;

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
        if(message instanceof String)
        {
            String serverMessage = (String) message;
            gui.updateMessageArea(serverMessage);
        }
        else if (message instanceof Player)
        {
            IPlayer updatedPlayer = (Player) message;
            gui.updateCurrentPlayer(updatedPlayer);
        }
          // Update GUI with messages from the server
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = (String) input.readObject();
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

        ClientGUI gui = new ClientGUI(null);  // Create GUI without client first
        Client client = new Client("localhost", 12345, gui, player);  // Pass GUI and Player to client

        gui.client = client;  // Link client to the GUI
    }

    public static String generateUserId() {
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
