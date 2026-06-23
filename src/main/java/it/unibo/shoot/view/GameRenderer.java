package it.unibo.shoot.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.List;

//import it.unibo.shoot.model.Game;
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
     * Handles world rendering, HUD, and overlays depending on STATE.
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

        // Clear screen
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);

        if (gameState == STATE.GAME || gameState == STATE.LEVEL_UP) {
            renderWorld(g);
            renderHUD(g, ammo);
        }

        if (gameState == STATE.LEVEL_UP) {
            renderLevelUpOverlay(g, currentUpgradeOptions);
        } else if (gameState == STATE.MENU) {
            renderMenu(g);
        } else if (gameState == STATE.GAME_OVER) {
            renderGameOver(g);
        }

        g.dispose();
        bs.show();
    }

    /**
     * Renders all game objects applying camera translation.
     *
     * @param g the Graphics context.
     */
    private void renderWorld(Graphics g) {
        g.translate((int) -camera.getX(), (int) -camera.getY());
        handler.render(g);
        g.translate((int) camera.getX(), (int) camera.getY());
    }

    /**
     * Renders the heads-up display (HP bar, EXP bar, and ammo counter).
     *
     * @param g the Graphics context.
     * @param ammo current ammo count.
     */
    private void renderHUD(Graphics g, int ammo) {
        Player p = (Player) handler.getPlayer();
        if (p == null) return;

        int hp = p.getHealth();
        int maxHp = p.getMaxHealth();
        if (maxHp <= 0) return;

        int currentXP = levelManager.getCurrentXP();
        int barWidth = 200;
        int barHeight = 20;

        // HP bar
        int HPbarX = 0, HPbarY = 0;
        int currentHPBarWidth = (int) (((double) hp / maxHp) * barWidth);
        g.setColor(new Color(150, 0, 0));
        g.fillRect(HPbarX, HPbarY, barWidth, barHeight);
        g.setColor(new Color(0, 180, 0));
        g.fillRect(HPbarX, HPbarY, currentHPBarWidth, barHeight);
        g.setColor(Color.BLACK);
        g.drawRect(HPbarX, HPbarY, barWidth, barHeight);

        // EXP bar
        int EXPbarX = width - barWidth - 10, EXPbarY = 0;
        int currentEXPBarWidth = (int) (((double) currentXP / levelManager.getNextLevelXP()) * barWidth);
        g.setColor(new Color(100, 0, 0));
        g.fillRect(EXPbarX, EXPbarY, barWidth, barHeight);
        g.setColor(new Color(0, 0, 200));
        g.fillRect(EXPbarX, EXPbarY, currentEXPBarWidth, barHeight);
        g.setColor(Color.BLACK);
        g.drawRect(EXPbarX, EXPbarY, barWidth, barHeight);

        // Ammo counter
        int AMMOx = 460, AMMOy = 0;
        g.setColor(new Color(30, 30, 30, 200));
        g.fillRoundRect(AMMOx, AMMOy, 90, barHeight, 5, 5);
        g.setColor(Color.BLACK);
        g.drawRoundRect(AMMOx, AMMOy, 90, barHeight, 5, 5);
        g.setColor(ammo <= 10 ? Color.RED : Color.ORANGE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("AMMO: " + ammo, AMMOx + 15, AMMOy + 14);
    }

    /**
     * Renders the level-up selection screen with upgrade cards.
     *
     * @param g the Graphics context.
     * @param options available upgrades to choose from.
     */
    private void renderLevelUpOverlay(Graphics g, List<Upgrade> options) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, width, height);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("SCEGLI UN UPGRADE!", width / 2 - 150, 80);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Clicca su una delle opzioni per potenziare il tuo eroe", width / 2 - 160, 110);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        for (int i = 0; i < options.size(); i++) {
            Upgrade u = options.get(i);
            int cardX = 120 + (i * 260);
            int cardY = 180, cardW = 220, cardH = 250;

            g.setColor(new Color(40, 40, 50));
            g.fillRoundRect(cardX, cardY, cardW, cardH, 15, 15);
            g.setColor(new Color(218, 165, 32));
            g.drawRoundRect(cardX, cardY, cardW, cardH, 15, 15);

            g.setColor(Color.CYAN);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString(u.getName(), cardX + 20, cardY + 40);

            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Arial", Font.ITALIC, 13));
            g.drawString("Livello Attuale: " + u.getCurrentLevel(), cardX + 20, cardY + 70);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString(u.getDescription(), cardX + 20, cardY + 140);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("[ CLICCA PER SCEGLIERE ]", cardX + 35, cardY + 220);
        }
    }

    /**
     * Renders the main menu screen.
     */
    private void renderMenu(Graphics g) {
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Impact", Font.PLAIN, 60));
        g.drawString("shOOt", width / 2 - 80, height / 2 - 80);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Clicca per iniziare", width / 2 - 80, height / 2 + 20);
    }

    /**
     * Renders the game over screen with restart/exit options.
     */
    private void renderGameOver(Graphics g) {
        g.setColor(new Color(20, 0, 0, 180));
        g.fillRect(0, 0, width, height);

        int menuW = 1000, menuH = 563;
        int menuX = (width / 2) - (menuW / 2);
        int menuY = (height / 2) - (menuH / 2);

        g.setColor(new Color(30, 10, 10));
        g.fillRoundRect(menuX, menuY, menuW, menuH, 20, 20);
        g.setColor(Color.RED);
        g.drawRoundRect(menuX, menuY, menuW, menuH, 20, 20);

        g.setColor(new Color(255, 50, 50));
        g.setFont(new Font("Impact", Font.PLAIN, 48));
        g.drawString("SEI MORTO!", menuX + 380, menuY + 70);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Il tuo viaggio si è concluso qui nell'arena.", menuX + 350, menuY + 115);

        int btnW = 260, btnH = 45;
        int btnX = (width / 2) - (btnW / 2);
        int btnY1 = menuY + 160;

        g.setColor(new Color(80, 20, 20));
        g.fillRoundRect(btnX, btnY1, btnW, btnH, 10, 10);
        g.setColor(Color.YELLOW);
        g.drawRoundRect(btnX, btnY1, btnW, btnH, 10, 10);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("PREMI 'R' PER RICOMINCIARE", btnX + 18, btnY1 + 28);

        int btnY2 = btnY1 + 65;
        g.setColor(new Color(80, 20, 20));
        g.fillRoundRect(btnX, btnY2, btnW + 5, btnH, 10, 10);
        g.setColor(Color.YELLOW);
        g.drawRoundRect(btnX, btnY2, btnW + 5, btnH, 10, 10);
        g.setColor(Color.RED);
        g.drawString("PREMI 'X' PER USCIRE DAL GIOCO", btnX, btnY2 + 35);
    }
}