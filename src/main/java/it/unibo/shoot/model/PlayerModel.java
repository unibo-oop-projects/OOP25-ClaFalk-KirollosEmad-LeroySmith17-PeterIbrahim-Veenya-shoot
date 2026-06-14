package it.unibo.shoot.model;
import java.awt.Rectangle;

public class PlayerModel {
    private double x, y;
    private double speed;
    private int health;
    private int maxHealth;
    private boolean isDead = false;
   
    
    // --- NEW UPGRADE STATS FIELDS ---
    private double damageMultiplier = 1.0; // Base damage is 100%
    private double dodgeChance = 0.0;      // Base dodge chance is 0%
    private double pickupRange = 1.0;      // Base multiplier for XP magnet range

    private float velX = 0, velY = 0;
    
    // Variabili per l'animazione
    private int aniTick, aniIndex, aniSpeed = 10; 
    private boolean isMoving = false;
    private int row = 0; // 0: Giù, 1: Su, 2: Sinistra, 3: Destra

    // Dimensioni per la Hitbox (rimangono 32x32 per la fisica del gioco)
    private final int width = 32;
    private final int height = 32;

    // Variabili per l'invincibilità
    private long lastDamageTime = 0; 
    private final int iFramesDuration = 1000; // 1000 millisecondi = 1 secondo

    public PlayerModel(double startX, double startY, double speed, int maxHealth) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    // --- NEW UPGRADE GETTERS & SETTERS ---
    public double getSpeed() { 
        return speed; 
    }
    
    public void setSpeed(double speed) { 
        this.speed = speed; 
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth; // Clamp to max health
        }
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public double getDodgeChance() {
        return dodgeChance;
    }

    public void setDodgeChance(double dodgeChance) {
        this.dodgeChance = dodgeChance;
    }

    public double getPickupRange() {
        return pickupRange;
    }

    public void setPickupRange(double pickupRange) {
        this.pickupRange = pickupRange;
    }


    public void setVelocity(float dx, float dy) {
        this.velX = dx * (float)speed;
        this.velY = dy * (float)speed;
        this.isMoving = (dx != 0 || dy != 0);

        if (dy > 0) row = 0;      // Cammina in giù 
        else if (dy < 0) row = 1; // Cammina in su 
        else if (dx < 0) row = 3; // <--- INVERTITO: Sinistra ora legge la riga 3
        else if (dx > 0) row = 2; // <--- INVERTITO: Destra ora legge la riga 2
    }

    public void updatePosition() {
        this.x += velX;
        this.y += velY;
    }

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

    public void takeDamage(int damage) {
        // 1. Se sei già a 0, il codice si ferma. I morti non prendono danni extra.
        if (this.health <= 0) {
        this.health = 0;
        this.isDead = true; // <--- FONDAMENTALE!
        System.out.println("GAME OVER! Vita: 0");
    }

    // Incorporate Dodge Mechanism before checking damage
        if (Math.random() < this.dodgeChance) {
            System.out.println("Schivato! Nessun danno subito.");
            return; 
        }

        // 2. Controllo del tempo: chiediamo a Java l'ora esatta in millisecondi
        long currentTime = System.currentTimeMillis();
        
        // Se la differenza tra "ora" e "l'ultima volta che hai preso danno" è minore di 1 secondo...
        if (currentTime - lastDamageTime < iFramesDuration) {
            return; // ...ignora il colpo! Sei invincibile.
        }

        // 3. Se il codice arriva fin qui, è passato più di 1 secondo. Prendi danno!
        this.health -= damage;
        this.lastDamageTime = currentTime; // Resetta il cronometro per il prossimo colpo

        // 4. Controllo del Game Over
        if (this.health <= 0) {
            this.health = 0;
            System.out.println("GAME OVER! Vita: 0");
        } else {
            System.out.println("Danno subito! Vita attuale: " + this.health);
        }
    }


    // Setters e Getters
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

    public Rectangle getHitbox() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    public boolean isDead() {
        return this.isDead;
    }

    public void setHealth(int health) {
        this.health = health ; 
    }

    /*public int currentXP() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'currentXP'");
    }*/
}