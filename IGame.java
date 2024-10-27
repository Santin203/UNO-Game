import java.util.ArrayList;

public interface IGame {
    void startGame();
    void checkWinner();
    void nextTurn();
    IDeck getDeck(String deck);
    void increasePlayerIndex();
    ArrayList<IPlayer> getPlayers();
    int getPlayerIndex();
    void revertOrder();
}
