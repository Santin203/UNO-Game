

public abstract class BaseCard implements ICard {

    public BaseCard() {
        
    }

    public abstract void play(IGame game);

    // @Override
    // public boolean canBePlayed(ICard topCard) {
    //     String topCardColor = ((ColorDecorator) topCard).getColor();
    //     return ((ColorDecorator) this).getColor().equals(topCardColor);
    // }

    public String getColor() {
        throw new UnsupportedOperationException("This card does not have a color.");
    }
}
