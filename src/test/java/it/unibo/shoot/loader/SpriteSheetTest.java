package it.unibo.shoot.loader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.shoot.util.Constants;
import java.awt.image.BufferedImage;

class SpriteSheetTest {

    private SpriteSheet ss;
    private static final int COLS = 4;
    private static final int ROWS = 4;

    @BeforeEach
    void setUp() {
        // Crea un'immagine sintetica grande abbastanza
        BufferedImage img = new BufferedImage(
            COLS * Constants.TILE_SIZE,
            ROWS * Constants.TILE_SIZE,
            BufferedImage.TYPE_INT_ARGB
        );
        ss = new SpriteSheet(img);
    }

    @Test
    void testGrabImageValido() {
        BufferedImage tile = ss.grabImage(0, 0);
        assertNotNull(tile);
        assertEquals(Constants.TILE_SIZE, tile.getWidth());
        assertEquals(Constants.TILE_SIZE, tile.getHeight());
    }

    @Test
    void testGrabImageUltimoTileValido() {
        BufferedImage tile = ss.grabImage(COLS - 1, ROWS - 1);
        assertNotNull(tile);
    }

    @Test
    void testGrabImageFuoriDaiBordi() {
        assertThrows(IllegalArgumentException.class, () -> ss.grabImage(COLS, 0));
        assertThrows(IllegalArgumentException.class, () -> ss.grabImage(0, ROWS));
    }

    @Test
    void testGrabImageConDimensioniValido() {
        int w = 64, h = 64;
        BufferedImage img = new BufferedImage(w * 3, h * 3, BufferedImage.TYPE_INT_ARGB);
        SpriteSheet ss2 = new SpriteSheet(img);
        BufferedImage tile = ss2.grabImage(0, 0, w, h);
        assertNotNull(tile);
        assertEquals(w, tile.getWidth());
        assertEquals(h, tile.getHeight());
    }

    @Test
    void testGrabImageConDimensioniFuoriDaiBordi() {
        int w = 64, h = 64;
        BufferedImage img = new BufferedImage(w * 2, h * 2, BufferedImage.TYPE_INT_ARGB);
        SpriteSheet ss2 = new SpriteSheet(img);
        assertThrows(IllegalArgumentException.class, () -> ss2.grabImage(2, 0, w, h));
        assertThrows(IllegalArgumentException.class, () -> ss2.grabImage(0, 2, w, h));
    }
}