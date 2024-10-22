import java.util.ArrayList;

public class Player implements IPlayer {
    private String name;
    private ArrayList<ICard> hand;
    private int status;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.status = 0;  // 0 = playing, 1 = winner, etc.
    }

    @Override
    public void drawCard(ICard card) {
        hand.add(card);
    }

    @Override
    public boolean playCard(ICard card) {
        // Logic to play a card
        return true;
    }

    @Override
    public int checkStatus() {
        return status;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<ICard> getHand(){
        return hand;
    }
}
