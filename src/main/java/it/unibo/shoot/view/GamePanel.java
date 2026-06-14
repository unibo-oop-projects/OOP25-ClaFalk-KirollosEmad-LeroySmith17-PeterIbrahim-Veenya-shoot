package it.unibo.shoot.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import it.unibo.shoot.util.Constants;

/**
 * Subclass of JPanel, it works as a game screen.
 */
public class GamePanel extends JPanel {
    
    /** Default size of tile, need to be scaled to be displayed bigger */
    final int originalTileSize = 16; // 16x16 size
    final int scale = 3; 

    final int tileSize = originalTileSize * scale; // 48x48

    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize *  maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        // IF set to true, all the drawing from this component will be done in an offscreen paiting buffer
        this.setDoubleBuffered(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    
    }
    // -------- Getters --------

    public int getOriginalTileSize() {
        return originalTileSize;
    }

    public int getScale() {
        return scale;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
    }
    public int getScreenWidth() {
        return screenWidth;
    }
    public int getScreenHeight() {
        return screenHeight;
    }

    // -------- Setters --------

    // I dont think setters are necessary...
    
}
