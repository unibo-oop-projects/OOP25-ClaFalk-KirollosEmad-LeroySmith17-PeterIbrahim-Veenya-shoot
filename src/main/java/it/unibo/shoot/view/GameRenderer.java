package it.unibo.shoot.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.List;

import it.unibo.shoot.model.Handler;
import it.unibo.shoot.model.LevelManager;
import it.unibo.shoot.model.Player;
import it.unibo.shoot.model.STATE;
import it.unibo.shoot.Upgrades.Upgrade;
import it.unibo.shoot.util.Constants;

/**
 * Handles all rendering logic for the game.
 * Draws the world, HUD, and all state-specific overlays (menu, game over, level up).
 */
public class GameRenderer {

    private final Handler handler;
    private final Camera camera;
    private final LevelManager levelManager;
    private final Canvas canvas;
    private final int width = Constants.SCREEN_WIDTH;
    private final int height = Constants.SCREEN_HEIGHT;

    private static final String FONT = "Comic Sans MS";

    /**
     * Creates a font with the given style and size.
     * 
     * @param style of the font
     * @param size of the font
     * @return the font
     */
    private static Font fontMaker(int style, int size) {
        return new Font(FONT, style, size);
    }

    /**
     * Creates a color from an int array of RGB or GBA values.
     * 
     * @param c array of 3 integer values in the range 0-255.
     * @return a color instance
     */
    private static Color col(int[] c) {
        return c.length == 4 ? new Color(c[0], c[1], c[2], c[3]) : new Color(c[0], c[1], c[2]);
    }

    // Font
    private final Font fontTitle = fontMaker(Font.BOLD, Constants.TITLE_FONT_SIZE);
    private final Font fontGameOver = fontMaker(Font.BOLD, Constants.GAME_OVER_FONT_SIZE);
    private final Font fontOverlayBig = fontMaker(Font.BOLD, Constants.OVERLAY_FONT_SIZE);
    private final Font fontMenu = fontMaker(Font.PLAIN, Constants.MENU_FONT_SIZE);
    private final Font fontCard = fontMaker(Font.BOLD, Constants.CARD_TITLE_FONT_SIZE);
    private final Font fontCardSub = fontMaker(Font.PLAIN, Constants.CARD_DESC_FONT_SIZE);
    private final Font fontCardDesc = fontMaker(Font.PLAIN, Constants.CARD_DESC_FONT_SIZE);
    private final Font fontHUD = fontMaker(Font.BOLD, Constants.HUD_FONT_SIZE);
    private final Font fontSmall = fontMaker(Font.PLAIN, Constants.MENU_FONT_SIZE);

    // Colors
    private static final Color COL_BG = col(Constants.COL_BG);
    private static final Color COL_HP_BG = col(Constants.COL_HP_BG);
    private static final Color COL_HP_FG = col(Constants.COL_HP_FG);
    private static final Color COL_EXP_BG = col(Constants.COL_EXP_BG);
    private static final Color COL_EXP_FG = col(Constants.COL_EXP_FG);
    private static final Color COL_AMMO_BG = col(Constants.COL_AMMO_BG);
    private static final Color COL_AMMO_LOW = col(Constants.COL_AMMO_LOW);
    private static final Color COL_AMMO_OK = col(Constants.COL_AMMO_OK);
    private static final Color COL_CARD_BG = col(Constants.COL_CARD_BG);
    private static final Color COL_CARD_BD = col(Constants.COL_CARD_BD);
    private static final Color COL_CARD_NAME = col(Constants.COL_CARD_NAME);
    private static final Color COL_OVERLAY = col(Constants.COL_OVERLAY);
    private static final Color COL_GO_BG = col(Constants.COL_GO_BG);
    private static final Color COL_GO_CARD = col(Constants.COL_GO_CARD);
    private static final Color COL_GO_BORDER = col(Constants.COL_GO_BORDER);
    private static final Color COL_BTN_BG = col(Constants.COL_BTN_BG);
    private static final Color COL_TITLE = col(Constants.COL_TITLE);
    private static final Color COL_SUBTITLE = col(Constants.COL_SUBTITLE);
    private static final Color COL_MENU_BG = col(Constants.COL_MENU_BG);
    private static final Color COL_DEAD = col(Constants.COL_DEAD);

    /**
     * Creates a GameRenderer.
     *
     * @param handler the handler containing all active game objects.
     * @param camera the camera used to translate the world view.
     * @param levelManager the level manager providing XP data for the HUD.
     * @param canvas the canvas to draw onto.
     */
    public GameRenderer(Handler handler, Camera camera, LevelManager levelManager, Canvas canvas) {
        this.handler = handler;
        this.camera = camera;
        this.levelManager = levelManager;
        this.canvas = canvas;
    }

    /**
     * Renders the current frame based on the active game state.
     *
     * @param gameState the current game state.
     * @param ammo the current ammo count to display in the HUD.
     * @param currentUpgradeOptions the list of upgrade options to display in LEVEL_UP state.
     */
    public void render(STATE gameState, int ammo, List<Upgrade> currentUpgradeOptions) {
        BufferStrategy bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(COL_BG);
        g.fillRect(0, 0, width, height);

        if (gameState == STATE.GAME || gameState == STATE.LEVEL_UP) {
            renderWorld(g);
            renderHUD(g, ammo);
        }

        if (gameState == STATE.LEVEL_UP)  {
            renderLevelUpOverlay(g, currentUpgradeOptions);
        } else if (gameState == STATE.MENU) {
            renderMenu(g);
        } else if (gameState == STATE.GAME_OVER) {
            renderGameOver(g);
        }

        g.dispose();
        bs.show();
    }

    /** Renders all game objects applying camera translation. */
    private void renderWorld(Graphics g) {
        g.translate((int)-camera.getX(), (int)-camera.getY());
        handler.render(g);
        g.translate((int)camera.getX(), (int)camera.getY());
    }

    /** Renders the HUD: HP bar, EXP bar, ammo counter. */
    private void renderHUD(Graphics g, int ammo) {
        Player p = (Player) handler.getPlayer();
        if (p == null) {
            return;
        }

        int hp = p.getHealth();
        int maxHp = p.getMaxHealth();
        if (maxHp <= 0) {
            return;
        }

        int currentXP = levelManager.getCurrentXP();
        int barW = 200, barH = 20;

        // HP bar
        int hpFill = (int) (((double) hp / maxHp) * barW);
        g.setColor(COL_HP_BG); g.fillRoundRect(0, 0, barW, barH, 10, 10);
        g.setColor(COL_HP_FG); g.fillRoundRect(0, 0, hpFill, barH, 10, 10);
        g.setColor(Color.WHITE); g.drawRoundRect(0, 0, barW, barH, 10, 10);
        g.setFont(fontHUD);
        g.setColor(Color.WHITE);
        g.drawString(hp + " / " + maxHp, 6, 14);

        // EXP bar
        int expX = width - barW - 10;
        int expFill = (int) (((double) currentXP / levelManager.getNextLevelXP()) * barW);
        g.setColor(COL_EXP_BG);  g.fillRoundRect(expX, 0, barW, barH, 10, 10);
        g.setColor(COL_EXP_FG);  g.fillRoundRect(expX, 0, expFill, barH, 10, 10);
        g.setColor(Color.WHITE);  g.drawRoundRect(expX, 0, barW, barH, 10, 10);
        g.setColor(Color.WHITE);
        g.drawString(currentXP + " / " + levelManager.getNextLevelXP(), expX + 6, 14);

        // Ammo counter
        int ammoX = (width / 2) - 50;
        g.setColor(COL_AMMO_BG);  g.fillRoundRect(ammoX, 0, 100, barH, 10, 10);
        g.setColor(ammo <= 10 ? COL_AMMO_LOW : COL_AMMO_OK);
        g.drawRoundRect(ammoX, 0, 100, barH, 10, 10);
        g.setFont(fontHUD);
        g.setColor(ammo <= 10 ? COL_AMMO_LOW : COL_AMMO_OK);
        String ammoStr = "AMMO: " + ammo;
        int ammoStrW = g.getFontMetrics().stringWidth(ammoStr);
        g.drawString(ammoStr, ammoX + (100 - ammoStrW) / 2, 14);
    }

    /** Renders the level-up overlay with upgrade cards. */
    private void renderLevelUpOverlay(Graphics g, List<Upgrade> options) {
        g.setColor(COL_OVERLAY);
        g.fillRect(0, 0, width, height);

        // Titolo
        g.setColor(COL_TITLE);
        g.setFont(fontOverlayBig);
        String title = "scegli un upgrade!";
        g.drawString(title, (width - g.getFontMetrics().stringWidth(title)) / 2, 76);

        // Sottotitolo
        g.setColor(COL_SUBTITLE);
        g.setFont(fontSmall);
        String sub = "clicca su una carta per potenziare il tuo eroe";
        g.drawString(sub, (width - g.getFontMetrics().stringWidth(sub)) / 2, 106);

        // Carte
        for (int i = 0; i < options.size(); i++) {
            Upgrade u = options.get(i);
            int cardX = 110 + (i * 260);
            int cardY = 130, cardW = 230, cardH = 280;

            // Sfondo carta
            g.setColor(COL_CARD_BG);
            g.fillRoundRect(cardX, cardY, cardW, cardH, 18, 18);

            // Bordo rosa
            g.setColor(COL_CARD_BD);
            g.drawRoundRect(cardX, cardY, cardW, cardH, 18, 18);
            g.drawRoundRect(cardX + 2, cardY + 2, cardW - 4, cardH - 4, 16, 16); // doppio bordo carino

            // Nome upgrade
            g.setColor(COL_CARD_NAME);
            g.setFont(fontCard);
            g.drawString(u.getName(), cardX + 16, cardY + 38);

            // Livello
            g.setColor(COL_SUBTITLE);
            g.setFont(fontCardSub);
            g.drawString("livello: " + u.getCurrentLevel(), cardX + 16, cardY + 62);

            // Separatore
            g.setColor(COL_CARD_BD);
            g.drawLine(cardX + 12, cardY + 76, cardX + cardW - 12, cardY + 76);

            // Descrizione
            g.setColor(Color.WHITE);
            g.setFont(fontCardDesc);
            g.drawString(u.getDescription(), cardX + 16, cardY + 102);

            // Footer
            g.setColor(COL_TITLE);
            g.setFont(fontHUD);
            String footer = "~ clicca per scegliere ~";
            g.drawString(footer, cardX + (cardW - g.getFontMetrics().stringWidth(footer)) / 2, cardY + cardH - 14);
        }
    }

    /** Renders the main menu screen. */
    private void renderMenu(Graphics g) {
        g.setColor(COL_MENU_BG);
        g.fillRect(0, 0, width, height);

        // Titolo
        g.setColor(COL_TITLE);
        g.setFont(fontTitle);
        String t = "shOOt";
        g.drawString(t, (width - g.getFontMetrics().stringWidth(t)) / 2, height / 2 - 50);

        // Sottotitolo
        g.setColor(COL_SUBTITLE);
        g.setFont(fontMenu);
        String s = "~ clicca per iniziare ~";
        g.drawString(s, (width - g.getFontMetrics().stringWidth(s)) / 2, height / 2 + 30);
    }

    /** Renders the game over screen. */
    private void renderGameOver(Graphics g) {
        g.setColor(COL_GO_BG);
        g.fillRect(0, 0, width, height);

        int menuW = (int) (width  * 0.82);
        int menuH = (int) (height * 0.72);
        int menuX = (width  - menuW) / 2;
        int menuY = (height - menuH) / 2;

        g.setColor(COL_GO_CARD);
        g.fillRoundRect(menuX, menuY, menuW, menuH, 22, 22);
        g.setColor(COL_GO_BORDER);
        g.drawRoundRect(menuX, menuY, menuW, menuH, 22, 22);
        g.drawRoundRect(menuX + 2, menuY + 2, menuW - 4, menuH - 4, 20, 20);

        // Titolo
        g.setColor(COL_DEAD);
        g.setFont(fontGameOver);
        String dead = "sei morto...";
        g.drawString(dead, menuX + (menuW - g.getFontMetrics().stringWidth(dead)) / 2, menuY + 90);

        // Sottotitolo
        g.setColor(COL_SUBTITLE);
        g.setFont(fontSmall);
        String sub = "il tuo viaggio si è concluso qui nell'arena";
        g.drawString(sub, menuX + (menuW - g.getFontMetrics().stringWidth(sub)) / 2, menuY + 130);

        // Bottoni
        int btnW = 320, btnH = 50;
        int btnX = menuX + (menuW - btnW) / 2;
        drawButton(g, btnX, menuY + 190, btnW, btnH, "premi  'R'  per ricominciare", COL_TITLE);
        drawButton(g, btnX, menuY + 260, btnW, btnH, "premi  'X'  per uscire",      new Color(255, 120, 120));
    }

    /** Draws a styled button with centered text. */
    private void drawButton(Graphics g, int x, int y, int w, int h, String label, Color textColor) {
        g.setColor(COL_BTN_BG);
        g.fillRoundRect(x, y, w, h, 14, 14);
        g.setColor(textColor);
        g.drawRoundRect(x, y, w, h, 14, 14);
        g.setFont(fontCard);
        g.setColor(textColor);
        g.drawString(label, x + (w - g.getFontMetrics().stringWidth(label)) / 2, y + h / 2 + 6);
    }
}