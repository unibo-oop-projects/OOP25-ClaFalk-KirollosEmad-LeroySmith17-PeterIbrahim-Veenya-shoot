import it.unibo.shoot.model.Game;

// TODO: Aggiungere i final?

/**
 * Main class of the game.
 */
public class Main {

    // It should prevent instance creation.
    private Main() {

    }

    /**
     * The main method for the application.
     * 
     * @param args parameter for the application.
     */
    public static void main(String[] args) {
        // MenuView
        // MainMenuController
        // startMenu()


        //TODO solve this squiggly line thing
        Game game = new Game();
        game.start();
    }
}
