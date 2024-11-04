import java.util.ArrayList;

public interface IGame {
    void setGameServer(IServer serverInstance);
    void startGame();
    void nextTurn();
    IDeck getDiscardDeck();
    IDeck getGameDeck();
    void increasePlayerIndex();
    ArrayList<IPlayer> getPlayers();
    int getPlayerIndex();
    void revertOrder();
    void removePlayer(IPlayer player);
}
