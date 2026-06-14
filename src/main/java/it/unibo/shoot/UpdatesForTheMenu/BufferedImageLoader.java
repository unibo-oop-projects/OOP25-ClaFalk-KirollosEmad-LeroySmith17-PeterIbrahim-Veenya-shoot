package it.unibo.shoot.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Utility class to load images ad BufferedImage from classpath resources.
 */
public class BufferedImageLoader {

    /** Image to be loaded. */
    private BufferedImage image;

    /**
     * Loads image from specified path.
     * @param path path of the image.
     * @return loaded BufferedImage, or null.
     */
    public BufferedImage loadImage(String path) {
        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return image;
    }
    
}
