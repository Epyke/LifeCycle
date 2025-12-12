package entities;

import world.Coord;
import world.World;

public abstract class Entity {
    private World world;
    private Coord coords;
    private int currentAge;
    private boolean isDead;

    public Entity(World world, Coord coords){
        this.world = world;
        this.coords = coords;
        currentAge = 0;
        isDead = false;
    }

    public World getWorld() {
        return world;
    }

    public Coord getCoords() {
        return coords;
    }

    public void setCoords(Coord coords){
        this.coords = coords;
    }

    public int getAge() {
        return currentAge;
    }

    public void incrementAge(){
        currentAge++;
    }

    public boolean getIsDead(){
        return isDead;
    }

    public void setDied(){
        isDead = true;
    }

    public abstract void updateStats();
    public abstract void die();
    public abstract void updateAge();
    public abstract boolean reproduction();
}
