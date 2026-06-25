package it.unibo.shoot.audio;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages and plays sounds for the game.
 */
public class Sound {

    /**
     * The sound effect types in the game
     */
    public enum SoundType {
        SHOOT,
        LEVEL_UP,
        GAME_OVER
    }

    private final Map<SoundType, URL> sounds = new HashMap<>();
    private Clip backgroundMusic;

    /**
     * Constructs a Sound instance and loads all audio resources from the classpath.
     */
    public Sound() {
        loadBackgroundMusic("audio/background_music.wav");
        
        loadResource(SoundType.SHOOT, "audio/pop_1.wav");
        loadResource(SoundType.LEVEL_UP, "audio/level_up.wav");
        loadResource(SoundType.GAME_OVER, "audio/game_over.wav");
    }

    /**
     * Plays the sound effect identified by the given SoundType.
     *
     * @param type the sound effect to play.
     */
    public void play(SoundType type) {
        URL url = sounds.get(type);
        if (url == null) {
            System.err.println("Suono non trovato: " + type);
            return;
        }
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.open(audioIn);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println("Errore riproduzione audio: " + e.getMessage());
        }
    }

    /**
     * Loads an audio resource from the classpath and stores it in the map.
     *
     * @param type the SoundType key to associate with the resource.
     * @param path the classpath path of the audio file.
     */
    private void loadResource(SoundType type, String path) {
        URL url = this.getClass().getClassLoader().getResource(path);
        if (url == null) {
            System.err.println("Risorsa audio non trovata: " + path);
            return;
        }
        sounds.put(type, url);
    }

    /**
     * Loads the background music in loop.
     * 
     * @param path of the song to reproduce.
     */
    private void loadBackgroundMusic(String path) {
        try {
            URL url = getClass().getClassLoader().getResource(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);

        } catch (Exception e) {
            System.err.println("Errore caricamento BGM: " + e.getMessage());
        }
    }

    public void startBackgroundMusic() {
        if (backgroundMusic == null) {
            return;
        }

        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic == null) {
            return;
        }
        backgroundMusic.stop();
        backgroundMusic.setFramePosition(0);
    }
}