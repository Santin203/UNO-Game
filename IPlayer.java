import java.util.ArrayList;
import java.util.Map;

public interface IPlayer {
    void drawCard(ICard card);
    boolean playCard(ICard card, IGame game);
    int checkStatus();
    void setStatus(int newStatus);
    String getName();
    ArrayList<ICard> getHand();
    String getAction(Map<String, Boolean> options);
}
