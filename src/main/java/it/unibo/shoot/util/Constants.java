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

    public static final int MAP_TILES = 64;  // map is 64x64
    public static final int WORLD_WIDTH  = MAP_TILES * TILE_SIZE;  // 2048
    public static final int WORLD_HEIGHT = MAP_TILES * TILE_SIZE;  // 2048

    /** Layer values */
    public static final int BACKGROUND_LAYER = 0;
    public static final int TILES_LAYER = 1;
    public static final int ENEMY_LAYER = 2;
    public static final int PLAYER_LAYER = 3;
    public static final int EFFECTS_LAYER = 4;
    public static final int HUD_LAYER = 5;

    /** Font values */
    public static final int TITLE_FONT_SIZE = 64;
    public static final int GAME_OVER_FONT_SIZE = 48;
    public static final int CARD_TITLE_FONT_SIZE = 15;
    public static final int CARD_DESC_FONT_SIZE = 15;
    public static final int OVERLAY_FONT_SIZE = 28;
    public static final int MENU_FONT_SIZE = 20;
    public static final int HUD_FONT_SIZE = 12;

    /** Color values */
    public static final int[] COL_BG = {255, 240, 245};
    public static final int[] COL_HP_BG = {255, 182, 193};
    public static final int[] COL_HP_FG = {255, 105, 135};
    public static final int[] COL_EXP_BG = {200, 180, 255};
    public static final int[] COL_EXP_FG = {148,  87, 235};
    public static final int[] COL_AMMO_BG = { 80,  60,  80, 180};
    public static final int[] COL_AMMO_LOW = {255, 100, 130};
    public static final int[] COL_AMMO_OK = {255, 213, 100};
    public static final int[] COL_CARD_BG = { 50,  40,  70};
    public static final int[] COL_CARD_BD = {255, 182, 219};
    public static final int[] COL_CARD_NAME = {255, 213, 240};
    public static final int[] COL_OVERLAY = { 30,  10,  40, 170};
    public static final int[] COL_GO_BG = { 20,  10,  30, 200};
    public static final int[] COL_GO_CARD = { 40,  20,  50};
    public static final int[] COL_GO_BORDER = {255, 150, 180};
    public static final int[] COL_BTN_BG = { 70,  30,  60};
    public static final int[] COL_TITLE = {255, 150, 200};
    public static final int[] COL_SUBTITLE = {220, 190, 255};
    public static final int[] COL_MENU_BG = { 25,  10,  35};
    public static final int[] COL_DEAD = {255, 120, 160};
}
