package obstacles;

import utils.Adjacent;
import utils.Rand;
import world.*;

import java.util.ArrayList;
import java.util.Objects;

public class Lake extends Obstacle{

    private int size;
    public Lake(World world, int size){
        super(world);
        this.size = size;
    }

    public boolean verifyCoords(int x, int y){
        if(super.getWorld().getGrid().get(y).get(x).getType() != CellType.GRASS){
            return false;
        }
        return true;
    }

    public Coord getRandomCoords(){
        boolean check = false;
        Coord newCoords = null;
        do{
            int randomX = Rand.getRandomNmb(super.getWorld().getSize() - 1);
            int randomY = Rand.getRandomNmb(super.getWorld().getSize() - 1);

            check = verifyCoords(randomX, randomY);

            if(check){
                newCoords = new Coord(randomX, randomY);
            }
            } while(!check);
        return newCoords;
    }

    public void genObstacle() {
        int currSize = 0;
        ArrayList<Cell> frontier = new ArrayList<>();
        Coord StartPos = getRandomCoords();
        Cell center = super.getWorld().getGrid().get(StartPos.getY()).get(StartPos.getX());
        center.setCellType(CellType.WATER);
        super.getMap().put(StartPos, center);
        currSize++;

        frontier = Adjacent.getAdjacents(super.getWorld(),center, CellType.GRASS);

        for(Cell c: frontier){
            if (c.getType() != CellType.GRASS){
                frontier.remove(c);
            }
        }

        while (currSize <= size && !frontier.isEmpty()){
            int randomIndex = (int)(Math.random() * frontier.size());
            Cell next = frontier.get(randomIndex);

            frontier.remove(randomIndex);

            next.setCellType(CellType.WATER);
            super.getMap().put(next.getCoord(), next);
            frontier.addAll(Adjacent.getAdjacents(super.getWorld(), next, CellType.GRASS));
            currSize++;
        }
    }
}

