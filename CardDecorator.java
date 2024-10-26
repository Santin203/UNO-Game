public abstract class CardDecorator implements ICard {
    protected ICard decoratedCard;

    public CardDecorator(ICard card) {
        this.decoratedCard = card;
    }

    @Override
    public void play(IGame game) {
        decoratedCard.play(game);
    }

    @Override
    public String getColor() {
        return decoratedCard.getColor();
    }

    @Override
    public String getValue() {
        return decoratedCard.getValue();
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        // Delegate to the wrapped card's canBePlayed method
        return decoratedCard.canBePlayed(topCard);
    }
}

