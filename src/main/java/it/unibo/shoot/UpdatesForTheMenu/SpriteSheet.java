package it.unibo.shoot.loader; // Controlla che il package sia giusto

import java.awt.image.BufferedImage;
import it.unibo.shoot.util.Constants; // Serve per il metodo di Vera

public class SpriteSheet {

    private BufferedImage image;

    public SpriteSheet(BufferedImage image) {
        this.image = image;
    }

    // ==========================================
    // METODO 1: (Per Block e Floor) 
    // ==========================================
    public BufferedImage grabImage(int col, int row) {
        int x = col * Constants.TILE_SIZE;
        int y = row * Constants.TILE_SIZE;
        if (x + Constants.TILE_SIZE > image.getWidth() || y + Constants.TILE_SIZE > image.getHeight()) {
            throw new IllegalArgumentException(
                "Tile (" + col + ", " + row + ") is out of bounds for spritesheet of size "
                + image.getWidth() + "x" + image.getHeight()
            );
        }
        return image.getSubimage(x, y, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    // ==========================================
    // METODO 2: (Per il Player) 
    // ==========================================
    public BufferedImage grabImage(int col, int row, int width, int height) {
        int x = col * width;
        int y = row * height;
        
        if (x + width > image.getWidth() || y + height > image.getHeight()) {
            throw new IllegalArgumentException(
                "Tile fuori dai bordi! x:" + x + " y:" + y + " nell'immagine " + image.getWidth() + "x" + image.getHeight()
            );
        }
        return image.getSubimage(x, y, width, height);
    }
}