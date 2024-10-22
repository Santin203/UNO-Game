public interface ICard {
    int getValue();
    String getColor();
    void setValue(int value);
    void setColor(String color);
    void performAction();  // For special actions like Skip, Take2, etc.
    void setIsPlayeable(ICard topCard);
    boolean getIsPlayeable();
}
