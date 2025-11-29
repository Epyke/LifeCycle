package obstacles;

import world.CellType;
import world.Coord;
import world.Cell;
import world.World;

public class Rock extends Obstacle{

    public Rock(World world){
        super(world);
    }

    public boolean verifyCoords(int x, int y){
        if(super.getWorld().getGrid().get(y).get(x).getType() != CellType.GRASS ||
                super.getWorld().getGrid().get(y+1).get(x).getType() != CellType.GRASS ||
                super.getWorld().getGrid().get(y).get(x+1).getType() != CellType.GRASS ||
                super.getWorld().getGrid().get(y+1).get(x+1).getType() != CellType.GRASS){
            return false;
        }

        return true;
    }

    public Coord getRandomCoords(){
        boolean check = false;
        Coord newCoords = null;
        do{
            int randomX = (int)(Math.random() * (super.getWorld().getSize() - 2));
            int randomY = (int)(Math.random() * (super.getWorld().getSize() - 2));

            check = verifyCoords(randomX, randomY);

            if(check){
                newCoords = new Coord(randomX, randomY);
            }
        } while(!check);
        return newCoords;
    }

    public void genObstacle(){
        Coord newCoords = getRandomCoords();
        Cell selectedCell = super.getWorld().getGrid().get(newCoords.getY()).get(newCoords.getX());
        selectedCell.setCellType(CellType.ROCK);
        super.getMap().put(selectedCell.getCoord(), selectedCell);

        selectedCell = super.getWorld().getGrid().get(newCoords.getY()+1).get(newCoords.getX());
        selectedCell.setCellType(CellType.ROCK);
        super.getMap().put(selectedCell.getCoord(), selectedCell);

        selectedCell = super.getWorld().getGrid().get(newCoords.getY()).get(newCoords.getX()+1);
        selectedCell.setCellType(CellType.ROCK);
        super.getMap().put(selectedCell.getCoord(), selectedCell);

        selectedCell = super.getWorld().getGrid().get(newCoords.getY()+1).get(newCoords.getX()+1);
        selectedCell.setCellType(CellType.ROCK);
        super.getMap().put(selectedCell.getCoord(), selectedCell);
    }
}
