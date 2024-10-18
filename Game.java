import java.util.ArrayList;

public class Game implements IGame {
    private ArrayList<IPlayer> players;
    private IDeck discardPile;
    private IPlayer currentPlayer;

    public Game(ArrayList<IPlayer> players) {
        this.players = players;
        this.discardPile = new Deck();
    }

    @Override
    public void startGame() {
        // Logic to start the game
    }

    @Override
    public void checkWinner() {
        // Logic to check if someone has won
    }

    @Override
    public void nextTurn() {
        // Logic for the next player's turn
    }
}
