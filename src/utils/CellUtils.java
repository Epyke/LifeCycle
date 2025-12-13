package utils;

import world.Cell;
import world.Coord;
import world.World;

public class CellUtils {
    private CellUtils(){};

    public static Cell findCell(World world,Coord coords){
        return world.getGrid().get(coords.getY()).get(coords.getX());
    }

    public static Cell findCell(World world, int x, int y){
        return world.getGrid().get(y).get(x);
    }
}
