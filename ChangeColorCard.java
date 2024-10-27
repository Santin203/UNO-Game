public class ChangeColorCard extends CardDecorator {
    private String newColor;

    public ChangeColorCard(ICard decoratedCard, String newColor) {
        super(decoratedCard);
        this.newColor = newColor;
    }

    @Override
    public void play(IGame game) {
        // Add change color logic
        super.play(game);  // Call the base card's play method
        
        setNewColor();
        System.out.println("Changing the color to: " + newColor);
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

    private void setNewColor() {
        String[] colors = new String[]{"blue", "red", "green", "red"};
        //Add logic to select color from user interface
        //Replace with logic
        newColor = colors[0];
    }
}

