import java.util.ArrayList;
import java.util.List;

public class Game implements IGame {
    private static Game instance;
    private ArrayList<IPlayer> players;
    private IDeck discardPile;
    private IDeck gamePile;
    private IPlayer currentPlayer;
    private IPlayer previousPlayer;
    private int playerIndex;
    private int order;
    private Server gameServer;

    public Game(ArrayList<IPlayer> players) {
        this.players = players;
        this.discardPile = Deck.buildDeck(0);
        this.playerIndex = 0;
        this.order = 1;
        this.gameServer = null;
    }

    public static Game getInstance(ArrayList<IPlayer> players) {
        if (instance == null) {
            instance = new Game(players);
        }
        return instance;
    }

    public void setGameServer(Server serverInstance) {
        this.gameServer = serverInstance;
    }

    @Override
    public void startGame() {
        setupGamePile();
        dealInitialCards();        

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
            increasePlayerIndex();
        }
    }

    private void setupGamePile() {
        int deckCount = Math.max(1, (players.size() + 9) / 10);
        gamePile = Deck.buildDeck(deckCount);
        ICard initialCard;
        gamePile.shuffleCards();
        
        do {

            initialCard = gamePile.giveCard();
        } while(initialCard instanceof ChangeColorDecorator || initialCard instanceof DrawKDecorator || initialCard instanceof ReverseDecorator || initialCard instanceof SkipDecorator);

        discardPile.addCard(initialCard);
    }

    private void dealInitialCards() {
        for (int i = 0; i < 7; i++) {
            for (IPlayer player : players) {
                player.drawCard(gamePile.giveCard());
            }
        }
    }

    @Override
    public void checkWinner() {
        // Logic to check if someone has won
    }

    @Override
    public void nextTurn() {
        ArrayList<ICard> currentHand = currentPlayer.getHand();
        ICard topCard = discardPile.getTopCard();
        
        // Define player's options for current turn
        List<String> options = getAvailableOptions();

        //Get indexes of playable cards
        ArrayList<Integer> playableIndexes = getPlayableIndexes(currentHand, topCard);

        //Get action from player
        String action = currentPlayer.getAction(options);

        //Execute action from player
        switch(action)
        {
            case "pickCard" -> {
                handlePickCard(currentHand, topCard);

            }
            case "playCard" -> {
                handlePlayCard(topCard);

            }
            default -> {
                System.out.println("Invalid action");
                return;
            }
        }

    }

    private List<String> getAvailableOptions() {
        List<String> options = new ArrayList<>();
        if (currentPlayer.hasPlayableCard(discardPile.getTopCard())) {
            options.add("playCard");
        }
        if (checkUnoPlayers()) {
            options.add("callUno");
        }
        options.add("pickCard");
        return options;
    }

    private boolean checkUnoPlayers() {
        for (IPlayer player : players) {
            if (player.needsToCallUno() && !player.hasCalledUno()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Integer> getPlayableIndexes(ArrayList<ICard> currentHand, ICard currentTopCard) {
        ArrayList<Integer> playableIndexes = new ArrayList<>();
        for (int i = 0; i < currentHand.size(); i++) {
            if (currentHand.get(i).canBePlayed(currentTopCard)) {
                playableIndexes.add(i);
            }
        }
        return playableIndexes;
    }

    private void handlePickCard(ArrayList<ICard> currentHand, ICard currentTopCard) {
        currentPlayer.drawCard(gamePile.giveCard());

        ICard lastDrawnCard = currentHand.get(currentHand.size() - 1);

        if (lastDrawnCard.canBePlayed(currentTopCard)) {
            currentPlayer.playCard(lastDrawnCard, this);
        }
    }

    private void handlePlayCard(ICard topCard) {
        ICard cardToPlay = currentPlayer.selectCardToPlay(topCard);
        if (cardToPlay != null) {
            currentPlayer.playCard(cardToPlay, this);
        }
        checkUnoCall();
    }

    private void checkUnoCall() {
        if (currentPlayer.needsToCallUno() && !currentPlayer.hasCalledUno()) {
            currentPlayer.givePenaltyForNotCallingUno(this);
        }
    }

    @Override
    public IDeck getDiscardDeck() {
        return discardPile;
    }

    @Override
    public IDeck getGameDeck() {
        return gamePile;
    }

    @Override
    public void increasePlayerIndex() {
        this.playerIndex = this.playerIndex + order;
        if (playerIndex == players.size()) 
        {
            //Jump back to first player
            //Game is going from i to i + 1
            playerIndex = 0;
        }
        if (playerIndex < 0)
        {
            //Jump to last player in list
            //Game is going from i to i - 1
            playerIndex = players.size() - 1;
        }
    }

    @Override
    public ArrayList<IPlayer> getPlayers() {
        return players;
    }

    @Override
    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override 
    public void revertOrder() {
        order = order * -1;
    }
}
