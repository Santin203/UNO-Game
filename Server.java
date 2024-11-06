import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements Observable {
    private static Server instance = null;
    private ArrayList<IPlayer> players = new ArrayList<>();
    private Map<String, Integer> playerInfo = new HashMap<>();
    private IGame game;
    private ServerSocket serverSocket;
    private Map<String, Observer> observers = new HashMap<>();
    private String lastAction; 
    private String selectedColor;
    public Integer cardIndex;
    private final Object actionLock = new Object();
    private final Object colorLock = new Object();

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
        for (var entry : observers.entrySet()) {
            Observer observer = entry.getValue();
            observer.update(message);  // Send update to all clients
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observer.update("NAME_REQUEST"); //Request name to observer to add into list
    }

    @Override
    public void removeObserver(Observer observer) {
        for(var entry: observers.entrySet()) {
            if(entry.getValue() == observer)
            {
                //Remove player from dictionary with names and hand sizes
                removePlayerInfo(entry.getKey());
                //Remove player from list with all player instances
                removePlayerFromList(entry.getKey());
                //Remove player from game's list of players
                for(IPlayer player : players) {
                    if(entry.getKey().equals(player.getName())) {
                        game.removePlayer(player);
                    }
                }
                observers.remove(entry.getKey());  // Remove client from observers list
            }
        }
        
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
                        //If incoming message is an answer to a name request
                        if(message.contains("NAME_REQUEST")) {
                            String playerName = message.replace("NAME_REQUEST","").trim();
                            observers.put(playerName, clientObserver);
                            clientObserver.update("PLAYER_REQUEST");    //Request player instance from client
                        } else if (message.contains("playCard") || message.contains("pickCard") || message.contains("callUno") || message.contains("challengeUno")) {
                            //notifyObservers(message);
                            if(message.contains("playCard")) {
                                String stringIndex = message.replace("playCard","").trim();
                                cardIndex = Integer.parseInt(stringIndex);
                                System.out.println("Received action from client: " + message);
                                message = "playCard";
                            }
                            lastAction = message;
                            synchronized(actionLock) {
                                actionLock.notify();  // Notify that an action is ready
                            }
                        } else if (message.contains("COLOR_CHANGE")) {
                            selectedColor = message.replace("COLOR_CHANGE","").trim();
                            synchronized(colorLock) {
                                colorLock.notify();  // Notify that an action is ready
                            }

                        } else {
                            System.out.println("Received string from client: " + message);
                            notifyObservers(message);
                        }
                    //If object is client's currentPlayer
                    } else if (clientMessage instanceof Player clientPlayer) {
                        System.out.println("Received player from client: " + clientPlayer.getName());
                        //Add to players if not found, replace old version if found
                        addPlayers(clientPlayer);
                        if(players.size() >= 2) {
                            giveStartSignal();
                        }
                    } else if (clientMessage instanceof Boolean) {
                        notifyObservers("Game has started!");
                        startGame();
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

    private void startGame() {
        game = Game.getInstance(players);
        game.setGameServer(instance);
        // Run the game on a new thread to allow continued message handling
        new Thread(() -> game.startGame()).start();
    }

    private void removePlayerFromList(String playerName) {
        for (IPlayer playerInList : players) {
            //Replace current instance if already in List
            if(playerInList.getName().equals(playerName))
            {
                players.remove(playerInList);
                removePlayerInfo(playerName);
                break;
            }
        }
    }

    private void removePlayerInfo(String message) {
        if(playerInfo.containsKey(message)) {
            playerInfo.remove(message);
        }
    }

    private void giveStartSignal() {
        Boolean startSignal = true;
        for (var entry : observers.entrySet()) {
            Observer observer = entry.getValue();
            observer.update(startSignal);  // Send update to all clients
        }
    }

    private void addPlayers(IPlayer clientPlayer) {
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
            players.add(clientPlayer);
            notifyObservers("New Player: " + clientPlayer.getName() + " connected!");
        }
        else
        {
            //Add at found index
            players.add(playerIndex, clientPlayer);
        }

        addPlayerInfo();
    }
    private void addPlayerInfo() {
        for(IPlayer playerInList: players) {
            Integer handSize = playerInList.getHand().size();
            playerInfo.put(playerInList.getName(), handSize);
        }
    }

    public void sendPlayableIndexes(ArrayList<Integer> playableIndexes, String playerName) {
        //Search Client in Observers dictionary by name
        Observer observer = observers.get(playerName);
        observer.update(playableIndexes);
        List<Integer> disabledArray = new ArrayList<Integer>();
        disabledArray.add(-1);
        for(String observerName: observers.keySet()) {
            if(!(observerName.equals(playerName))) {
                Observer notCurrentPlayer = observers.get(observerName);
                notCurrentPlayer.update(disabledArray);
            }
        }
    }
    
    public void sendPlayers(ArrayList<IPlayer> players) {
        for(String observerName : observers.keySet()) {
            IPlayer currentPlayer = new Player("");

            Map <String, Integer> otherPlayers = new HashMap<>();
            for(IPlayer player : players) {
                if(!(observerName.equals(player.getName()))) {
                    otherPlayers.put(player.getName(), player.getHandSize());
                }
                else {
                    //Send current player instance back
                    currentPlayer = player;
                }
            }
            //Send dictionary with other players to current observer
            observers.get(observerName).update(otherPlayers);
            //Send updated player instance to client
            observers.get(observerName).update(currentPlayer);
        }
    }

    public void sendTopPlayCard(ICard card) {
        for (var entry : observers.entrySet()) {
            Observer observer = entry.getValue();
            observer.update(card);
        }
    }

    public String getPlayerAction(IPlayer player, List<String> options) {
        ClientObserver observer = (ClientObserver) observers.get(player.getName());

        observer.update(options);  // Send options to the client
        // Synchronize and wait for client's response
        synchronized(actionLock) {
            try {
                actionLock.wait();  // Wait for the client to notify with the selected action
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return lastAction;
    }

    public String getPlayerColor(IPlayer player, String[] colors) {
        ClientObserver observer = (ClientObserver) observers.get(player.getName());
        
        observer.update(colors);
        synchronized(colorLock) {
            try {
                colorLock.wait();  // Wait for the client to notify with the selected color
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return selectedColor;
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
                output.reset();  // Clears the cache, forcing a full serialization next time
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
