package it.unibo.shoot.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import it.unibo.shoot.util.Constants;

/**
 * Subclass of JPanel, it works as a game screen.
 */
public class GamePanel extends JPanel {

    /** Default size of tile, need to be scaled to be displayed bigger */
    public GamePanel() {
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
    }

    /** Paints component.
     * 
     * @param g the graphics context.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    
    }
}
