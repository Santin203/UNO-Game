public abstract class Card implements ICard {
    protected int value;
    protected String color;
    protected boolean isPlayeable;

    public Card(int value, String color) {
        this.value = value;
        this.color = color;
    }
    
    public static ICard buildCard(String type, int value, String color){
        ICard newCard;

        newCard = switch (type) {
            case "number" -> new NumberCard(value, color);
            case "skip" -> new SkipCard(value, color);
            case "take2" -> new Take2Card(value, color);
            case "reverse" -> new ReverseCard(value, color);
            case "wild" -> new WildCard(-5);
            case "wildtake4" -> new WildTake4Card(value);
            default -> new NumberCard(-1, color);
        };
        return newCard;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public abstract void performAction();
}

