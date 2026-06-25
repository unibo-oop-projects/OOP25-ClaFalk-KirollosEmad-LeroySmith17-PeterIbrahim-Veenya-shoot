package it.unibo.shoot.loader;

import java.awt.image.BufferedImage;
import it.unibo.shoot.util.Constants;

/**
 * Utility class to extract sprites from a spritesheet image.
 */
public class SpriteSheet {

    private final BufferedImage image;

    /**
     * Creates a SpriteSheet from the given image.
     *
     * @param image the spritesheet image.
     */
    public SpriteSheet(BufferedImage image) {
        this.image = image;
    }

    /**
     * Extracts a tile of fixed size ({@link Constants#TILE_SIZE}) from the spritesheet.
     *
     * @param col the column index of the tile.
     * @param row the row index of the tile.
     * @return the extracted {@link BufferedImage}.
     */
    public BufferedImage grabImage(int col, int row) {
        return grabImage(col, row, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    /**
     * Extracts a sprite of arbitrary size from the spritesheet.
     *
     * @param col the column index of the sprite.
     * @param row the row index of the sprite.
     * @param width the width of the sprite in pixels.
     * @param height the height of the sprite in pixels.
     * @return the extracted {@link BufferedImage}.
     * @throws IllegalArgumentException if the requested region is out of bounds.
     */
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