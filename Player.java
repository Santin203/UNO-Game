import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class Player implements IPlayer {
    private String name;
    private ArrayList<ICard> hand;
    private int status;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.status = 0;  // 0 = playing, 1 = winner, etc.
    }

    @Override
    public void drawCard(ICard card) {
        hand.add(card);
    }

    @Override
    public boolean playCard(ICard card) {
        // Logic to play a card
        card.play();
        hand.remove(card);
        return true;
    }

    @Override
    public int checkStatus() {
        return status;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<ICard> getHand(){
        return hand;
    }

    @Override
    public String getAction(Map<String, Boolean> options){
        ArrayList<String> availableOptions = options.entrySet().stream()
                .filter(entry -> entry.getValue()) // Keep only entries with true values
                .map(Map.Entry::getKey)            // Extract the keys
                .collect(Collectors.toCollection(ArrayList::new));
        
        //Insert code to get input from user through UI
        //Replace current line with logic
        String option = availableOptions.get(0); 
        return option;
    }
}
