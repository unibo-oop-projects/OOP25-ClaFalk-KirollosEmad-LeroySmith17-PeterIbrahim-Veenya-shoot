package it.unibo.shoot.model;

import it.unibo.shoot.GameObjects.GameObject;

/**
 * Identifies the {@link GameObject} type.
 */
public enum ID {
    /** A generic Box (used for testing). */
    Box(),
    /** The player character. */
    Player(),
    /** A solid wall block, used for level boundaries. */
    Block(),
    /** A projectile fired by the player or enemies. */
    Bullet(),
    /** An enemy entity. */
    Enemy(),
    /** A weapon object */
    Weapon(),
    /** A floor tile. */
    Floor(),
    /** Experience object */
    Experience(),
    /** A pickable crate object. */
    Crate(),
    /** A boss enemy. */
    Boss();
}
