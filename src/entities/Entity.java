package entities;

import world.Coord;
import world.World;

public abstract class Entity {
    private World world;
    private Coord coords;
    private int age;

    public Entity(World world, Coord coords){
        this.world = world;
        this.coords = coords;
    }

    public abstract boolean reproduction();

    public World getWorld() {
        return world;
    }

    public Coord getCoords() {
        return coords;
    }

    public int getAge() {
        return age;
    }
}
