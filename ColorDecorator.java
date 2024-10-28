public class ColorDecorator extends BaseCard {
    private BaseCard baseCard;
    private String color;

    public ColorDecorator(BaseCard baseCard, String color) {
        super(color);
        this.baseCard = baseCard;
        this.color = color;
    }

    @Override
    public void play(IGame game) {
        baseCard.play(game);
        System.out.println("Color changed to " + color);
        // Additional logic for changing color
    }

}
