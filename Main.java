
import java.util.ArrayList;



public class Main {
    public static void main(String[] args) {
        // Create players
        ArrayList<IPlayer> players = new ArrayList<>();
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));
        players.add(new Player("Charlie"));

        // Create game instance
        Game game = new Game(players);

        // Start the game
        game.startGame();
    }
}

