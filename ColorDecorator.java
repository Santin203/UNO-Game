public class ColorDecorator extends BaseCard {
    private BaseCard baseCard;
    private String color;

    public ColorDecorator(BaseCard baseCard, String color) {
        super();
        this.baseCard = baseCard;
        this.color = color;
    }

    @Override
    public void play(IGame game) {
        System.out.println("Player plays a " + color + " card!");
    }

    public String getColor() {
        return color;  // Color is only defined here
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        return topCard.getColor().equals(color);
    }

}
