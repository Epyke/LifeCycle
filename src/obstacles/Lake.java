package obstacles;

import world.Cell;
import world.CellType;
import world.Coord;
import world.World;

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
            int randomX = (int)(Math.random() * (super.getWorld().getSize() - 1));
            int randomY = (int)(Math.random() * (super.getWorld().getSize() - 1));

            check = verifyCoords(randomX, randomY);

            if(check){
                newCoords = new Coord(randomX, randomY);
            }
            } while(!check);
        return newCoords;
    }

    public void genObstacle() {
        Coord StartPos = getRandomCoords();

        for (int i = 0; i < size; i++){
            Cell selectedCell = super.getWorld().getGrid().get(StartPos.getY()).get(StartPos.getX());
            selectedCell.setCellType(CellType.WATER);
        }
    }
}

