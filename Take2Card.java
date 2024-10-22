public class Take2Card extends Card{

    public Take2Card(int value, String color) {
        super(value, color);  // Wildcards initially have no color
    }

    @Override
    public void performAction() {

    }

    @Override
    public void setIsPlayeable(ICard topCard){
        
    }
}
