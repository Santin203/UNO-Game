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
            //If current player has no more cards
            if(currentPlayer.checkStatus() == 1)
            {
                //Current player Won, game ends
                break;
            }
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
            case "pickCard" -> {
                //Draw card from gamePile
                currentPlayer.drawCard(gamePile.giveCard());
                //Check if card can be played inmediately
                if(currentHand.get(currentHand.size()-1).canBePlayed(currentTopCard))
                {
                    int playerAction = 0;
                    if(playerAction == 0)
                    {
                        currentPlayer.playCard(currentHand.get(currentHand.size()-1), this);
                    }
                    else
                    {
                        return;
                    }
                }
                }
            case "playCard" -> {
                boolean calledUNO = false;

                if(currentHand.size() == 2)
                {
                    options.put("callUno", true);
                    //Add logic to call UNO from UI
                }
                //Add code to chose card from UI
                //Replace with logic
                int cardIndex = playableIndexes.get(0);
                //Play card at selected index
                currentPlayer.playCard(currentHand.get(cardIndex), this);
                switch(currentHand.size())
                {
                    case 0:
                        //Won
                        currentPlayer.setStatus(1);
                    case 1:
                        if(calledUNO)
                        {
                            //Called UNO properly
                            currentPlayer.setStatus(2);
                        }
                        else
                        {
                            //Didn't call UNO
                            currentPlayer.setStatus(3);
                        }
                        break;
                    default:
                        //Still playing like normal
                        currentPlayer.setStatus(0);
                }
                return;
            }
            default -> {
                return;
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
