public class ChangeColorCard extends CardDecorator {
    private String newColor;

    public ChangeColorCard(ICard decoratedCard, String newColor) {
        super(decoratedCard);
        this.newColor = newColor;
    }

    @Override
    public void play() {
        // Add change color logic
        System.out.println("Changing the color to: " + newColor);
        super.play();  // Call the base card's play method
    }

    @Override
    public String getColor() {
        // Return the new color when the card is played
        return newColor;
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        // Wild cards or change color cards can always be played
        return true;
    }
}

