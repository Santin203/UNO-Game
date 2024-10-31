import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

// Singleton Server class
public class Server implements Observable {
    private static Server instance = null;  // Singleton instance
    private ArrayList<IPlayer> players = new ArrayList<>();
    private IGame game;
    private ServerSocket serverSocket;
    private List<Observer> observers = new ArrayList<>(); // List of observers (clients)

    // Private constructor (Singleton)
    private Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("UNO Server started on port " + port);
    }

    // Singleton getInstance method
    public static Server getInstance(int port) throws IOException {
        if (instance == null) {
            instance = new Server(port);
        }
        return instance;
    }

    // Start the server
    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                PlayerHandler playerHandler = new PlayerHandler(clientSocket);
                new Thread(playerHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Broadcast message to all clients
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);  // Send update to all clients
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);  // Add client as an observer
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);  // Remove client from observers list
    }

    // PlayerHandler class for handling individual players
    private class PlayerHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;

        public PlayerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ClientObserver clientObserver = null;
            try {
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
        
                // Add this client as an observer
                clientObserver = new ClientObserver(output);
                addObserver(clientObserver);
        
                // Handle game logic, such as receiving cards, moves, etc.
                while (true) {
                    //Read object from client
                    Object clientMessage = input.readObject();
                    //If object is a string
                    if (clientMessage instanceof String message) {
                        //Deserialize
                        System.out.println("Received string from client: " + message);
                        notifyObservers(message);
                    //If object is client's currentPlayer
                    } else if (clientMessage instanceof IPlayer clientPlayer) {
                        //Deserialize
                        System.out.println("Received player from client: " + clientPlayer.getName());
                        //Add to players if not found, replace old version if found
                        updatePlayers(clientPlayer);
                        if(players.size() >= 2) {
                            giveStartSignal();
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                // Clean up when a player disconnects
                if (clientObserver != null) {
                    removeObserver(clientObserver);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

    private void giveStartSignal() {
        Boolean startSignal = true;
        for (Observer observer : observers) {
            observer.update(startSignal);  // Send update to all clients
        }
    }

    private void updatePlayers(IPlayer clientPlayer) {
        int playerIndex = -1;
        //Search for player in player list
        for (IPlayer playerInList : players) {
            //Replace current instance if already in List
            if(playerInList.getName().equals(clientPlayer.getName()))
            {
                playerIndex = players.indexOf(playerInList);
                break;
            }
        }
        //If not found in list
        if(playerIndex == -1)
        {
            //Add at the end
            players.add((players.size() -1), clientPlayer);
            notifyObservers("New Player: " + clientPlayer.getName() + " connected!");
        }
        else
        {
            //Add at found index
            players.add(playerIndex, clientPlayer);
        }
    }

    // Inner class for ClientObserver, implements Observer
    private class ClientObserver implements Observer {
        private ObjectOutputStream output;

        public ClientObserver(ObjectOutputStream output) {
            this.output = output;
        }

        @Override
        public void update(Object message) {
            try {
                output.writeObject(message);  // Send message to client
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Server server = Server.getInstance(12345);  // Singleton instance
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Observable interface
interface Observable {
    void notifyObservers(String message);
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
}

// Observer interface
interface Observer {
    void update(Object message);
}
