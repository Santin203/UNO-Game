import java.io.*;
import java.net.*;

public class Client implements Observer {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ClientGUI gui;

    public Client(String address, int port, ClientGUI gui) {
        this.gui = gui;  // Reference to the GUI
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

    public void sendToServer(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String message) {
        gui.updateMessageArea(message);  // Update GUI with messages from the server
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
        ClientGUI gui = new ClientGUI(null);  // Create GUI without client first
        Client client = new Client("localhost", 12345, gui);  // Pass GUI to client
        gui.client = client;  // Link client to the GUI
    }
}
