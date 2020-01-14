import java.util.List;
import java.util.ArrayList;

class Deck{

	private List<Integer> suits;
	private List<List<Card>> deck;

	public Deck(){
		//loop until all cards are added to deck
		suits = new ArrayList<Integer>();
		deck = new ArrayList<List<Card>>();
		//loop suits
		for(int i=0;i<4;i++){
			List<Card> tempList = new ArrayList<>();
			//loop numbers
			for(int j=0;j<6;j++){
				Card card = new Card(i,(j));
				tempList.add(card);
			}
			deck.add(tempList);
		}
	}

	//gets amount of suits left in deck so randoms will not go out of bounds
	public int getSuitsLeft(){
		return deck.size();
	}

	//gets amount of numbers left in deck for that suit so randoms will not go out of bounds
	public int getNumsLeft(int suit){
		return deck.get(suit).size();
	}

	//draws card from the deck, removing that number from list of that suit
	//if it was last number of that suit, remove suit also
	public Card drawCard(int suit, int num){
		Card drawn = deck.get(suit).remove(num);
		if(deck.get(suit).isEmpty())
			deck.remove(suit);
		return drawn;
	}

	//check if all cards have been dealt
	public boolean deckIsEmpty(){
		if(deck.isEmpty())
			return true;
		return false;
	}


	
}
