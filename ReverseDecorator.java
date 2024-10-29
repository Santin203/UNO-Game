public class ReverseDecorator extends BaseCard {
    private BaseCard baseCard;

    public ReverseDecorator(BaseCard baseCard) {
        super();
        this.baseCard = baseCard;
    }

    @Override
    public void play(IGame game) {
        baseCard.play(game);
        System.out.println("Reverse effect activated!");
        // Additional logic for reverse effect
        applyReverseEffect(game);
    }

    private void applyReverseEffect(IGame game) {
        game.revertOrder();
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        return baseCard.canBePlayed(topCard) || topCard instanceof ReverseDecorator; // Same color or reverse card
    }

    public BaseCard getWrappedCard() {
        return baseCard;
    }
}
