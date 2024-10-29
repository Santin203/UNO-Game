import java.util.ArrayList;

public interface IGame {
    void startGame();
    void checkWinner();
    void nextTurn();
    IDeck getDiscardDeck();
    IDeck getGameDeck();
    void increasePlayerIndex();
    ArrayList<IPlayer> getPlayers();
    int getPlayerIndex();
    void revertOrder();
}
