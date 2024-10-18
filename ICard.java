public interface ICard {
    String getValue();
    String getColor();
    void setValue(String value);
    void setColor(String color);
    void performAction();  // For special actions like Skip, Take2, etc.
}
