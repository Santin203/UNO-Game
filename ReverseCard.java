public class ReverseCard extends CardDecorator {

    public ReverseCard(ICard card) {
        super(card);
    }

    @Override
    public void play(IGame game) {
        System.out.println("Reverse action performed");
        super.play(game);

        applyReverseEffect(game);
    }

    private void applyReverseEffect(IGame game) {
        game.revertOrder();
    }
}
