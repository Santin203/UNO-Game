import java.util.ArrayList;

public class DrawKDecorator extends BaseCard {
    private BaseCard baseCard;
    private int drawCount;

    public DrawKDecorator(BaseCard baseCard, int drawCount) {
        super();
        this.baseCard = baseCard;
        this.drawCount = drawCount;
    }

    @Override
    public void play(IGame game) {
        baseCard.play(game);
        System.out.println("Next player must draw " + drawCount + " cards!");

        applyDrawKEffect(game);
        
        // Implement game logic for drawing cards, e.g., informing the game instance
        //game.drawCardsForNextPlayer(drawCount);
    }

    public int getDrawCount() {
        return drawCount;
    }

    private void applyDrawKEffect(IGame game) {
        // Logic to force the next player to draw 2 cards

        ArrayList<IPlayer> gamePlayers = game.getPlayers();
       
        //Set nextPlayer to upcoming player
        //Will skip next player when current player's turn ends
        game.increasePlayerIndex();
        IPlayer nextPlayer = gamePlayers.get(game.getPlayerIndex());

        //Draw K cards from game pile
        for (int i = 0; i < drawCount; i++) {
            nextPlayer.drawCard(game.getGameDeck().giveCard());
        }
    }

    @Override
    public boolean canBePlayed(ICard topCard) {
        return baseCard.canBePlayed(topCard);
    }

    public BaseCard getWrappedCard() {
        return baseCard;
    }

    public String getColor() {
        return baseCard.getColor();
    }
}
