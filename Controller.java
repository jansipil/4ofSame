import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

class Controller{
	
	//basic lock object
	public static Object lock = new Object();

	//map which is used to make other players wait
  	public static TreeMap<Long, Boolean> waitConditions = new TreeMap<Long, Boolean>();

  	//deck of cards
  	public static Deck deck = new Deck();

  	//current players
  	public static List<Player> players = new ArrayList<>();

  	//stacks between players
  	public static List<List<Card>> stacks = new ArrayList<>();

  	//list used for easier turn control
  	public static List<Long> playerOrder = new ArrayList<>();

  	private static int turn = 0;
  	private static boolean ready = true;
  	private static boolean win = false;

  	public static String winner;

  	public static void main(String[] args) {

  		System.out.println("Game starts");

  		//Add players and deal them cards
  		for(int i=0;i<4;i++){
  			Player randPlayer = new Player(i); 
  			for(int j=0;j<4;j++){
  				int randSuit = ThreadLocalRandom.current().nextInt(0, deck.getSuitsLeft());
  				int randNum = ThreadLocalRandom.current().nextInt(0, deck.getNumsLeft(randSuit));
  				Card drawn = deck.drawCard(randSuit,randNum);
  				randPlayer.addCard(new Card(drawn.getSuit(),drawn.getNumber()));
			}
			players.add(randPlayer);
  		}

  		//Add rest of the cards to stacks between players
  		for(int i=0;i<4;i++){
  			List<Card> tempStack = new ArrayList<>();
  			for(int j=0;j<2;j++){
  				int randSuit = ThreadLocalRandom.current().nextInt(0, deck.getSuitsLeft());
  				int randNum = ThreadLocalRandom.current().nextInt(0, deck.getNumsLeft(randSuit));
  				Card drawn = deck.drawCard(randSuit,randNum);
  				tempStack.add(new Card(drawn.getSuit(),drawn.getNumber()));
  			}
  			stacks.add(tempStack);
  		}

  		//make sure all cards in deck were dealt
  		if(!deck.deckIsEmpty()){
  			System.out.println("Deck was not empty");
  			return;
  		}

  		System.out.println("Deck is dealt");

  		//initialize player threads, they will go into waiting
  		for(Player p : players){
  			waitConditions.put(p.getThreadId(),true);
  			playerOrder.add(p.getThreadId());
  			p.initialize();
  		}

  		System.out.println("Players are ready");
  		
  		while(true){
  			
		    long id = playerOrder.get(turn);

		    //check if previous player is ready
		    if(getReady()){
			    synchronized (lock) { 
			    	//notify all threads, but allow only current player to proceed
			    	waitConditions.put(id, false);
			    	lock.notifyAll();
			    	setReady(false);
			    }
		    }
		    //if player has won the game, break the loop
		    if(getWin()){
		    	break;
		    }


			try {
    			Thread.sleep(3000);
  			}
  			catch(InterruptedException e) {}


  			waitConditions.put(id, true);
  			increaseTurn();
		}

	    System.out.println("\nWinner is: " + winner);

	    //try {
    	//	Thread.sleep(3000);
  		//}catch(InterruptedException e) {}

	    //shut down rest of the threads
	    synchronized (lock) {
		    for(Player p : players){
	  			waitConditions.put(p.getThreadId(),false);
	  		}
	  		lock.notifyAll();
	  	}
  		
	}


	public static synchronized int getTurn(){
		return turn;
	}

	public static void increaseTurn(){
		turn++;
		if(turn>3)
			turn=0;
	}

	public static boolean getReady(){
		return ready;
	}

	public static void setReady(boolean bool){
		ready = bool;
	}

	public static boolean getWin(){
		return win;
	}

	public static void setWin(boolean bool){
		win = bool;
	}


}