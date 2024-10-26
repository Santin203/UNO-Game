public class SkipCard extends CardDecorator {
    public SkipCard(ICard decoratedCard) {
        super(decoratedCard);
    }

    @Override
    public void play(IGame game) {
        // Add skip logic before or after playing the card
        System.out.println("Player skips their turn.");
        super.play(game);  // Call the base card's play method
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        // Additional logic for skip cards (if any)
        return super.canBePlayed(topCard);
    }
}
