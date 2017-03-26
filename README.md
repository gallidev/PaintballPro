![Paintball Pro](res/assets/paintballlogo.png)

Paintball Pro is a top-down cooperative shooter inspired by the real-world 
game Paintball. There are both single player (4v4 with AI) and multiplayer 
(4v4 with real players/AI) versions of the game, with two different game 
types - Team Match and Capture the Flag.

## Running a Client or Server

Simply run either `PaintballProClient.jar` or `PaintballProServer.jar` by double-clicking on it in the file explorer.

The game can also be run from an IDE, however this requires adding the JAR files contained in `lib/` to the build path.

## Running Tests

Currently this needs to be run in an IDE:
1. Create the new project
2. Add the library JAR files to the classpath, and build the project to ensure this has been done correctly
3. Run `Runner.java` (main method), found in `test/testRunner`