package it.unibo.shoot.view;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import it.unibo.shoot.model.PlayerModel;
import it.unibo.shoot.loader.SpriteSheet; 

public class PlayerView {
    private PlayerModel model;
    private BufferedImage[][] animations;
    
    public PlayerView(PlayerModel model, SpriteSheet ss) {
        this.model = model;
        animations = new BufferedImage[4][4]; 

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                // TAGLIO SU MISURA PER IMMAGINE 128x60
                // Larghezza frame: 32 (128/4)
                // Altezza frame: 15 (60/4)
                animations[r][c] = ss.grabImage(c, r, 32, 15); 
            }
        }
    }

    public void render(Graphics2D g2) {
        BufferedImage currentSprite = animations[model.getRow()][model.getAniIndex()];
        
        int x = (int)model.getX();
        int y = (int)model.getY();
        
        // Disegniamo lo sprite forzandolo a diventare 32x32 
        // così non sembra schiacciato e occupa tutto il blocco del gioco
        g2.drawImage(currentSprite, x, y, 32, 32, null); 
    }
}