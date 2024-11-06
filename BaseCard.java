
import java.io.Serializable;


public abstract class BaseCard implements ICard, Serializable {
    private static final long serialVersionUID = 1L;
    public BaseCard() {
        
    }

    public abstract void play(IGame game);

}
