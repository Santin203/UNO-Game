import java.io.*;
import java.net.*;

// Client class with Observer implementation
public class Client implements Observer {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Connected to UNO server!");

            // Start listening for server updates
            new Thread(new ServerListener()).start();

            // Simulate sending messages to the server (e.g., playing a card)
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Enter message (e.g., played card): ");
                String message = reader.readLine();
                sendToServer(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Send a message to the server
    public void sendToServer(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update method (Observer pattern)
    @Override
    public void update(String message) {
        System.out.println("Update from server: " + message);  // Display server updates
    }

    // Inner class to listen for updates from the server
    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = (String) input.readObject();
                    System.out.println("Received from server: " + message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 12345);  // Connect to server
    }
}

