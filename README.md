README

4 player card game

Compile with:
javac -cp "." *.java

Run with:
java -cp "." Controller


The game:

There are 4 players with 4 cards in hand. Between each player is a stack of 2 cards.
On turn, players will put one card to next stack and take one from previous stack.
Goal is to get 4 same numbers in hand.
Players will play automaticly, with each on their own thread.
Controller class is used to define turns.

No input from user required, just compile and run.