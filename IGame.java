import java.util.ArrayList;

public interface IGame {
    void setGameServer(Server serverInstance);
    void startGame();
    void nextTurn();
    IDeck getDiscardDeck();
    IDeck getGameDeck();
    void increasePlayerIndex();
    ArrayList<IPlayer> getPlayers();
    int getPlayerIndex();
    void revertOrder();
}
