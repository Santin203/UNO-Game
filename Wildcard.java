public class WildCard extends Card {

    public WildCard(int value) {
        super(value, "");  // Wildcards initially have no color
    }

    @Override
    public void performAction(){
        
    }

    @Override
    public void setIsPlayeable(ICard topCard){
        this.isPlayeable = true;
    }
}

