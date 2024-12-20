import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements IPlayer, Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<ICard> hand;
    private int status; // 0 = playing, 1 = winner, 2 = called UNO, 3 = one card but didn't call UNO
    private boolean unoCalled;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.status = 0; 
        this.unoCalled = false;
    }

    @Override
    public void drawCard(ICard card) {
        hand.add(card);
        if (card == null) {
            System.out.println("Player " + name + " drew a card");
        }
        unoCalled = false; // Reset UNO call status if player draws a card
    }

    @Override
    public void playCard(ICard card, IGame game) {
        card.play(game);
        game.getDiscardDeck().addCard(card);
        hand.remove(card);
        
        // Update status after playing a card
        updateStatusPlay();
    }

    private void updateStatusPlay() {
        if (hand.isEmpty()) {
            status = 1; // Player won
        } else if (needsToCallUno()) {
            status = unoCalled ? 2 : 3; // Set status based on UNO call
        } else {
            status = 0; // Continue playing
        }
    }

    @Override
    public int checkStatus() {
        return status;
    }

    @Override
    public void setStatus(int newStatus) {
        this.status = newStatus;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<ICard> getHand(){
        return hand;
    }

    @Override
    public int getHandSize() {
        return hand.size();
    }

    @Override
    public String getAction(List<String> options, Server server) {        
        return server.getPlayerAction(this, options);
        //return "playCard";
    }

    @Override
    public boolean hasPlayableCard(ICard topCard) {
        for (ICard card : hand) {
            if (card.canBePlayed(topCard)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int selectCardToPlay(Server server) {
        return server.cardIndex;
    }

    @Override
    public boolean needsToCallUno() {
        return hand.size() == 1 && !unoCalled;
    }

    @Override
    public void callUno() {
        unoCalled = true;
    }

    @Override
    public boolean hasCalledUno() {
        return unoCalled;
    }

    @Override
    public void giveUnoPenalty(IGame game) {
        if (!unoCalled && hand.size() == 1) {
            drawCard(game.getGameDeck().giveCard());
            drawCard(game.getGameDeck().giveCard());
        }
    }

    @Override
    public void removeCard(ICard card) {
        hand.remove(card);
    }

}
