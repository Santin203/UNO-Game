public class ChangeColorDecorator extends BaseCard {
    private BaseCard baseCard;
    private String colorToChange;
    private String[] colors = new String[]{"red", "blue", "yellow","green"};
    

    public ChangeColorDecorator(BaseCard baseCard) {
        super();
        this.baseCard = baseCard; // Assign baseCard here
    }

    public void changeColor(String newColor) {
        this.colorToChange = newColor;

        // Cast baseCard to ColorDecorator to access setColor
        if (baseCard instanceof ColorDecorator) {
            ((ColorDecorator) baseCard).setColor(newColor);
        }
    }


    @Override
    public void play(IGame game) {
        //IMPLEMENT LOGIC
        Server gameServer = game.getServer();
        String color = gameServer.getPlayerColor(game.getCurrentPlayer(), colors);
        changeColor(color);
        baseCard.play(game);
        System.out.println("Color changed to " + colorToChange);
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        return true; // Can be played on any card
    }

    public String getColor() {
        return baseCard.getColor();
    }

}
