public class ReverseCard extends CardDecorator {

    public ReverseCard(ICard card) {
        super(card);
    }

    @Override
    public void play(IGame game) {
        // Add reverse card specific action
        System.out.println("Reverse action performed");
        // Optionally call the decorated card's action
        super.play(game);
    }
}
