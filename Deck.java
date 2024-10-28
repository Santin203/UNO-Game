import java.util.ArrayList;
import java.util.Collections;

public class Deck implements IDeck {
    private ArrayList<ICard> cards;

    public Deck() {
        cards = new ArrayList<>();
    }

    @Override
    public void shuffleCards() {
        Collections.shuffle(cards);
    }

    @Override
    public ICard giveCard() {
        return cards.isEmpty() ? null : cards.remove(cards.size() - 1);
    }

    @Override
    public void addCard(ICard card) {
        cards.add(card);
    }

    @Override
    public void removeCard(ICard card) {
        cards.remove(card);
    }

    @Override
    public ICard getTopCard() {
        return cards.isEmpty() ? null : cards.get(cards.size() - 1);
    }

    @Override
    public void fillDeck(IDeck deckUnfilled, int decksN) {
        String[] colorList = {"blue", "red", "yellow", "green"};
        String[] specialTypeList = {"skip", "reverse", "draw2"};

        if (decksN > 0) {
            for (int i = 0; i < decksN; i++) {
                // Create number cards and special cards for each color
                for (String color : colorList) {
                    createNumberCards(color, deckUnfilled);
                    createSpecialCards(color, specialTypeList, deckUnfilled);
                }
                // Add wild cards and wild take 4 cards (no color)
                createWildCards(deckUnfilled);
            }
        }
    }

    private void createNumberCards(String color, IDeck deckUnfilled) {
        // Add number cards 0-9, 1-9 twice
        deckUnfilled.addCard(new ColorDecorator(null, color));  // Card 0
        for (int j = 1; j < 10; j++) {
            deckUnfilled.addCard(new ColorDecorator(null, color));
            deckUnfilled.addCard(new ColorDecorator(null, color));
        }
    }

    private void createSpecialCards(String color, String[] specialTypeList, IDeck deckUnfilled) {
        for (String type : specialTypeList) {
            // Add two of each special card per color
            ICard specialCard = createSpecialCard(type, color);
            deckUnfilled.addCard(specialCard);
            deckUnfilled.addCard(specialCard);
        }
    }

    private ICard createSpecialCard(String type, String color) {
        BaseCard baseCard = new ColorDecorator(null, color);  // Creates a basic colored card

        switch (type) {
            case "skip":
                return new SkipDecorator(baseCard);
            case "reverse":
                return new ReverseDecorator(baseCard);
            case "draw2":
                return new DrawKDecorator(baseCard, 2);  // Assuming DrawKDecorator with 2 cards
            default:
                throw new IllegalArgumentException("Unknown special card type: " + type);
        }
    }

    private void createWildCards(IDeck deckUnfilled) {
        for (int k = 0; k < 4; k++) {
            // Add ChangeColor and Draw 4 cards with no initial color
            deckUnfilled.addCard(new ChangeColorDecorator(new ColorDecorator(null, "Black")));
            deckUnfilled.addCard(new DrawKDecorator(new ChangeColorDecorator(new ColorDecorator(null, "Black")), 4));

            
        }
    }

    public static IDeck buildDeck(int decksN) {
        IDeck newDeck = new Deck();
        newDeck.fillDeck(newDeck, decksN);
        newDeck.shuffleCards();
        return newDeck;
    }
}
