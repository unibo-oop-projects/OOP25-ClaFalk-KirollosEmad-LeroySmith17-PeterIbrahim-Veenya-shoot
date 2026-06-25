package it.unibo.shoot.upgrades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.shoot.Upgrades.*;
import it.unibo.shoot.loader.SpriteSheet;
import it.unibo.shoot.model.Game;
import it.unibo.shoot.model.Handler;
import it.unibo.shoot.model.ID;
import it.unibo.shoot.model.LevelManager;
import it.unibo.shoot.model.Player;
import it.unibo.shoot.model.STATE;
public class UpgradeAndLevelUpTest {

    private Player player;
    private LevelManager levelManager;

    @BeforeEach
    public void setUp() {
        // 1. Inizializzo i componenti minimi di controllo
        Canvas dummyCanvas = new Canvas();
        Handler dummyHandler = new Handler();
        Game dummyGame = new Game();
        levelManager = new LevelManager(dummyGame);
        levelManager.setPlayer(player);
        // 2. creo  uno spritesheet fittizio valido in memoria
        // creo un'immagine vuota grande abbastanza (es. 128x60) per non andare fuori dai bordi.
        BufferedImage dummyImage = new BufferedImage(128, 60, BufferedImage.TYPE_INT_ARGB);
        SpriteSheet dummySpriteSheet = new SpriteSheet(dummyImage);

        // 3. passo lo spritesheet valido al player
        player = new Player(0, 0, ID.Player, dummyCanvas, dummySpriteSheet, dummyHandler, dummyGame);
        player.setDamageMultiplier(1.0);

        // 4. Inizializzo il LevelManager legato al player
    }

    @Test
    public void testDamageUpgradeApplication() {
        Upgrade damageUpgrade = new DamageUpgrade();
        
        assertEquals(0, damageUpgrade.getCurrentLevel());
        assertEquals(1.0, player.getDamageMultiplier(), 0.001);

        // Applico l'upgrade (+15% danno)
        damageUpgrade.apply(player);
        
        assertEquals(1, damageUpgrade.getCurrentLevel());
        assertEquals(1.15, player.getDamageMultiplier(), 0.001);
    }

    @Test
    public void testUpgradeMaxLevelConstraint() {
        Upgrade damageUpgrade = new DamageUpgrade(); // maxLevel = 5
        
        for (int i = 0; i < 5; i++) {
            assertFalse(damageUpgrade.isMaxed());
            damageUpgrade.apply(player);
        }

        assertTrue(damageUpgrade.isMaxed());
        assertEquals(5, damageUpgrade.getCurrentLevel());
        
        double damageAfterMax = player.getDamageMultiplier();

        // Il sesto tentativo non deve applicare modifiche o incrementare il livello
        damageUpgrade.apply(player);
        assertEquals(5, damageUpgrade.getCurrentLevel());
        assertEquals(damageAfterMax, player.getDamageMultiplier(), 0.001);
    }

    @Test
    public void testXPAccumulationAndLevelUp() {
        Game dummyGame = new Game();
        assertEquals(0, levelManager.getCurrentXP());
        assertEquals(100, levelManager.getNextLevelXP());

        levelManager.addXP(50);
        assertEquals(50, levelManager.getCurrentXP());

        // Raggiungendo 110 XP scatta il livello (110 - 100 = 10 XP rimanenti)
        levelManager.addXP(60);
        
        assertEquals(10, levelManager.getCurrentXP());
        assertEquals(125, levelManager.getNextLevelXP());
        
        assertEquals(STATE.MENU, dummyGame.gameState);
    }

    @Test
    public void testGetRandomUpgradesFiltersMaxed() {
        List<Upgrade> initialOptions = levelManager.getRandomUpgrades(4);
        assertEquals(4,initialOptions.size(),"Il pool iniziale conta solo 3 ");
        

        // Trova l'upgrade del danno e portalo al massimo
        Upgrade damageUpgrade = initialOptions.stream()
                .filter(u -> u instanceof DamageUpgrade)
                .findFirst()
                .orElseThrow();
        
        for (int i = 0; i < 5; i++) {
            damageUpgrade.apply(player);
        }
        assertTrue(damageUpgrade.isMaxed());

        // Verifica che richiedendo nuovi upgrade casuali, quello massimo sia escluso dal pool delle scelte
        for (int check = 0; check < 10; check++) {
            List<Upgrade> options = levelManager.getRandomUpgrades(3);
            boolean containsMaxed = options.stream().anyMatch(Upgrade::isMaxed);
            assertFalse(containsMaxed, "Il LevelManager ha restituito un upgrade già al livello massimo!");
        }
    }
}
