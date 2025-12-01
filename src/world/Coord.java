package world;

import java.util.ArrayList;

/**
 * A class Coord representa o conjunto de coordenadas x e y.
 */
public class Coord {
    private int x, y;

    /**
     *
     * @param x Representa a coordenada x de um conjunto de coordenadas
     * @param y Representa a coordenada y de um conjunto de coordenadas
     */
    public Coord(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return getter da coordenada x
     */
    public int getX(){
        return x;
    }

    /**
     * @return getter da coordenada y
     */
    public int getY(){
        return y;
    }

    /**
     * @return um ArrayList com as coordenadas x e y.
     */
    public ArrayList<Integer> getCoords(){
        ArrayList<Integer> res = new ArrayList<>(2);
        res.add(x);
        res.add(y);
        return res;
    }
}