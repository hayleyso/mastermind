# Mastermind Game
Mastermind is a code-breaking game where the objective is to guess a secret four color code within a limited number of attempts. After each guess, the player receives feedback on how many colors are correct and in the correct position. The game can be played in two modes: the player sets the code for the program to guess, or the program sets the code for the player to break. The goal is to guess the code in as few attempts as possible.

### Supported Features:
- **Gameplay with GUI**: Play the game through a user-friendly graphical interface.
- **Algorithm Solvers**: The program can guess the code set by the player.
  - **Donald Knuth's Algorithm** (Hard mode): A highly optimized algorithm that solves the code in ~5 guesses.
  - **Medium Algorithm** (Medium mode): A balanced guessing algorithm with reasonable performance.
  - **Basic Algorithm** (Easy mode): A straightforward guessing approach suitable for beginners.
- **Human Solver**: Players can guess the code based on provided hints.
- **File Handling**: Supports saving and loading game data from external files.
- **Code Guessing Levels**: Provides multiple levels for guessing the code.
    - **Easy**: 1-3 colors
    - **Medium**: 3-4 colors
    - **Hard**: 4 colors

### Additional Features:
- **User Response Validation**: Ensures that all hints provided by the system are valid and correctly aligned with the player's guess.
- **Return to a Previous Game**: Allows players to save and load unfinished games, picking up where they left off.
- **Popup Window**: Displays a prompt if there is an unfinished game, offering the player the option to either return to it or start a new game.
- **Home Button**: Quickly returns the player to the main menu or home screen for easy navigation.
- **New Game**: Enables players to start a new game with a different mode or level, providing variety in gameplay.

## Javadocs
For detailed API documentation, you can view the [Javadocs](https://hayleyso.github.io/mastermind/) of this project.

## Prerequisites
Before you begin, ensure you have the following installed:
- **Java Development Kit (JDK) 11** or later
- **Eclipse IDE**
- **JavaFX SDK**

## Setting up JavaFX in Eclipse
1. **Download the JavaFX SDK**
   - Visit [Gluon](https://gluonhq.com/products/javafx/) and download the JavaFX SDK.
2. **Extract the JavaFX SDK**
   - Extract the downloaded ZIP file to a location on your computer.
3. **Configure JavaFX in Eclipse**
   - In Eclipse, go to `Window > Preferences > Java > Build Path > User Libraries`.
   - Click **New**, name the library `JavaFX`.
   - Select the `JavaFX` library and click **Add External JARs**.
   - Navigate to the `lib` folder in your extracted JavaFX SDK and select all the JAR files.
   - Click **Apply and Close**.


## Importing the Project
1. **Clone this repository or download the source code**
2. **Import the Project into Eclipse**
   - In Eclipse, go to `File > Import > General > Existing Projects into Workspace`.
   - Select the root directory of the downloaded project and click **Finish**.


## Configuring the Project
**Set JavaFX Library in Eclipse**
   - Right-click on the project in Eclipse's Package Explorer.
   - Select `Properties > Java Build Path`.
   - In the `Libraries` tab, click **Add Library** and select **User Library**.
   - Check the `JavaFX` library you created earlier and click **Finish**.
   - In the `Order and Export` tab, ensure the `JavaFX` library is checked.
   - Click **Apply and Close**.


## Running the Application
1. **Run the Main Class**
   - Right-click on the main class file (`Mastermind.java`).
   - Select `Run As > Run Configurations`.
2. **Set VM Arguments**
   - In the `Arguments` tab, add the following to **VM arguments**:
     ```text
     --module-path /path/to/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml
     ```
     Replace `/path/to/javafx-sdk-17` with the actual path to your JavaFX SDK.
3. **Run the Application**
   - Click **Apply** and then **Run**.
