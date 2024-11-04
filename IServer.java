import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IServer {

    // Initialize server with a port and return the instance
    IServer getInstance(int port) throws IOException;

    // Start the server and handle incoming connections
    void start();

    // Add a player to the server
    void addPlayer(IPlayer player);

    // Remove a player from the server
    void removePlayer(String playerName);

    // Notify all observers with a message
    void notifyObservers(String message);

    // Add an observer (client) to the observer list
    void addObserver(Observer observer);

    // Remove an observer (client) from the observer list
    void removeObserver(Observer observer);

    // Send current state of all players to observers
    void sendPlayers(ArrayList<IPlayer> players);

    // Send the top play card to all clients
    void sendTopPlayCard(ICard card);

    // Additional game-specific actions (if needed)
    void startGame();

    // Retrieve list of observers
    Map<String, Observer> getObservers();

    // Retrieve current player list
    ArrayList<IPlayer> getPlayers();

    String getPlayerAction(IPlayer player, List<String> options);
}

