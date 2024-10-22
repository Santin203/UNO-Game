public class NumberCard extends Card{
    
    public NumberCard(int value, String color) {
        super(value, color);  // Wildcards initially have no color
    }

    @Override
    public void performAction() {

    }

    @Override
    public void setIsPlayeable(ICard topCard){

    }
}
