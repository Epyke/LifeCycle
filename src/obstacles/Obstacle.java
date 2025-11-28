package obstacles;


import world.Cell;
import world.Coord;
import world.World;
import java.util.HashMap;

public abstract class Obstacle {
    private HashMap<Coord, Cell> map;
    private World world;
    public Obstacle(World world){
        map = new HashMap<>();
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public HashMap<Coord, Cell> getMap(){
        return map;
    }

    public abstract boolean verifyCoords(int x, int y);
    public abstract Coord getRandomCoords();
    public abstract void genObstacle();
}
