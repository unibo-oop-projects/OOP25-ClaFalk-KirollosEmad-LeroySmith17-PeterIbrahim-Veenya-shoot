package it.unibo.shoot.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import it.unibo.shoot.model.PlayerModel;
import it.unibo.shoot.loader.SpriteSheet; 

/**
 * Gestisce la rappresentazione visiva (rendering) del giocatore.
 * Si occupa di caricare, suddividere e riprodurre i frame di animazione
 * estratti da un singolo foglio di sprite in base allo stato logico dettato dal Modello.
 * di animazione attualmente annidata nel PlayerModel.
 */
public class PlayerView {
    
    // Costanti per eliminare i "magic numbers" (Numeri magici hardcoded)
    private static final int SPRITE_GRID_ROWS = 4;
    private static final int SPRITE_GRID_COLS = 4;
    private static final int RENDER_SIZE = 32;

    private PlayerModel model;
    
    // Matrice [Riga (Direzione)][Colonna (Frame dell'animazione)]
    private BufferedImage[][] animations;
    
    /**
     * Costruisce la View del giocatore ed esegue il parsing dello SpriteSheet.
     * * @param model Il modello logico da cui leggere coordinate e stato di movimento.
     * @param ss Il foglio di sprite contenente l'asset grafico del giocatore.
     */
    public PlayerView(PlayerModel model, SpriteSheet ss) {
        this.model = model;
        animations = new BufferedImage[SPRITE_GRID_ROWS][SPRITE_GRID_COLS]; 

        // Scompone lo SpriteSheet caricando ogni singolo frame nella matrice.
        for (int r = 0; r < SPRITE_GRID_ROWS; r++) {
            for (int c = 0; c < SPRITE_GRID_COLS; c++) {
                animations[r][c] = ss.grabImage(c, r, RENDER_SIZE, 15); 
            }
        }
    }

    /**
     * Disegna il frame corrente dell'animazione sul contesto grafico.
     * Il frame viene scelto interrogando lo stato di movimento del modello.
     * * @param g2 Il contesto grafico (Graphics2D) su cui renderizzare l'entità.
     */
    public void render(Graphics2D g2) {
        BufferedImage currentSprite = animations[model.getRow()][model.getAniIndex()];
        
        int x = (int) model.getX();
        int y = (int) model.getY();
        
        // Renderizza l'immagine forzandola alle dimensioni stabilite (RENDER_SIZE)
        g2.drawImage(currentSprite, x, y, RENDER_SIZE, RENDER_SIZE, null); 
    }
}




