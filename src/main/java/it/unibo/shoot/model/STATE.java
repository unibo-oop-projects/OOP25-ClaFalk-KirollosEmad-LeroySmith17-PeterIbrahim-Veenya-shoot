package it.unibo.shoot.model;

/**
 * Represents the possible states of the game.
 */
public enum STATE {
    /** The game is on the main menu, game logic is inactive. */
    MENU,
    /** The game is running, tick and render are active. */
    GAME,
    /** The player has died, all physics are frozen. */
    GAME_OVER,
    /** The player has levelled up, the world is frozen and the upgrade menu is shown. */
    LEVEL_UP
}