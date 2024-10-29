public abstract class BaseCard implements ICard {

    public BaseCard() {
        
    }

    public abstract void play(IGame game);

    // @Override
    // public boolean canBePlayed(ICard topCard) {
    //     String topCardColor = ((ColorDecorator) topCard).getColor();
    //     return ((ColorDecorator) this).getColor().equals(topCardColor);
    // }
}
