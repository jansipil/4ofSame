import java.util.List;
import java.util.ArrayList;
import javafx.util.Pair;
import java.util.concurrent.ThreadLocalRandom;
import java.util.TreeMap;

class Player implements Runnable{
	
	private List<Card> hand;
	private boolean wait;
	private String playerNumber;
	private Thread thread;
	

	public Player(int playerNumber){
		hand = new ArrayList<Card>();
		this.playerNumber = "Player"+(playerNumber+1);
		thread = new Thread(this);
		
	}

	//player turn function
	private void timeToPlay() {

		//prints players current hand
		System.out.println("-----------");
		System.out.println(playerNumber + " hand:");
		for(Card card : hand)
			card.printCard();
		
   		//will be used to define which cards to keep in hand
		List<List<Integer>> holdingCards = new ArrayList<>();

		//there are propably better ways to define which cards to keep but oh well it works

		//will add amount of each same number in hand
		for(int i=0;i<4;i++){
			List<Integer> amount = new ArrayList<>();
			int num = hand.get(i).getNumber();
			for(int j=0;j<hand.size();j++){
				if(hand.get(j).getNumber()==num)
					amount.add(j);
			}
			holdingCards.add(amount);
		}

		//check which numbers were most
		int max = 0;
		List<Integer> maxCards = new ArrayList<>();
		for(List temp : holdingCards){
			if(temp.size()>max){
				max = temp.size();
				maxCards = new ArrayList<>(temp);
			}
		}

		//if holding 4 same numbers, win the game
		if(maxCards.size()==4){
			Controller.setWin(true);
			Controller.winner = playerNumber;
			return;
		}

		//add numbers player has most to map so he wont give them away
		TreeMap<Integer, Boolean> holdingThis = new TreeMap<Integer, Boolean>();
		holdingThis.put(0,false);
		holdingThis.put(1,false);
		holdingThis.put(2,false);
		holdingThis.put(3,false);
		for(Integer cards : maxCards){
			holdingThis.put(cards,true);
		}

		//give card based on map
		int givenCard = 0;
		if(!holdingThis.get(0))
			givenCard = 0;
		else if(!holdingThis.get(1))
			givenCard = 1;
		else if(!holdingThis.get(2))
			givenCard = 2;
		else
			givenCard = 3;

		Card tempCard = hand.remove(givenCard);
		System.out.print("\nGive: ");
		tempCard.printCard();

		int giveStack = Controller.getTurn();
		int takeStack = giveStack-1;
		if(takeStack<0)
			takeStack=3;

		//give card to next stack
		Controller.stacks.get(giveStack).add(tempCard);
		//take card from stack and add to hand
		tempCard = Controller.stacks.get(takeStack).get(0);
		Controller.stacks.get(takeStack).remove(0);
		hand.add(tempCard);
		System.out.print("Take: ");
		tempCard.printCard();
		System.out.println("-----------");

		//set ready so next player can go
   		Controller.setReady(true);
    }

    public void run() {

    	//loop thread until game is won
   		do {
	  		boolean wait=true;
			synchronized(Controller.lock) {
				//check if thread was allowed to play
				Boolean flag=Controller.waitConditions.get(Thread.currentThread().getId());
				if(null!=flag)
					wait=flag.booleanValue();
				//if not the wait for turn
				if(wait) {
					try {
						Controller.lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}
			//players turn if he is allowed to proceed and nobody has won yet
			if(!wait && !Controller.getWin()) {
				this.timeToPlay();
				try {
    				Thread.sleep(3000);
  				}catch(InterruptedException e) {}
			}
		} while(!Controller.getWin());
	}

	//starts thread but it will go into waiting
	public void initialize(){
		thread.start();
	}

	//can add cards from outside class
	public void addCard(Card card){
		hand.add(card);
	}
	
	//get id for player/thread
	public long getThreadId(){
		if (thread!=null)
			return thread.getId();
		return 0;
	}



}