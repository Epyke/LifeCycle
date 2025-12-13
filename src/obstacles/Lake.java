package obstacles;

import structures.HabitatType;
import utils.Adjacent;
import utils.Rand;
import world.*;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A class Lake representa os lagos presentes no mundo, cada lago é generado aleatoriamente seja em posição e formato.
 */
public class Lake extends Obstacle{

    private int size;
    public Lake(World world, int size){
        super(world);
        this.size = size;
    }

    /**
     * Verifica através das coordenadas fornecidas se a célula é do tipo GRASS
     * @param x
     * @param y
     * @return retorna um valor booleano
     */
    public boolean verifyCoords(int x, int y){
        Cell c = super.getWorld().getGrid().get(y).get(x);
        return c.getType() == CellType.GRASS
                && c.getCurrentOcupant() == LayerType.NONE
                && c.getHabitat() == HabitatType.NONE;
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
     * Algoritmo que vai gerar um lago com um formato aleatorio, escolhe de forma aleatoria uma célula vizinha que seja válida e do tipo GRASS até que o determinado tamanho seja atinjido.
     */
    public void genObstacle() {
        int currSize = 0;
        ArrayList<Cell> frontier = new ArrayList<>();
        Coord StartPos = getRandomCoords();
        Cell center = super.getWorld().getGrid().get(StartPos.getY()).get(StartPos.getX());
        center.setCellType(CellType.WATER);
        super.getMap().put(StartPos, center);
        currSize++;

        frontier = Adjacent.getAdjacents(super.getWorld(),center, CellType.GRASS, LayerType.NONE, HabitatType.NONE);

        while (currSize < size && !frontier.isEmpty()){
            int randomIndex = Rand.getRandomNmb(frontier.size() - 1);
            Cell next = frontier.get(randomIndex);

            frontier.remove(randomIndex);

            if(next.getType() == CellType.WATER){
                continue;
            }

            next.setCellType(CellType.WATER);
            super.getMap().put(next.getCoord(), next);
            frontier.addAll(Adjacent.getAdjacents(super.getWorld(), next, CellType.GRASS, LayerType.NONE, HabitatType.NONE));
            currSize++;
        }
    }
}

