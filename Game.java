import java.util.ArrayList;
import java.util.Map;

public class Game implements IGame {
    private ArrayList<IPlayer> players;
    private IDeck discardPile;
    private IDeck gamePile;
    private IPlayer currentPlayer;
    private IPlayer previousPlayer;
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
        currentColor = discardPile.getTopCard().getColor();
        currentNumber = discardPile.getTopCard().getValue();

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
        ArrayList<ICard> currentHand = currentPlayer.getHand();
        
        //Define player's options for current turn
        Map<String, Boolean> options = Map.of(
            "playCard", false, 
            "pickCard", false, 
            "challengeTake4", false, 
            "challengeUno", false);

        //If last card played was a Wild Take 4
        if(gamePile.getTopCard() instanceof WildTake4Card)
        {
            //Give option to challeng it
            //If the previous player had cards it could have played, that player takes 4 instead.
            //If it did not had nay cards to play, the current player takes 4 + 2 of penalty.
            options.put("challengeTake4", true);
        }

        //Check which cards can be played from player's hand
        ArrayList<Integer> playableIndexes = new ArrayList<>();
        ICard currentTopCard = discardPile.getTopCard();
        for(int i = 0; i < currentHand.size(); i++)
        {
            //If ith card is wild or wild take 4,
            //add to playeable list, wild and wild take 4 are always playeable
            if(currentHand.get(i) instanceof WildCard
            || currentHand.get(i) instanceof WildTake4Card)
            {
                playableIndexes.add(i);
            }
            //If same color as top card,
            //add to playeable list
            else if(currentHand.get(i).getColor().equals(currentTopCard.getColor()))
            {
                playableIndexes.add(i);
            }
            //If same value as top card and not wild take 4,
            //add to playable list
            else if(currentHand.get(i).getValue() == currentTopCard.getValue()
            && currentTopCard.getValue() > -4)
            {
                playableIndexes.add(i);
            }
        }

        //If cards are available to be played
        if(!playableIndexes.isEmpty())
        {
            options.put("playCard", true);
        }
    }
}
