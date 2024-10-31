import java.util.ArrayList;
import java.util.List;

public interface IPlayer {
    void drawCard(ICard card);
    void playCard(ICard card, IGame game);
    int checkStatus();
    void setStatus(int newStatus);
    String getName();
    ArrayList<ICard> getHand();
    String getAction(List<String> options);
    boolean hasPlayableCard(ICard topCard);
    ICard selectCardToPlay(ICard topCard);
    boolean needsToCallUno();
    void callUno();
    boolean hasCalledUno();
    void giveUnoPenalty(IGame game);
}
