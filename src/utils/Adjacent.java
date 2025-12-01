package utils;

import world.Cell;
import world.CellType;
import world.Coord;
import world.World;

import java.util.ArrayList;


/**
 * A class Adjacent representa as células vizinhas de uma dada célula
 */
public class Adjacent {
    private static int[][] directions = {{0,1},{1,0},{-1,0},{0,-1}};
    private World w;

    private Adjacent(){}

    /**
     * Verifica se as coordenadas fornecidas são válidas
     * @param w
     * @param x
     * @param y
     * @return retorna um valor booleano consoante a validade da célula
     */
    public static boolean verifyCoords(World w, int x, int y){
        if(x < 0 || x > w.getSize()-1 || y < 0 || y > w.getSize()-1){
            return false;
        }
        return true;
    }


    /**
     * @param w Objeto world
     * @param c Objeto Coord
     * @param type Tipo de células desejadas
     * @return Um arraylist das células vizinhas de uma determinada célula e de um determinado tipo
     */
    public static ArrayList<Cell> getAdjacents(World w, Cell c, CellType type){
        ArrayList<Cell> frontier = new ArrayList<>();
        Coord currCoordsCell = c.getCoord();
        int x = currCoordsCell.getX();
        int y = currCoordsCell.getY();

        int[][] adjacent = {{0,1}, {1,0}, {-1,0}, {0,-1}};

        for(int[] a: adjacent){
            int nx = x + a[0];
            int ny = y + a[1];

            if(verifyCoords(w, nx, ny) && w.getGrid().get(ny).get(nx).getType() == type){
                frontier.add(w.getGrid().get(ny).get(nx));
            }
        }
        return frontier;
    }
}
