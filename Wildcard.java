public abstract class WildCard extends Card {

    public WildCard(String value) {
        super(value, "");  // Wildcards initially have no color
    }

    @Override
    public abstract void performAction();
}

