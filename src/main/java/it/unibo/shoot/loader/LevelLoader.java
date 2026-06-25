package it.unibo.shoot.loader;

import java.awt.image.BufferedImage;

import it.unibo.shoot.GameObjects.Block;
import it.unibo.shoot.GameObjects.Crate;
import it.unibo.shoot.model.Enemy1;
import it.unibo.shoot.model.Enemy2;
import it.unibo.shoot.model.Enemy3;
import it.unibo.shoot.model.Game;
import it.unibo.shoot.model.Handler;
import it.unibo.shoot.model.ID;
import it.unibo.shoot.model.LevelManager;
import it.unibo.shoot.model.Player;
import it.unibo.shoot.util.Constants;

/**
 * Parses a PNG map image and populates the Handler with the corresponding GameObjects.
 * Each pixel color maps to a specific object type.
 *
 */
public class LevelLoader {

    private final Handler handler;
    private final ResourceLoader resources;
    private final LevelManager levelManager;
    private final Game game;

    /**
     * Creates a LevelLoader.
     *
     * @param handler the handler to populate with objects.
     * @param resources the loaded game resources.
     * @param levelManager the level manager to bind enemies to.
     * @param game the game instance, passed to Player.
     */
    public LevelLoader(Handler handler, ResourceLoader resources, LevelManager levelManager, Game game) {
        this.handler = handler;
        this.resources = resources;
        this.levelManager = levelManager;
        this.game = game;
    }

    /**
     * Parses the map image and populates the handler.
     *
     * @param image the PNG map image to parse.
     * @return the Player instance found in the map, or null if not present.
     */
    public Player load(BufferedImage image) {
        Player player = null;
        int w = image.getWidth();
        int h = image.getHeight();

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                int tx = xx * Constants.TILE_SIZE;
                int ty = yy * Constants.TILE_SIZE;

                if (red == 255 && green == 0 && blue == 0) {
                    handler.addObject(new Block(tx, ty, ID.Block, resources.getTileSS()));

                } else if (blue == 255 && red == 0 && green == 0) {
                    player = new Player(tx, ty, ID.Player, game, resources.getPlayerSS(), handler, game);
                    handler.addObject(player);

                } else if (green == 255 && red == 0 && blue == 0) {
                    handler.addObject(new Enemy1(tx, ty, ID.Enemy, resources.getEnemySS(), handler, levelManager));

                } else if (green == 255 && red == 255 && blue == 0) {
                    handler.addObject(new Enemy2(tx, ty, ID.Enemy, resources.getEnemySS(), handler, levelManager));

                } else if (green == 255 && blue == 255 && red == 0) {
                    handler.addObject(new Enemy3(tx, ty, ID.Enemy, resources.getEnemySS(), handler, levelManager));

                } else if (red == 255 && blue == 255 && green == 0) {
                    handler.addObject(new Crate(tx, ty, ID.Crate, resources.getCrateImage()));
                }
            }
        }
        return player;
    }
}