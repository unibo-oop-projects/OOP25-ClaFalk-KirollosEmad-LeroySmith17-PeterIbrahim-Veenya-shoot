package it.unibo.shoot.Upgrades;
import it.unibo.shoot.model.Player;
import it.unibo.shoot.model.Game;

public class EvasionUpgrade extends Upgrade {
    Game game;
    public EvasionUpgrade() { 
        super("Mantello Schivata", "+5% Possibilità di schivata", 5); 
    }

    @Override 
    protected void executeUpgradeLogic(Player p) { 
        p.setDodgeChance(p.getDodgeChance() + 0.05);
         Game game = p.getGame();
          if (game != null) {
            game.ammo += 50; // Aggiunge 50 munizioni
          }
        
    }
}
