package world;

import obstacles.Rock;

import java.util.ArrayList;

public class World {
    private ArrayList<ArrayList<Cell>> grid;
    private final int size;
    public World(int size){
        grid = new ArrayList<>();
        this.size = size;
        if (!(size > 0)){
            throw new IllegalArgumentException("Size needs to be > 0");
        }
    }

    public void genRandomRocks(int limit){
        int rdmNumb = (int)(Math.random() * limit);
        for(int i = 0;i < rdmNumb; i++){
            new Rock(this).genObstacle();
        }
    }

    public void worldGen(){
        for(int i = 0; i < size; i++){
            grid.add(new ArrayList<Cell>());
        }

        int y = 0;
        for(ArrayList<Cell> rows: grid){
            for(int x = 0; x < size; x++){
                rows.add(new Cell(new Coord(x,y), CellType.GRASS));
            }
            y++;
        }

        genRandomRocks(5);
    }

    public String worldView(){
        String res = "";
        for(ArrayList<Cell> rows: grid){
            for(Cell c: rows){
               res += String.valueOf(c.getType().getSymb()) + "  ";
            }
            res += "\n";
        }

        return res;
    }

    public ArrayList<ArrayList<Cell>> getGrid(){
        return grid;
    }

    public int getSize(){
        return size;
    }
}
