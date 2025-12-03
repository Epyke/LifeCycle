package world;

import entities.Animals;
import entities.Entity;
import entities.Plants;
import obstacles.Lake;
import obstacles.Rock;
import utils.Rand;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *A class world representa o mundo em que vai ocorrer a simulação
 */
public class World {
    private ArrayList<ArrayList<Cell>> grid;
    private final int size;

    /**
     * Dois HashMaps separados para animais e plantas, porque como os animais se movem, isto implica estar sempre a procurar no msm Hashmap
     * e por isso ao separmos em dois, isto alevia a carga de procura do hashmap animais, já que não existe plantas.
     */
    private HashMap<Coord, Animals> animals;
    private HashMap<Coord, Plants> plants;


    public World(int size){
        grid = new ArrayList<>();
        this.size = size;
        animals = new HashMap<>();
        plants = new HashMap<>();
        if (!(size > 0)){
            throw new IllegalArgumentException("Size needs to be > 0");
        }
    }

    /**
     *Generação de lagos no mundo, para os animais poderem beber.
     * @param limit A quantidade maxima de lagos que vão aparecer no mapa, aparece por padrão pelo menos um;
     * @param sizeMin O tamanho minimo de cada lago
     * @param sizeMax O tamanho máxim de cada lago
     * O tamanho é gerado aleatoriamente, porém dentro dos tamanhos minimos e máximos fornecidos.
     */
    public void genRandomLakes(int limit, int sizeMin, int sizeMax){
        int rdmNumb = Rand.getRandomNmb(1,limit);
        for(int i = 0; i < rdmNumb; i++){
            int rdmSize = Rand.getRandomNmb(sizeMin, sizeMax);
            new Lake(this, rdmSize).genObstacle();
        }
    }

    /**
     * Generação de rochas no mundo, estas são por enquanto apenas obstaculos.
     * @param limit O numero máximo de Rochas no mundo, por padrão aparecem pelo menos uma.
     */
    public void genRandomRocks(int limit){
        int rdmNumb = Rand.getRandomNmb(1,limit);
        for(int i = 0;i < rdmNumb; i++){
            new Rock(this).genObstacle();
        }
    }

    /**
     * Generação do mundo, incluido obstaculos do mapa.
     */
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
        genRandomLakes(3, 10, 15);
    }

    /**
     * Visualização do mundo na linha de comandos
     * @return String A sequencia de caracteres que representa o mundo.
     */
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
