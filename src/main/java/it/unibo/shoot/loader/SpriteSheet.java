package it.unibo.shoot.loader;

import java.awt.image.BufferedImage;
import it.unibo.shoot.util.Constants;


public class SpriteSheet {

    private final BufferedImage image;

   
    public SpriteSheet(BufferedImage image) {
        this.image = image;
    }

    
    public BufferedImage grabImage(int col, int row) {
        return grabImage(col, row, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

   
    public BufferedImage grabImage(int col, int row, int width, int height) {
        int x = col * width;
        int y = row * height;
        if (x + width > image.getWidth() || y + height > image.getHeight()) {
            throw new IllegalArgumentException(
                "Sprite (" + col + ", " + row + ") out of bounds for spritesheet of size "
                + image.getWidth() + "x" + image.getHeight()
            );
        }
        return image.getSubimage(x, y, width, height);
    }
}
