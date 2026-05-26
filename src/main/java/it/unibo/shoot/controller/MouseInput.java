package it.unibo.shoot.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import it.unibo.shoot.model.Bullet;
import it.unibo.shoot.model.Game;
import it.unibo.shoot.model.GameObject;
import it.unibo.shoot.model.Handler;
import it.unibo.shoot.model.ID;
import it.unibo.shoot.model.STATE;
import it.unibo.shoot.view.Camera;

public class MouseInput extends MouseAdapter {

    private Handler handler;
    private Camera camera;

    public MouseInput(Handler handler, Camera camera) {
        this.handler = handler;
        this.camera = camera;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (Game.gameState == STATE.MENU) {
            Game.gameState = STATE.GAME; // Cambia stato e avvia l'azione!
            return; // Ferma il codices q
        }
        // 1. Prendiamo le coordinate del click sullo schermo
        int mx = e.getX();
        int my = e.getY();

        // 2. Le trasformiamo in coordinate assolute del mondo di gioco
        int worldX = (int) (mx + camera.getX());
        int worldY = (int) (my + camera.getY());

        // 3. Cerchiamo il Player nell'Handler per sapere da dove sparare
        for (int i = 0; i < handler.getObjects().size(); i++) {
            GameObject tempObject = handler.getObjects().get(i);

            if (tempObject.getId() == ID.Player) {
                // Trovato! Calcoliamo il centro del player
                int startX = (int) tempObject.getX() + 16;
                int startY = (int) tempObject.getY() + 16;

                // Creiamo il proiettile che viaggia da startX,startY fino a worldX,worldY
                handler.addObject(new Bullet(startX, startY, ID.Bullet, handler, worldX, worldY, null));
                break; // Usciamo dal ciclo, abbiamo già sparato
            }
        }
    }
}