public class ChangeColorDecorator extends BaseCard {
    private BaseCard baseCard;
    private String colorToChange;

    public ChangeColorDecorator(BaseCard baseCard) {
        super(baseCard.getColor());
        this.baseCard = baseCard; // Assign baseCard here
    }

    public void changeColor(String newColor) {
        this.colorToChange = newColor;
        
        // Cast baseCard to ColorDecorator to access setColor
        if (baseCard instanceof ColorDecorator) {
            ((ColorDecorator) baseCard).setColor(newColor);
        }
    }

    @Override
    public String getColor() {
        return colorToChange != null ? colorToChange : baseCard.getColor();
    }

    @Override
    public void play(IGame game) {
        //IMPLEMENT LOGIC
        baseCard.play(game);
        System.out.println("Color changed to " + colorToChange);
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        return true; // Can be played on any card
    }

}
