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

    public static IGame getInstance(ArrayList<IPlayer> players) {
        if (instance == null) {
            instance = new Game(players);
        }
        return instance;
    }

    @Override
    public void setGameServer(Server serverInstance) {
        this.gameServer = serverInstance;
    }

    @Override
    public void startGame() {
        setupGamePile();
        dealInitialCards();
        updateServer();

        //Game loop
        while(true)
        {
            checkFewCardsDeck();
            currentPlayer = players.get(playerIndex);
            nextTurn();
            updateServer();
            //If current player has no more cards
            if(currentPlayer.checkStatus() == 1)
            {
                //Current player Won, game ends
                System.out.println(currentPlayer.getName() + " has won the game!");
                gameServer.notifyObservers("Player: "+currentPlayer.getName()+ "has WON!");
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

    private void checkFewCardsDeck() {
        if (gamePile.getDeckSize() <= 6) {
            ICard currentCard = discardPile.giveCard();
            IDeck tempDeck = gamePile;
            gamePile = discardPile;
            discardPile = new Deck();
            while (tempDeck.getDeckSize() > 0) {
                discardPile.addCard(tempDeck.giveCard());
            }
            discardPile.addCard(currentCard);
            gamePile.shuffleCards();
            System.out.println("Deck is empty, reshuffling discard pile");
        }
    }

    @Override
    public void nextTurn() {
        ArrayList<ICard> currentHand = currentPlayer.getHand();
        ICard topCard = discardPile.getTopCard();

        //Get indexes of playable cards
        ArrayList<Integer> playableIndexes = getPlayableIndexes(currentHand, topCard);
        
        // Define player's options for current turn
        List<String> options = getAvailableOptions(playableIndexes);
        //Send current player its playable indexes
        gameServer.sendPlayableIndexes(playableIndexes, currentPlayer.getName());
        //Get action from player
        String action = currentPlayer.getAction(options, gameServer);
        //Execute action from player
        executeAction(action, currentHand, topCard, playableIndexes);
    }

    private List<String> getAvailableOptions(ArrayList<Integer> playableIndexes) {
        List<String> options = new ArrayList<>();
        if (currentPlayer.hasPlayableCard(discardPile.getTopCard())) {
            options.add("playCard");
        }
        options.add("pickCard");
        if ((currentPlayer.getHandSize() == 2)&&(!playableIndexes.isEmpty())) {
            options.add("callUno");
        }
        if((previousPlayer != null) && (previousPlayer != currentPlayer)) {
            if ((!previousPlayer.hasCalledUno())&&(previousPlayer.getHandSize() == 1)) {
                options.add("challengeUno");
            }
        }
        return options;
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

    private void executeAction(String action, ArrayList<ICard> currentHand, ICard currentTopCard, ArrayList<Integer> playableIndexes) {
        switch(action)
        {
            case "pickCard" -> {
                handlePickCard(currentHand, currentTopCard);
                System.out.println("Player " + currentPlayer.getName() + " picked a card");
            }
            case "playCard" -> {
                handlePlayCard();
            }
            case "callUno" -> {
                currentPlayer.setStatus(2);
                currentPlayer.callUno();
                System.out.println("Player " + currentPlayer.getName() + " called UNO!");
                playerIndex += (-1)*order;
                
            }
            case "challengeUno" -> {
                previousPlayer.giveUnoPenalty(this);
                System.out.println("Player " + currentPlayer.getName() + " called UNO!");
                playerIndex += (-1)*order;
            }
            default -> {
                System.out.println("Invalid action");
                return;
            }
        }     
    }

    private void handlePickCard(ArrayList<ICard> currentHand, ICard currentTopCard) {
        currentPlayer.drawCard(gamePile.giveCard());

        ICard lastDrawnCard = currentHand.get(currentHand.size() - 1);

        if (lastDrawnCard == null) {
            System.out.println("Player " + currentPlayer.getName() + " drew a card");
        }

        if (lastDrawnCard.canBePlayed(currentTopCard)) {
            currentPlayer.playCard(lastDrawnCard, this);
            if(currentPlayer.getHandSize() == 1) {
                currentPlayer.callUno();
            }
        }
    }

    private void handlePlayCard() {
        int cardIndexToPlay = currentPlayer.selectCardToPlay(gameServer);
        ICard cardToPlay = currentPlayer.getHand().get(cardIndexToPlay);
        if (cardToPlay != null) {
            currentPlayer.playCard(cardToPlay, this);
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

    @Override
    public Server getServer() {
        return gameServer;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void removePlayer(IPlayer player) {
        players.remove(player);
    }

    void updateServer() {
        gameServer.sendPlayers(players);
        gameServer.sendTopPlayCard(discardPile.getTopCard());
    }
}
