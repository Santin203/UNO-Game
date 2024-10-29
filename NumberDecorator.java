public class NumberDecorator extends BaseCard {
    private BaseCard baseCard;
    private String number;

    public NumberDecorator(BaseCard baseCard, String number) {
        super();
        this.baseCard = baseCard;
        this.number = number;
    }

    @Override
    public void play(IGame game) {
        baseCard.play(game);
        System.out.println("Player plays a " + number + " card!");
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        return baseCard.canBePlayed(topCard) || (topCard instanceof NumberDecorator && this.getNumber().equals(((NumberDecorator) topCard).getNumber()));
    }
    
}
