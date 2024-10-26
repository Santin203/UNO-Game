import java.util.ArrayList;
import java.util.Map;

public class Game implements IGame {
    private ArrayList<IPlayer> players;
    private IDeck discardPile;
    private IDeck gamePile;
    private IPlayer currentPlayer;
    private IPlayer previousPlayer;
    private String currentColor;
    private String currentNumber;
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
        currentColor = discardPile.getTopCard().getColor();
        currentNumber = discardPile.getTopCard().getValue();

        //Game loop
        while(true)
        {
            currentPlayer = players.get(playerIndex);
            nextTurn();
            previousPlayer = currentPlayer;
            playerIndex = players.size()%(playerIndex + order);
        }
    }

    @Override
    public void checkWinner() {
        // Logic to check if someone has won
    }

    @Override
    public void nextTurn() {
        // Logic for the next player's turn
        ArrayList<ICard> currentHand = currentPlayer.getHand();
        
        // Define player's options for current turn
        Map<String, Boolean> options = Map.of(
            "playCard", false, 
            "pickCard", false, 
            "challengeTake4", false, 
            "challengeUno", false,
            "callUno", false);

        // Check which cards can be played from the player's hand
        ArrayList<Integer> playableIndexes = new ArrayList<>();
        ICard currentTopCard = discardPile.getTopCard();
        
        for (int i = 0; i < currentHand.size(); i++) {
            ICard card = currentHand.get(i);
            
            // Instead of checking the type of card, we let each card determine if it can be played
            if (card.canBePlayed(currentTopCard)) {
                playableIndexes.add(i);
            }
        }

        // If cards are available to be played
        if (!playableIndexes.isEmpty()) {
            options.put("playCard", true);
        } 

        //Player can always pick a card
        options.put("pickCard", true);

        //Get action from player
        String action = currentPlayer.getAction(options);

        //Execute action from player
        switch(action)
        {
            case "pickCard" -> currentPlayer.drawCard(gamePile.giveCard());
            case "playCard" -> {
                //Add code to chose card from UI
                //Replace with logic
                int cardIndex = 0;
                //Play card at selected index
                currentPlayer.playCard(currentHand.get(cardIndex), this);
            }
            default -> {
            }
        }

    }

    @Override
    public IDeck getDeck(String deck){
        switch(deck)
        {
            case "discard":
                return gamePile;
            case "game":
                return discardPile;
            default:
                return discardPile;
        }
    }

}
