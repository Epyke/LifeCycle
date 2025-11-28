package world;

import java.util.ArrayList;

public class Coord {
    private int x, y;
    public Coord(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public ArrayList<Integer> getCoords(){
        ArrayList<Integer> res = new ArrayList<>(2);
        res.add(x);
        res.add(y);
        return res;
    }
}