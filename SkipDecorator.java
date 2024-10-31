public class SkipDecorator extends BaseCard {
    private BaseCard baseCard;

    public SkipDecorator(BaseCard baseCard) {
        super();
        this.baseCard = baseCard;
    }

    @Override
    public void play(IGame game) {
        // Add skip logic before or after playing the card
        System.out.println("Player skips their turn.");
        baseCard.play(game);

        applySkipEffect(game);
    }

    private void applySkipEffect(IGame game) {
        game.increasePlayerIndex();
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        return baseCard.canBePlayed(topCard) || topCard instanceof SkipDecorator; // Same color or skip card
    }

    public BaseCard getWrappedCard() {
        return baseCard;
    }
}
