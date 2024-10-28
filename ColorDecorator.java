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

    @Override
    public String getColor() {
        return color;  // Color is only defined here
    }

    public void setColor(String color) {
        this.color = color;
    }

}
