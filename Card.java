

class Card{

	private int suitNum;
	private String suit;
	private int number;

	public Card(int suitnum, int number){
		switch(suitnum){
			case 0: this.suit=" of Hearts";break;
			case 1: this.suit=" of Diamonds";break;
			case 2: this.suit=" of Spades";break;
			case 3: this.suit=" of Clubs";break;
		}
		this.number=number;
		this.suitNum = suitnum;
	}

	public int getNumber(){
		return number;		
	}
	public int getSuit(){
		return suitNum;
	}

	public void printCard(){
		System.out.println((number+1) + suit);
	}

}