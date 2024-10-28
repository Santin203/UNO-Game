public abstract class BaseCard implements ICard {
    private String color;

    public BaseCard(String color) {
        this.color = color;
    }

    public abstract void play(IGame game);

    @Override
    public boolean canBePlayed(ICard topCard) {
        return this.getColor().equals(topCard.getColor());
    }

    public String getColor() {
        return color;
    }
}
