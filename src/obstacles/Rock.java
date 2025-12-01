package obstacles;

import utils.Rand;
import world.CellType;
import world.Coord;
import world.Cell;
import world.World;

/**
 * A class Rock representa as Rochas presentes no mundo, cada rocha é gerada numa posição aleatória.
 */
public class Rock extends Obstacle{
    private static final int[][] disp = {{0,1},{1,0},{1,1}};
    public Rock(World world){
        super(world);
    }

    /**
     * Verifica através das coordenadas fornecidas se as células que formam o formato da pedra são válidas e do tipo GRASS
     * @param x
     * @param y
     * @return retorna um valor booleano
     */
    public boolean verifyCoords(int x, int y){
        if (super.getWorld().getGrid().get(y).get(x).getType() != CellType.GRASS){
            return false;
        }

        for(int[] c: disp){
            int nx = x + c[0];
            int ny = y + c[1];

            if (nx > super.getWorld().getSize()-1 || ny > super.getWorld().getSize() - 1 ||
            super.getWorld().getGrid().get(ny).get(nx).getType() != CellType.GRASS){
                return false;
            }
        }

        return true;
    }

    /**
     * Escolhe aleatoriamente uma coordenada válida e que tenha uma célula do tipo GRASS
     * @return as coordenadas aleatorios de uma célula do tipo grass
     */
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

    /**
     * Geração das rochas no mundo
     * Altera o estado das células do tipo Grass para ROCK
     */
    public void genObstacle(){
        Coord newCoords = getRandomCoords();
        Cell selectedCell = super.getWorld().getGrid().get(newCoords.getY()).get(newCoords.getX());
        selectedCell.setCellType(CellType.ROCK);
        super.getMap().put(newCoords, selectedCell);

        for(int[] c: disp){
            int nx = newCoords.getX() + c[0];
            int ny = newCoords.getY() + c[1];

            Cell selectedCell2 = super.getWorld().getGrid().get(ny).get(nx);
            selectedCell2.setCellType(CellType.ROCK);
            super.getMap().put(selectedCell2.getCoord(), selectedCell2);
        }
    }
}
