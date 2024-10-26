public interface ICard {
    void play(IGame game);  // Core behavior for playing the card
    String getColor();  // Return the color of the card
    String getValue();  // Return the value (or type) of the card, could be a number or action
    boolean canBePlayed(ICard topCard); // method to check if the card can be played based on the top card of the discard pile
}


