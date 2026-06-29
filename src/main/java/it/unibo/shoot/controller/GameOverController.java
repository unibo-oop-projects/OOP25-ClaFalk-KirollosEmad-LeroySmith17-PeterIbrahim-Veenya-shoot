package it.unibo.shoot.controller;

import java.awt.event.KeyEvent;

import it.unibo.shoot.model.Game;
import it.unibo.shoot.model.STATE;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import it.unibo.shoot.model.Game;
import it.unibo.shoot.model.STATE;

public class GameOverController implements KeyListener {
    private final Game game;
    
    public GameOverController(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
          if (game.getGameState() == STATE.GAME_OVER) {
            game.restartGame(); 
          }
          
        }

        if (e.getKeyCode() == KeyEvent.VK_X) {
            // Only exit if the game is over or in the main menu
            if (game.getGameState() == STATE.GAME_OVER || game.getGameState() == STATE.MENU) {
                System.exit(0);
            }   
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
