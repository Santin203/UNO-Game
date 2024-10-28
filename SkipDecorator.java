public class SkipDecorator extends BaseCard {
    private BaseCard baseCard;

    public SkipDecorator(BaseCard baseCard) {
        super(baseCard.getColor());
    }

    @Override
    public void play(IGame game) {
        // Add skip logic before or after playing the card
        System.out.println("Player skips their turn.");
        //super.play(game);  // Call the base card's play method

        applySkipEffect(game);
    }

    private void applySkipEffect(IGame game) {
        game.increasePlayerIndex();
    }
}
