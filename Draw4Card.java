public class Draw4Card extends CardDecorator {

    public Draw4Card(ICard card) {
        super(card);  // Call the parent CardDecorator with the base card
    }

    @Override
    public void play(IGame game) {
        // Special logic for playing Wild Draw Four
        System.out.println("Wild Draw Four card played!");
        
        // Call the base card's play method (if needed)
        super.play(game);
        
        // Apply Wild Draw Four behavior
        applyWildTake4Effect();
    }

    @Override
    public String getColor() {
        // Wild cards do not have a color until a player chooses one, so the color could be set dynamically.
        return "wild";
    }

    @Override
    public String getValue() {
        // Value of the card is "wildtake4" or similar identifier
        return "wildtake4";
    }

    private void applyWildTake4Effect() {
        // Logic to force the next player to draw 4 cards
        System.out.println("The next player must draw 4 cards and you can choose a color.");
        // You can add more logic to handle the actual drawing of cards and color selection
    }
}

