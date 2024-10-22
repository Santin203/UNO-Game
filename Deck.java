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

    public static IDeck buildDeck(int decksN){
        IDeck newDeck = new Deck();

        newDeck.fillDeck(newDeck, decksN);

        return newDeck;
    }


    @Override
    public ICard getTopCard(){
        return cards.get(cards.size() -1);
    }

    @Override
    public void fillDeck(IDeck deckUnfilled, int decksN)
    {
        String[] colorList = {"blue", "red", "yellow", "green"};

        String[] specialTypeList = {"skip", "reverse", "take2"};

        if(decksN > 0){
            for(int i = 0; i < decksN; i++)
            {
                //Iterate through values of colorList
                for (String color : colorList) {
                    
                    //Add cards from 0-9, add cards from 1-9 twice
                    cards.add(Card.buildCard("number", 0, color));
                    for(int j = 1; j < 10; j ++)
                    {
                        cards.add(Card.buildCard("number", j, color));
                        cards.add(Card.buildCard("number", j, color));
                    }
                    int specialValue = -1;
                    //Add two cards of each special type per color
                    //Skip's value = -1
                    //Reverse value = -2
                    //Take 2 value = -3
                    for (String type: specialTypeList)
                    {
                        cards.add(Card.buildCard(type, specialValue, color)); 
                        cards.add(Card.buildCard(type, specialValue, color));
                        specialValue -= 1;
                    }
                }
                //Add wild and take 4 cards
                for (int k = 0; k < 4; k++) {
                    cards.add(Card.buildCard("wild", -1, ""));
                    cards.add(Card.buildCard("wildtake4", -4, ""));
                }             
            }
            
        }
    }
}

