import java.util.ArrayList;
import java.util.List;

public interface IPlayer {
    void drawCard(ICard card);
    void playCard(ICard card, IGame game);
    int checkStatus();
    void setStatus(int newStatus);
    String getName();
    ArrayList<ICard> getHand();
    int getHandSize();
    String getAction(List<String> options, Server server);
    boolean hasPlayableCard(ICard topCard);
    int selectCardToPlay(Server server);
    boolean needsToCallUno();
    void callUno();
    boolean hasCalledUno();
    void giveUnoPenalty(IGame game);
    void removeCard(ICard card);
}
