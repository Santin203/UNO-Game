public class DrawKDecorator extends BaseCard {
    private BaseCard baseCard;
    private int drawCount;

    public DrawKDecorator(BaseCard baseCard, int drawCount) {
        super(baseCard.getColor());
        this.baseCard = baseCard;
        this.drawCount = drawCount;
    }

    @Override
    public void play(IGame game) {
        baseCard.play(game);
        System.out.println("Next player must draw " + drawCount + " cards!");
        
        // Implement game logic for drawing cards, e.g., informing the game instance
        //game.drawCardsForNextPlayer(drawCount);
    }
}
