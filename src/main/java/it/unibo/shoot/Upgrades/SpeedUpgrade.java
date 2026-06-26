package it.unibo.shoot.Upgrades;
import it.unibo.shoot.model.Player;
import it.unibo.shoot.model.Game;

// 1. SCARPE RAPIDE 
public class SpeedUpgrade extends Upgrade {
    Game game;
    public SpeedUpgrade() {
         super("Scarpe Rapide", "+10% Velocità di movimento", 5); 
    }

    @Override
    protected void executeUpgradeLogic(Player p) { 
        p.setSpeed(p.getSpeed() * 1.10); 
         Game game = p.getGame();
          if (game != null) {
            game.ammo += 50; // Aggiunge 50 munizioni
          }
        
    }
}

