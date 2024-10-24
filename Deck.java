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
        String[] specialTypeList = {"skip", "reverse", "take2"};

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
        deckUnfilled.addCard(new BasicCard(color, "0"));
        for (int j = 1; j < 10; j++) {
            deckUnfilled.addCard(new BasicCard(color, String.valueOf(j)));
            deckUnfilled.addCard(new BasicCard(color, String.valueOf(j)));
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
        switch (type) {
            case "skip":
                return new SkipCard(new BasicCard(color, "skip"));
            case "reverse":
                return new ReverseCard(new BasicCard(color, "reverse"));
            case "take2":
                return new Draw2Card(new BasicCard(color, "take2"));
            default:
                throw new IllegalArgumentException("Unknown special card type: " + type);
        }
    }

    private void createWildCards(IDeck deckUnfilled) {
        for (int k = 0; k < 4; k++) {
            // Wild and WildTake4 cards don't have colors
            deckUnfilled.addCard(new ChangeColorCard(new BasicCard("", "wild"), ""));
            deckUnfilled.addCard(new Draw4Card(new BasicCard("", "wildtake4")));
        }
    }

    public static IDeck buildDeck(int decksN) {
        IDeck newDeck = new Deck();
        newDeck.fillDeck(newDeck, decksN);
        newDeck.shuffleCards();
        return newDeck;
    }
}
