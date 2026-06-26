package it.unibo.shoot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerModelTest {

    @Test
    void testInitialState() {
        // ARRANGE & ACT
        PlayerModel player = new PlayerModel(100.0, 100.0, 5.0, 100);

        // ASSERT
        assertEquals(100, player.getHealth(), "La vita iniziale deve essere 100");
        assertEquals(100, player.getMaxHealth(), "La vita massima deve essere 100");
        assertFalse(player.isDead(), "Il giocatore non deve essere morto all'inizio");
    }

    @Test
    void testTakeDamageAndIFrames() {
        // ARRANGE: Creazione giocatore (X=0, Y=0, Speed=5.0, HP=100)
        PlayerModel player = new PlayerModel(0, 0, 5.0, 100);
        player.setDodgeChance(0.0); // Assicuriamoci che non schivi per caso

        // ACT 1: Danno iniziale
        player.takeDamage(10);
        
        // ASSERT 1
        assertEquals(90, player.getHealth(), "I calcoli del danno sono errati");

        // ACT 2: Secondo danno immediato (I-Frames attivi)
        player.takeDamage(50);

        // ASSERT 2: La vita deve restare 90
        assertEquals(90, player.getHealth(), "Fallimento I-Frames: il danno è passato nel periodo di invincibilità");
    }

    @Test
    void testHealLimits() {
        // ARRANGE
        PlayerModel player = new PlayerModel(0, 0, 5.0, 100);
        player.setHealth(80); // Forziamo la vita a 80
        
        // ACT: Cura massiccia
        player.heal(50);
        
        // ASSERT: Il cap massimo (100) deve bloccare l'over-healing
        assertEquals(100, player.getHealth(), "Fallimento blocco cura: la vita ha superato maxHealth");
    }

    @Test
    void testUpdatePositionVector() {
        // ARRANGE: (X=10, Y=10, Speed=5.0, HP=100)
        PlayerModel player = new PlayerModel(10.0, 10.0, 5.0, 100);
        
        // ACT: Direzione X=1, Y=-1. Il metodo setVelocity moltiplica per speed (5.0).
        // Quindi velX diventa 5.0 e velY diventa -5.0.
        player.setVelocity(1.0f, -1.0f); 
        player.updatePosition();
        
        // ASSERT: Comparazione esatta con i double
        assertEquals(15.0, player.getX(), "La posizione X non è stata calcolata correttamente");
        assertEquals(5.0, player.getY(), "La posizione Y non è stata calcolata correttamente");
        assertTrue(player.isMoving(), "Il flag isMoving non si è attivato");
    }
    
    @Test
    void testDodgeMechanic() {
        // ARRANGE
        PlayerModel player = new PlayerModel(0, 0, 5.0, 100);
        
        // ACT: 100% probabilità di schivata
        player.setDodgeChance(1.0); 
        player.takeDamage(50);
        
        // ASSERT
        assertEquals(100, player.getHealth(), "La meccanica di schivata ha fallito");
    }

    @Test
    void testDeathState() {
        // ARRANGE
        PlayerModel player = new PlayerModel(0, 0, 5.0, 100);
        player.setDodgeChance(0.0);

        // ACT: Danno letale 
        player.takeDamage(999);

        // ASSERT
        assertEquals(0, player.getHealth(), "La vita è scesa sotto lo zero (valore negativo)");
        
        // ATTENZIONE: Questo test fallirà finché non aggiungi "this.isDead = true;" 
        // in fondo al tuo metodo takeDamage() dentro l'if (this.health <= 0)
        assertTrue(player.isDead(), "Il flag isDead non è scattato alla morte");
    }
}