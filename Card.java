public abstract class Card implements ICard {
    protected int value;
    protected String color;

    public Card(int value, String color) {
        this.value = value;
        this.color = color;
    }
    
    public static ICard buildCard(String type, int value, String color){
        ICard newCard;

        switch(type)
        {
            case "number":
                newCard = new NumberCard(value, color);
                break;
            case "skip":
                newCard = new SkipCard(value, color);
                break;
            case "take2":
                newCard = new Take2Card(value, color);
                break;
            case "reverse":
                newCard = new ReverseCard(value, color);
                break;
            case "wild":
                newCard = new WildCard(-1);
                break;
            case "wildtake4":
                newCard = new WildTake4Card(value);
                break;
            default:
                newCard = new NumberCard(-1, color);
                break;
        }
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

