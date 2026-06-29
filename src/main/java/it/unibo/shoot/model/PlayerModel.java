package it.unibo.shoot.model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Modello logico del giocatore.
 * Custodisce lo stato matematico (coordinate fisiche, velocità), i parametri vitali 
 * e la logica di business associata al combattimento e alla telemetria.
 * Questa classe è totalmente indipendente dal rendering grafico e dai sistemi I/O hardware,
 * permettendone il test automatizzato in totale isolamento.
 */
public class PlayerModel {
   
    private double x, y;
    private double speed;
    private int health;
    private int maxHealth;
    private boolean isDead = false;
    
    private final List<Integer> damageHistory = new ArrayList<>();
    
    private double damageMultiplier = 1.0; 
    private double dodgeChance = 0.0;       
    private double pickupRange = 1.0;       

    private float velX = 0, velY = 0;
    
    private int aniTick, aniIndex, aniSpeed = 10; 
    private boolean isMoving = false;
    private int row = 0; 

    private final int width = 32;
    private final int height = 32;

    private long lastDamageTime = 0; 
    private final int iFramesDuration = 1000; 

    public PlayerModel(double startX, double startY, double speed, int maxHealth) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    /**
     * Ripristina la salute del giocatore applicando un hard-clamping superiore.
     * Garantisce matematicamente che la vita corrente non superi mai il limite massimo.
     *
     * @param amount La quantità di punti vita grezzi da ripristinare.
     */
    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth; 
        }
    }

    /**
     * Traduce gli input direzionali in vettori di velocità spaziale scalati per la statistica 'speed'.
     * Dedurrà dinamicamente l'indice direzionale ('row') da fornire alla View per il rendering vettoriale.
     *
     * @param dx Moltiplicatore di direzione sull'asse X (-1.0f, 0.0f, 1.0f).
     * @param dy Moltiplicatore di direzione sull'asse Y (-1.0f, 0.0f, 1.0f).
     */
    public void setVelocity(float dx, float dy) {
        this.velX = dx * (float)speed;
        this.velY = dy * (float)speed;
        this.isMoving = (dx != 0 || dy != 0);
        
        if (dy > 0) row = 0;      
        else if (dy < 0) row = 1; 
        else if (dx < 0) row = 3; 
        else if (dx > 0) row = 2; 
    }

    /**
     * Applica l'ultimo vettore di velocità (velX, velY) calcolato alle coordinate spaziali assolute.
     */
    public void updatePosition() {
        this.x += velX;
        this.y += velY;
    }

    /**
     * Avanza l'orologio dell'animazione (tick) per ciclare passivamente tra i frame
     * del movimento. Se il giocatore è stazionario, forza l'indice al frame di Idle.
     */
    public void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 4) { 
                aniIndex = 0;
            }
        }
        
        if (!isMoving) {
            aniIndex = 0;
        }
    }

    /**
     * Applica una scalatura sottrattiva alla vita valutando una gerarchia di filtri difensivi:
     * 1. Limite vitale: Annulla l'impatto se l'entità risulta già defunta.
     * 2. Evasione Stocastica: RNG contro dodgeChance.
     * 3. Disaccoppiamento temporale: Ignora i danni reiterati entro la soglia degli iFramesDuration.
     *
     * Se l'attacco perfora i filtri, innesca la registrazione per la telemetria (damageHistory)
     * e muta lo stato di isDead() qualora la salute scenda a zero.
     *
     * @param damage Quantità grezza di danno valutato.
     */
    public void takeDamage(int damage) {
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true; 
            return; 
        }

        if (Math.random() < this.dodgeChance) {
            return; 
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDamageTime < iFramesDuration) {
            return;
        }

        this.health -= damage;
        this.lastDamageTime = currentTime; 
        this.damageHistory.add(damage);
        
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true; 
        }
    }

    /**
     * Costruisce il limite volumetrico (Bounding Box) necessario al calcolo delle compenetrazioni solide.
     * @return Rettangolo di classe AWT ancorato alle coordinate attuali X e Y.
     */
    public Rectangle getHitbox() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    // ==========================================
    // METODI DI DELEGA E ACCESSO
    // ==========================================
   
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public double getDamageMultiplier() { return damageMultiplier; }
    public void setDamageMultiplier(double damageMultiplier) { this.damageMultiplier = damageMultiplier; }
    public double getDodgeChance() { return dodgeChance; }
    public void setDodgeChance(double dodgeChance) { this.dodgeChance = dodgeChance; }
    public double getPickupRange() { return pickupRange; }
    public void setPickupRange(double pickupRange) { this.pickupRange = pickupRange; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public int getRow() { return row; }
    public int getAniIndex() { return aniIndex; }
    public boolean isMoving() { return isMoving; }
    public float getVelX() { return velX; }
    public float getVelY() { return velY; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth;}
    public boolean isDead() { return this.isDead; }
    public void setHealth(int health) { this.health = health ; }

    /**
     * Interroga la cronologia telemetrica per calcolare la media dei danni percorsi.
     * Utilizza Java Stream API associato al wrapper monadico OptionalDouble per 
     * prevenire eccezioni divisione-per-zero qualora non sussistano danni in registro.
     * @return La stima frazionaria della media calcolata, o 0.0 se la storia è vuota.
     */
    public double getAverageDamageTaken() {
        return damageHistory.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0);
    }

    /**
     * Scandaglia la telemetria storica estraendo la collisione dal maggiore impatto.
     * @return Il valore di danno massimo assorbito, o 0 in caso la collezione sia intatta.
     */
    public int getMaxDamageTaken() {
        return damageHistory.stream()
                            .mapToInt(Integer::intValue)
                            .max()
                            .orElse(0);
    }
}
