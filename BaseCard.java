
import java.io.Serializable;


public abstract class BaseCard implements ICard, Serializable {
    private static final long serialVersionUID = 1L;
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
