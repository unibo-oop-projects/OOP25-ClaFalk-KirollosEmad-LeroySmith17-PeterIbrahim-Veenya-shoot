package it.unibo.shoot.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import it.unibo.shoot.model.PlayerModel;
import it.unibo.shoot.loader.SpriteSheet; 

/**
 * Gestisce la rappresentazione visiva (rendering) del giocatore.
 * Implementa il pattern architetturale "Passive View": non altera mai lo stato 
 * dell'entità, limitandosi a interrogare il PlayerModel per estrarre le coordinate 
 * vettoriali e gli indici di animazione necessari alla riproduzione grafica.
 */
public class PlayerView {
    
    // Costanti strutturali per l'eliminazione dei "magic numbers"
    private static final int SPRITE_GRID_ROWS = 4;
    private static final int SPRITE_GRID_COLS = 4;
    private static final int RENDER_SIZE = 32;
    private static final int RAW_FRAME_HEIGHT = 15;

    private PlayerModel model;
    
    /** Matrice di cache visiva: [Riga (Direzione)][Colonna (Frame dell'animazione)] */
    private BufferedImage[][] animations;
    
    /**
     * Costruisce la View del giocatore ed esegue il pre-caching dello SpriteSheet.
     * @param model Il modello logico da cui leggere coordinate e stato di movimento.
     * @param ss Il foglio di sprite contenente l'asset grafico grezzo.
     */
    public PlayerView(PlayerModel model, SpriteSheet ss) {
        this.model = model;
        animations = new BufferedImage[SPRITE_GRID_ROWS][SPRITE_GRID_COLS]; 

        // Scompone lo SpriteSheet isolando ogni singolo frame nella matrice in tempo O(R*C).
        for (int r = 0; r < SPRITE_GRID_ROWS; r++) {
            for (int c = 0; c < SPRITE_GRID_COLS; c++) {
                animations[r][c] = ss.grabImage(c, r, RENDER_SIZE, RAW_FRAME_HEIGHT); 
            }
        }
    }

    /**
     * Disegna il frame corrente dell'animazione sul contesto grafico.
     * VINCOLO VISIVO: Applica dinamicamente un Texture Scaling (Stretching) tramite le 
     * API di Graphics2D. L'immagine grezza (32x15) viene forzata a una proporzione quadrata 
     * (32x32) per garantire una sovrapposizione visiva 1:1 con la Bounding Box fisica (Hitbox)
     * calcolata dal motore delle collisioni.
     *
     * @param g2 Il contesto grafico (Graphics2D) su cui renderizzare l'entità.
     */
    public void render(Graphics2D g2) {
        BufferedImage currentSprite = animations[model.getRow()][model.getAniIndex()];
        
        int x = (int) model.getX();
        int y = (int) model.getY();
        
        // Renderizza l'immagine applicando lo stretching per coincidere con la hitbox
        g2.drawImage(currentSprite, x, y, RENDER_SIZE, RENDER_SIZE, null); 
    }
}
