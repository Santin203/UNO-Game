public abstract class BaseCard implements ICard {

    public BaseCard() {
        
    }

    public abstract void play(IGame game);

    @Override
    public boolean canBePlayed(ICard topCard) {
        return ((ColorDecorator) this).getColor().equals(((ColorDecorator) topCard).getColor());
    }

}
