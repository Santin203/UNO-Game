import java.util.ArrayList;

public class DrawKCard extends CardDecorator {

    public DrawKCard(ICard card) {
        super(card);  // Call the parent CardDecorator with the base card
    }

    @Override
    public void play(IGame game) {
        // Special logic for playing Draw Two
        System.out.println("Draw K card played!");

        // Call the base card's play method (if needed)
        super.play(game);

        // Apply Draw Two behavior
        applyDrawKEffect(game);
    }

    private void applyDrawKEffect(IGame game) {
        // Logic to force the next player to draw 2 cards
        int k;
        if("draw2".equals(getValue()))
        {
            System.out.println("The next player must draw 2 cards.");
            k = 2;
        }
        else
        {
            System.out.println("The next player must draw 4 cards.");
            k = 4;
        }

        ArrayList<IPlayer> gamePlayers = game.getPlayers();
       
        //Set nextPlayer to upcoming player
        //Will skip next player when current player's turn ends
        game.increasePlayerIndex();
        IPlayer nextPlayer = gamePlayers.get(game.getPlayerIndex());

        //Draw K cards from game pile
        for (int i = 0; i < k; i++) {
            nextPlayer.drawCard(game.getDeck("game").giveCard());
        }
    }
}


