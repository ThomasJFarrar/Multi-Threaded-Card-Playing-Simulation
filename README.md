
# Multi-Threaded Card Playing Simulation
## Introduction
This multi-threaded card playing simulation is developed for the 2022 Continous Assessment for the ECM2414 Software Development module at the University of Exeter. See `ECM2414-CA.pdf` for the full specification. The program plays a simple card game which takes user input for the number of players that will be playing the game, and the pack that will be used to deal the cards from and used throughout the game. Text files are generated for all the players and are updated during the game with details on the beginning of the game, each turn, and the end of the game once there is a winner.
## Prerequisites
**openJDK 17.0.3** was used for the development of the multi-threaded card playing simulation. To run the `cards.jar` file, which will execute the game, you will need to have installed the JRE (Java Runtime Environment) on your machine. The JDK (Java Development Kit) is a superset of the JRE and will also work for running `cards.jar`.
## Getting Started
To get started with the Multi-Threaded Card Playing Simulation, run `cards.jar` using the command `java -cp cards.jar src.CardGame` in the terminal.
### Before the Game Begins
The terminal window will request you to input the number of players you would like to play the game, there must be at least 2 players. 
The terminal will also request the location of a valid input pack. For an input pack to be valid it must:
* Have 8n rows (n = number of players)
* Each row must only contain a single non-negative integer value
### Start of the Game
Each player gets numbered 1 to n, and n number of decks are created that also get numbered 1 to n. The decks and players form a ring topology (see illustration in `ECM2414-CA.pdf`) and the cards are distributed to the player's hands in a round-robin fashion from the top of the pack. The remaining cards are distributed to the decks, again in a round-robin fashion. A text file is created for every player, which is written throughout the game.
>**Note:** The pack does not get shuffled
### During the Game
Each turn, every player will simultaneously pick up a card from the deck to their left, and discard a card from their hand to the deck to their right. Each player will prefer certain card denominations, which reflect their player number. Players will never discard their preferred cards, and instead will choose a random non-preferred card from their hand to discard. After the turn, the player's hand and cards which were picked up and discarded that turn will be written to their individual text file.
### End of the Game
The game is won if any player is holding four cards of the same value in their hand. The winning player notifies the other players and a winning message is printed to the terminal window. Each player's final hand and confirmation of the winning player is written to their text file. A text file is created for every deck, and a single line is written to them detailing the contents of the deck at the end of the game.
## Testing
The program was tested using unit tests in **JUnit 4.13.2**.
To run the multi-threaded card playing simulation test suite use the following command in the terminal:
`java -cp lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore src.TestSuite`
## Known Issues
* The game does not handle a scenario where 2 or more players win simultaneously (Not required in spec)
* The game does not check if the pack file has at least 4 of the same values in it, meaning it can sometimes be impossible for a player to win if this is this is the case with the pack file (Not required in spec)
## Authors
* **Thomas Farrar**
* **Owen Gibson**
## License
This project is licensed under the MIT License - see `LICENSE` for more details.