package it.unibo.shoot.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import it.unibo.shoot.model.*;

public class PlayerController implements KeyListener {

    private PlayerModel model;
    private Game game; 
    
    
    private Set<Integer> pressedKeys = new HashSet<>();

    public PlayerController(PlayerModel model, Game game) {
        this.model = model;
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); 
        pressedKeys.add(key);

       
    
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    
    public void update() {
        if (game.getGameState() == STATE.GAME_OVER) {
            return;
        }
    
        float dx = 0;
        float dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_W)) dy--;
        if (pressedKeys.contains(KeyEvent.VK_S)) dy++;
        if (pressedKeys.contains(KeyEvent.VK_A)) dx--;
        if (pressedKeys.contains(KeyEvent.VK_D)) dx++;
        
        model.setVelocity(dx, dy);
    }
}
