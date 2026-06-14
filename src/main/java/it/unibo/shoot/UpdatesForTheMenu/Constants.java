package it.unibo.shoot.util;

/**
 * Keeps all the constants of the application,
 * it is used to store all the constants so they are accessible to other classes
 * from this singular point.
 */
public class Constants {
    private Constants() {}

    public static final String TITLE = "Sh00t";

    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 563;
    public static final int TILE_SIZE = 32;

    public static final int MAP_TILES = 64;  // your map is 64x64
    public static final int WORLD_WIDTH  = MAP_TILES * TILE_SIZE;  // 2048
    public static final int WORLD_HEIGHT = MAP_TILES * TILE_SIZE;  // 2048

    /**Layer values */
    public static final int BACKGROUND_LAYER = 0;
    public static final int TILES_LAYER = 1;
    public static final int ENEMY_LAYER = 2;
    public static final int PLAYER_LAYER = 3;
    public static final int EFFECTS_LAYER = 4;
    public static final int HUD_LAYER = 5;
    
}
