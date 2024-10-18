public interface IDeck {
    void shuffleCards();
    ICard giveCard();
    void addCard(ICard card);
    void removeCard(ICard card);
}

