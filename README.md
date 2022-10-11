  ![](https://i2.wp.com/geek.pizza/wp-content/uploads/2020/04/Copertina-Maestri-del-Rinascimento.jpg)

# Masters of Renaissance Videogame
## Overview
The program is a digital implementation in `Java` of the <b>Masters of Renaissance</b> tabletop game by <b>Cranio Games</b>.

In Masters of Renaissance, you are an important citizen of Florence and your goal is to increase your fame and prestige. 

Take resources from the market and use them to buy new cards. 
Expand your power both in the city and in the surrounding territories! Every card gives you a production power that transforms the resources so you can store them in your strongbox. Try to use the leaders' abilities to your advantage and don't forget to show your devotion to the Pope!

Masters of Renaissance is a game with simple rules offering deep strategic choices of action selection and engine building.

### Authors
Politecnico di Milano - Prof. Margara section - <b>AM25</b> group
- <b>Andrea Alesani</b> (andrea.alesani@mail.polimi.it)
- <b>Tommaso Brumani</b> (tommaso.brumani@mail.polimi.it)
- <b>Luigi Cagliani</b> (luigi.cagliani@mail.polimi.it)

### License
The project was carried out as part of the 2020/2021 '<b>Software Engineering</b>' course at <b>Politecnico of Milano</b>, where it was awarded a score of 30/30 cum Laude.

All rights to the Masters of Renaissance game are owned by Cranio Games, which provided the graphical resources to be used for educational purposes only.

## Project Specifications
The project consisted of developing a digital version of the tabletop game Masters of Reinassance according to the rules described in the `specifications/rules.pdf` document.

The program is realized following a Model-View-Controller design pattern: several UML diagrams are provided to clarify the structure and dynamics of the code.

A game instance is made up of a single server and multiple clients, which connect to it.

As part of the grading of the project, a number of possible functionalities could be implemented (these are described in greater detail in the `specifications/requirements.pdf` document. The implemented functionalities are:

- Basic Rules: games of 2-4 players.
- Complete Rules: games of 1-4 players (single-player available).
- Socket: remote connection to the server.
- CLI: command line game interface. 
- GUI: graphical game interface realized with JavaFX.
- Multiple games: server can host multiple game instances.
- Persistence: games are saved automatically and can be resumed.
- Disconnection resiliance: players can disconnect and reconnect to the server.

## Running the Game
The game consists of a single jar file by the name <code>AM25-jar-with-dependencies.jar</code>. It can be found in <code>/target</code> directory after building the project with Maven.

This file holds both the Server, the CLI and the GUI applications, one of which can be selected when booting.

To run the jar file, use the command

<code>java -jar AM25-jar-with-dependencies.jar</code>  

from the command line in the jar's folder. 

Both the Windows and Linux consoles are supported.

### Server

The machine running the server must be reachable from the clients in order to play the game. To start the server, select the <code>0</code> option when booting.  

By default, when launching this command, the server starts to listen for incoming messages on port <code>1234</code> (this can be changed from the <code>HostAndPort.json</code> file found in the <code>/resources/json</code> folder), and will accept the connection of any number of clients.

The server will look for a <code>/savedGames</code> directory on the same directory where the jar file is located, and will create one if none is found. 
This directory is used to save the server's games into json files, from which they will be automatically restored once the server is run again.

Once launched, the server will print the events' log on the standard output.

### Client

The CLI and GUI versions of the client can be run by selecting the <code>1</code> and <code>2</code> options when booting respectively.

In both cases, losing connection with the server will cause the termination of the client application.

#### Login

After selecting the host ip and port number to connect to, the player will be asked to choose a username. 
If no games are currently waiting for more players to join them a new game will be created, and the user will be asked to choose its number of players.

A user cannot select the same username as that of one of the players of a running game, unless that player has disconnected.
Selecting the username of a disconnected player will reconnect them and provide access to their game.

While a player is choosing the number of players for the next game no more players can join the server.
Once the game's size has been decided (and if it is greater than one) any joining players will automatically be added to that game. 
The creator and any joining players will be put into a waiting room until the game has reached its full capacity, at which point it will start automatically.

Disconnecting during the login phase will cause the player to be removed from the game.

#### First game turn

The game will determine a turn order randomly. During each player's first turn, they will have to select two of four leadercards to keep and, if necessary, which bonus resources to obtain and where to place them.
After confirming this choice, they will be able to take their turn normally.

A player disconnecting during this phase will have to complete the selection upon joining again, and will then be able to take their turn normally.

#### Further turns

On their normal turns, the players will be able to act according to the game rules. 

A player's turn is broadly split into two sections:
a first phase where the player will have to select one of the game's three actions, and, if the first is carried through successfully, a second phase where they will have to handle the results of said action.

Disconnecting during the first phase will cause the action to simply be aborted, and the player will be able to resume upon reconnecting.

Disconnecting during the second phase will instead cause the server to terminate the player's turn automatically, by executing different operations depending on the action taken:
if the player had selected a market row or column any resources not placed in any depot will be automatically discarded, whereas if the player had purchased a development card or activated one or more productions, the payment for this operation will be automatically detracted by the server from the player's storage.

During both of these phases the player will be able to interact with their leader cards, and move around the contents of their depots.

#### After the game ends

Once the game ends, all players will be informed of the winner's identity and their score.
They will be then prompted to close the client application, and may run it again to start a new game.

A game that has ended is removed from the server's saved games, and will thus not be restored when the server is run again.

#### Game interruptions

If a game's player disconnects from the server their game will continue whilst skipping their turn. 
A disconnected player can reconnect to their game by logging in using the same username, and will be able to take their turn normally once it arrives.

If all of a game's players disconnect, the first one to reconnect will become the current player.

## File System Structure
* `deliverables`: various explanatory documents, such as a description of the connection protocol and various UMLs
* `javadoc`: documentation for the classes and methods generated from the comments in the code
* `specifications`: specifications for the project and ruleset for the game
* `src`: code of the project and tests
* `target`: `jar` file for running the game, as well as the classes and files generated by Java