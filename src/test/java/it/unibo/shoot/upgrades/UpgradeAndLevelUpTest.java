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
    private Game dummyGame;
    @BeforeEach
    public void setUp() {
        // 1. Inizializzo i componenti minimi di controllo
        Canvas dummyCanvas = new Canvas();
        Handler dummyHandler = new Handler();
        dummyGame = new Game();
        levelManager = new LevelManager(dummyGame);
        levelManager.setPlayer(player);
        // 2. creo  uno spritesheet fittizio valido in memoria
        // creo un'immagine vuota grande abbastanza (es. 128x60) per non andare fuori dai bordi.
        BufferedImage dummyImage = new BufferedImage(128, 60, BufferedImage.TYPE_INT_ARGB);
        SpriteSheet dummySpriteSheet = new SpriteSheet(dummyImage);

        // 3. passo lo spritesheet valido al player
        player = new Player(0, 0, ID.Player, dummyCanvas, dummySpriteSheet, dummyHandler, dummyGame);
        player.setDamageMultiplier(1.0);

        // 4. Collegamento del player al LevelManager
        levelManager.setPlayer(player);    
    }   



    /**
     * Verifica l'applicazione base del potenziamento del danno.
     * Controlla l'incremento di livello dell'upgrade e la corretta modifica dei moltiplicatori del Player.
     */

    @Test
    public void testDamageUpgradeApplication() {
        Upgrade damageUpgrade = new DamageUpgrade();
        // Verifica dello stato iniziale dell'upgrade e del moltiplicatore del danno del player
        assertEquals(0, damageUpgrade.getCurrentLevel());
        assertEquals(1.0, player.getDamageMultiplier(), 0.001);

        // Applico l'upgrade (+15% danno)
        damageUpgrade.apply(player);
        // Verifica che il livello sia incrementato e che il moltiplicatore rifletta il bonus del 15%
        assertEquals(1, damageUpgrade.getCurrentLevel());
        assertEquals(1.15, player.getDamageMultiplier(), 0.001);
    }
    

    /**
     * Verifica che i potenziamenti rispettino il vincolo del livello massimo impostato (Max Level = 5).
     * Assicura che ulteriori applicazioni oltre il limite non alterino lo stato del gioco.
     */

    @Test
    public void testUpgradeMaxLevelConstraint() {
        Upgrade damageUpgrade = new DamageUpgrade(); // maxLevel = 5
        // Portiamo l'upgrade al livello massimo consentito mediante un ciclo
        for (int i = 0; i < 5; i++) {
            assertFalse(damageUpgrade.isMaxed());
            damageUpgrade.apply(player);
        }
        // Verifica che l'upgrade sia correttamente identificato come "al massimo"
        assertTrue(damageUpgrade.isMaxed(),"L'upgrade non dovrebbe essere al massimo prima del quinto incremento");
        assertEquals(5, damageUpgrade.getCurrentLevel());
        // Memorizziamo il moltiplicatore del danno al livello massimo per i controlli successivi
        double damageAfterMax = player.getDamageMultiplier();

        // Il sesto tentativo non deve applicare modifiche o incrementare il livello
        damageUpgrade.apply(player);
        assertEquals(5, damageUpgrade.getCurrentLevel());
        assertEquals(damageAfterMax, player.getDamageMultiplier(), 0.001);
    }
     


    /**
     * Verifica la corretta gestione dell'esperienza accumulata, il calcolo della XP residua
     * al passaggio di livello e il conseguente cambio di stato del gioco.
     */

    @Test
    public void testXPAccumulationAndLevelUp() {
        
        // Verifica delle condizioni di partenza della XP al livello 1

        assertEquals(0, levelManager.getCurrentXP());
        assertEquals(100, levelManager.getNextLevelXP());
        
        // Aggiunta parziale di XP che non deve determinare alcun passaggio di livello
        levelManager.addXP(50);
        assertEquals(50, levelManager.getCurrentXP());

        // Raggiungendo 110 XP scatta il livello (110 - 100 = 10 XP rimanenti)
        levelManager.addXP(60);
        
        assertEquals(10, levelManager.getCurrentXP());
        assertEquals(125, levelManager.getNextLevelXP());
        // Verifica che il gioco sia transitato nello stato corretto
        assertEquals(STATE.LEVEL_UP, dummyGame.gameState);
    }


    /**
     * Verifica che la selezione casuale dei potenziamenti escluda automaticamente quelli 
     * che hanno già raggiunto il livello massimo consentito.
     */
    @Test
    public void testGetRandomUpgradesFiltersMaxed() {
      // Recupero del pool iniziale delle opzioni di upgrade disponibili
        List<Upgrade> initialOptions = levelManager.getRandomUpgrades(4);
        assertEquals(4, initialOptions.size(), "Il pool iniziale deve contenere esattamente 4 opzioni di scelta");
        
        // Ricerca dell'istanza specifica del potenziamento del danno all'interno della lista
        Upgrade damageUpgrade = initialOptions.stream()
                .filter(u -> u instanceof DamageUpgrade)
                .findFirst()
                .orElseThrow(() -> new AssertionError("L'upgrade DamageUpgrade deve essere presente nel pool iniziale"));
        
        // Sviluppiamo al massimo il potenziamento del danno trovato
        for (int i = 0; i < 5; i++) {
            damageUpgrade.apply(player);
        }
        assertTrue(damageUpgrade.isMaxed());

        // Eseguiamo molteplici campionamenti casuali (10 tentativi) per avere la certezza statistica
        // che l'upgrade massimizzato sia stato rimosso in modo permanente dal sistema di estrazione
        for (int check = 0; check < 10; check++) {
            List<Upgrade> options = levelManager.getRandomUpgrades(3);
            
            // Verifica che nessuna delle opzioni estratte corrisponda a un upgrade già completato
            boolean containsMaxed = options.stream().anyMatch(Upgrade::isMaxed);
            assertFalse(containsMaxed, "Il LevelManager ha restituito un upgrade già al livello massimo!");
        }
    }
}
