public class ChangeColorDecorator extends BaseCard {
    private BaseCard baseCard;
    private String colorToChange;

    public ChangeColorDecorator(BaseCard baseCard) {
        super(baseCard.getColor());

    }

    public void changeColor(String newColor) {
        this.colorToChange = newColor;
        System.out.println("Color changed to: " + newColor);
    }

    @Override
    public String getColor() {
        return colorToChange != null ? colorToChange : baseCard.getColor();
    }

    @Override
    public void play(IGame game) {
        //IMPLEMENT LOGIC
        //baseCard.play(game);
        System.out.println("Color changed to " + colorToChange);
    }
}
