package it.unibo.shoot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerModelTest {

    @Test
    void testInitialState() {
        
        PlayerModel player = new PlayerModel(100.0, 100.0, 5.0, 100);

        
        assertEquals(100, player.getHealth(), "La vita iniziale deve essere 100");
        assertEquals(100, player.getMaxHealth(), "La vita massima deve essere 100");
        assertFalse(player.isDead(), "Il giocatore non deve essere morto all'inizio");
    }

    @Test
    void testTakeDamageAndIFrames() {
      
        PlayerModel player = new PlayerModel(0, 0, 5.0, 100);
        player.setDodgeChance(0.0); 

        
        player.takeDamage(10);
        
        assertEquals(90, player.getHealth(), "I calcoli del danno sono errati");

       
        player.takeDamage(50);

        
        assertEquals(90, player.getHealth(), "Fallimento I-Frames: il danno è passato nel periodo di invincibilità");
    }

    @Test
    void testHealLimits() {
        
        PlayerModel player = new PlayerModel(0, 0, 5.0, 100);
        player.setHealth(80); 
        
       
        player.heal(50);
        
      
        assertEquals(100, player.getHealth(), "Fallimento blocco cura: la vita ha superato maxHealth");
    }

    @Test
    void testUpdatePositionVector() {
        
        PlayerModel player = new PlayerModel(10.0, 10.0, 5.0, 100);
        
        
      
        player.setVelocity(1.0f, -1.0f); 
        player.updatePosition();
        
       
        assertEquals(15.0, player.getX(), "La posizione X non è stata calcolata correttamente");
        assertEquals(5.0, player.getY(), "La posizione Y non è stata calcolata correttamente");
        assertTrue(player.isMoving(), "Il flag isMoving non si è attivato");
    }
    
    @Test
    void testDodgeMechanic() {
        
        PlayerModel player = new PlayerModel(0, 0, 5.0, 100);
        
       
        player.setDodgeChance(1.0); 
        player.takeDamage(50);
        
       
        assertEquals(100, player.getHealth(), "La meccanica di schivata ha fallito");
    }

    @Test
    void testDeathState() {
        
        PlayerModel player = new PlayerModel(0, 0, 5.0, 100);
        player.setDodgeChance(0.0);

        
        player.takeDamage(999);

        
        assertEquals(0, player.getHealth(), "La vita è scesa sotto lo zero (valore negativo)");
        
        
        assertTrue(player.isDead(), "Il flag isDead non è scattato alla morte");
    }
}
