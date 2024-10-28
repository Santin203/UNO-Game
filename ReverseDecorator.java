public class ReverseDecorator extends BaseCard {
    private BaseCard baseCard;

    public ReverseDecorator(BaseCard baseCard) {
        super(baseCard.getColor());
        this.baseCard = baseCard;
    }

    @Override
    public void play(IGame game) {
        baseCard.play(game);
        System.out.println("Reverse effect activated!");
        // Additional logic for reverse effect
    }
}
