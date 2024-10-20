import java.util.ArrayList;
import java.util.Collections;

public class Deck implements IDeck {
    private ArrayList<ICard> cards;

    public Deck() {
        cards = new ArrayList<ICard>();
    }

    @Override
    public void shuffleCards() {
        Collections.shuffle(cards);
    }

    @Override
    public ICard giveCard() {
        return cards.remove(cards.size() - 1);
    }

    @Override
    public void addCard(ICard card) {
        cards.add(card);
    }

    @Override
    public void removeCard(ICard card) {
        cards.remove(card);
    }

    public static IDeck buildDeck(int deckPercentage)
    {
        IDeck newDeck = new Deck();

        newDeck.fillDeck(newDeck, deckPercentage);

        return newDeck;
    }

    @Override
    public void fillDeck(IDeck deckUnfilled, int deckPercentage)
    {
        int cardAmount = 108;
        
        double scale = deckPercentage/100;

        double finalCardAmount = cardAmount*scale;

        


    }
}

