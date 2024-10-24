public class BasicCard implements ICard {
    private String color;
    private String value;

    public BasicCard(String color, String value) {
        this.color = color;
        this.value = value;
    }

    @Override
    public void play() {
        // Basic play logic, such as updating game state
        // For example, this could be setting the current color and value of the game
        System.out.println("Played " + this.toString());
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        // The card can be played if the color or value matches the top card
        return this.color.equals(topCard.getColor()) || this.value.equals(topCard.getValue());
    }

    @Override
    public String toString() {
        return "Card: " + color + " " + value;
    }
}
