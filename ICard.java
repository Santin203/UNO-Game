public interface ICard {
    void play(IGame game);  // Core behavior for playing the card
    boolean canBePlayed(ICard topCard); // method to check if the card can be played based on the top card of the discard pile
    String getColor(); // method to get the color of the card
}


