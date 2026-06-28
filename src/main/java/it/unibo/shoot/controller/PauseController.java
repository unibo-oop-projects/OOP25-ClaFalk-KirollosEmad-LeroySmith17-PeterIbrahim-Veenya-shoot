package it.unibo.shoot.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import it.unibo.shoot.model.Game;
import it.unibo.shoot.model.STATE;

/** Handler keyboard input for pausing and resuming the game. */
public class PauseController implements KeyListener {

    private final Game game;

    public PauseController(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (game.getGameState() == STATE.GAME) {
                game.setGameState(STATE.PAUSE);
            } else if (game.getGameState() == STATE.PAUSE) {
                game.setGameState(STATE.GAME);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}