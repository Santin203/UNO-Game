import java.util.ArrayList;

public class Game implements IGame {
    private ArrayList<IPlayer> players;
    private IDeck discardPile;
    private IDeck gamePile;
    private IPlayer currentPlayer;
    private String currentColor;
    private int currentNumber;
    private int order;

    public Game(ArrayList<IPlayer> players) {
        this.players = players;
        this.discardPile = Deck.buildDeck(0);
        this.order = 1;
    }

    @Override
    public void startGame() {
        // Logic to start the game

        double decksN = players.size()/10;
        gamePile = Deck.buildDeck((int)decksN);
        gamePile.shuffleCards();

        for(int i = 0; i < 8; i++)
        {
            //Deal cards to players, 7 each
            for (IPlayer player : players)
            {
                player.drawCard(gamePile.giveCard());
            }
        }
        int playerIndex = 0;

        //Get initial card
        discardPile.addCard(gamePile.giveCard());
        
        //Set initial color and numbers for the game
        currentColor = discardPile.getTopColor();
        currentNumber = discardPile.getTopNumber();

        //Game loop
        while(true)
        {
            currentPlayer = players.get(playerIndex);
            nextTurn();
        }
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
