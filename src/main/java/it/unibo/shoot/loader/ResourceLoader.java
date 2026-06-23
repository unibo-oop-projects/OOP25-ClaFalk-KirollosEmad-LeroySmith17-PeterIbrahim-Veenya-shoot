package it.unibo.shoot.loader;

import java.awt.image.BufferedImage;

/**
 * Loads and stores all game resources (images and spritesheets).
 */
public class ResourceLoader {

    private final SpriteSheet tileSS;
    private final SpriteSheet playerSS;
    private final SpriteSheet enemySS;
    private final BufferedImage crateImage;
    private final BufferedImage levelImage;

    /**
     * Loads all game resources from the classpath.
     */
    public ResourceLoader() {
        BufferedImageLoader loader = new BufferedImageLoader();
        levelImage  = loader.loadImage("/maps/map1.png");
        tileSS      = new SpriteSheet(loader.loadImage("/tiles/tileset.png"));
        playerSS    = new SpriteSheet(loader.loadImage("/sprites/player.png"));
        enemySS     = new SpriteSheet(loader.loadImage("/sprites/enemies.png"));
        crateImage  = loader.loadImage("/object/crate.png");
    }

    public SpriteSheet getTileSS()       { return tileSS; }
    public SpriteSheet getPlayerSS()     { return playerSS; }
    public SpriteSheet getEnemySS()      { return enemySS; }
    public BufferedImage getCrateImage() { return crateImage; }
    public BufferedImage getLevelImage() { return levelImage; }
}