public abstract class BaseCard implements ICard {

    public BaseCard(String color) {
        
    }

    public abstract void play(IGame game);

    @Override
    public boolean canBePlayed(ICard topCard) {
        return this.getColor().equals(topCard.getColor());
    }

    public abstract String getColor();

}
