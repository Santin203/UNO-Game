public abstract class Card implements ICard {
    protected String value;
    protected String color;

    public Card(String value, String color) {
        this.value = value;
        this.color = color;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public abstract void performAction();
}

