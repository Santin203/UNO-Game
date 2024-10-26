public interface IGame {
    void startGame();
    void checkWinner();
    void nextTurn();
    IDeck getDeck(String deck);
}
