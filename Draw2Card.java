public class Draw2Card extends CardDecorator {

    public Draw2Card(ICard card) {
        super(card);  // Call the parent CardDecorator with the base card
    }

    @Override
    public void play() {
        // Special logic for playing Draw Two
        System.out.println("Draw Two card played!");

        // Call the base card's play method (if needed)
        super.play();

        // Apply Draw Two behavior
        applyDraw2Effect();
    }

    @Override
    public String getColor() {
        return super.getColor();
    }

    @Override
    public String getValue() {
        // Value of the card is "draw2" or similar identifier
        return "draw2";
    }

    private void applyDraw2Effect() {
        // Logic to force the next player to draw 2 cards
        System.out.println("The next player must draw 2 cards.");
        // You can add more logic to handle the actual drawing of cards in your game
    }
}


