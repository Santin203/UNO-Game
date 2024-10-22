import java.util.ArrayList;

public interface IPlayer {
    void drawCard(ICard card);
    boolean playCard(ICard card);
    int checkStatus();
    String getName();
    ArrayList<ICard> getHand();
}
