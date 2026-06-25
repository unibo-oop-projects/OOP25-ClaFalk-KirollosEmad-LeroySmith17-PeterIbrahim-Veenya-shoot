import it.unibo.shoot.model.Game;

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
        Game game = new Game();
        game.start();
    }
}
