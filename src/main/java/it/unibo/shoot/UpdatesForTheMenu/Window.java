package it.unibo.shoot.view;

import it.unibo.shoot.model.Game;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Creates and configures the main window for the application using JFrame.
 * 
 * Sets a fixed size, adds the Game panel, centers the window, disables resizing, and makes it visible.
 * 
 * @param width the width of the window in pixels
 * @param height the height of the window in pixels
 * @param title the title of the window displayed in the top bar
 * @param game the game that is displayed in the windodw
*/

public class Window {
    public Window(int width, int height, String title, Game game) {

        JFrame frame = new JFrame();

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(title);

        frame.add(game);
        frame.pack(); // Causes this window to be sized to fit the preffered size and layouts of its subcomponents (in this case, Gamepalel)

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
