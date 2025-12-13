package world;

import entities.*;
import obstacles.Lake;
import obstacles.Rock;
import utils.CellUtils;
import utils.Rand;

import java.nio.charset.CoderResult;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *A class world representa o mundo em que vai ocorrer a simulação
 */
public class World {
    private ArrayList<ArrayList<Cell>> grid;
    private final int size;

    /**
     * Um unico HashMap para simplificar a atualização dos organismos
     */
    private HashMap<Coord, Entity> entities;

    public World(int size){
        grid = new ArrayList<>();
        this.size = size;
        entities = new HashMap<>();
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
        genEntities(20, 30, 50);
    }

    /**
     * Criação de uma lista de células, usada na geração de entidades no mundo
     * @return Arraylist de células vazias do tipo GRASS
     */
    public ArrayList<Cell> getEmptyCells(){
        ArrayList<Cell> res = new ArrayList<>();
        for(ArrayList<Cell> rows: grid){
            for(Cell c: rows){
                if(c.getType() == CellType.GRASS){
                    res.add(c);
                }
            }
        }
        return res;
    }

    /**
     * Criação de um organismo
     * @param emptyCells Lista de células vazias do tipo GRASS
     * @param type Tipo de organismo, planta ou animal
     */
    public void createEntity(ArrayList<Cell> emptyCells, LayerType type) {
        if(type == LayerType.NONE){
            throw new IllegalArgumentException("O tipo de organismo não pode ser nulo");
        }

        Cell selectedCell = Rand.getRandomItem(emptyCells);
        selectedCell.setCurrentOcupant(type);
        if(type == LayerType.PLANT){
            Plant newPlant = new Plant(this, selectedCell.getCoord(), Rand.getRandomEnum(PlantType.class));
            entities.put(newPlant.getCoords(), newPlant);
        } else {
            Animal newAnimal = new Animal(this, selectedCell.getCoord(), Rand.getRandomEnum(AnimalType.class));
            entities.put(newAnimal.getCoords(), newAnimal);
        }
        emptyCells.remove(selectedCell);
    }

    /**
     * Geração de organismos no mundo segundo valores passados por paramêtro.
     * @param plantsRate Taxa de plantas em pourcentagem que vão ser inicializadas após a criação do mundo
     * @param animalsRate Taxa de animais em pourcentagem que vão ser inicializados após a criação do mundo
     * @param emptyRate Taxa de células vazias em pourcentagem que vão ser inicializadas após a criação do mundo (células GRASS)
     */
    public void genEntities(double plantsRate, double animalsRate, double emptyRate){
        if(plantsRate < 0 || plantsRate > 100 || animalsRate < 0 || animalsRate > 100 || emptyRate < 0 || emptyRate > 100){
            throw new IllegalArgumentException("O valor de cada argumento têm de estar entre 0 e 100");
        }

        if(plantsRate + animalsRate + emptyRate != 100){
            throw new IllegalArgumentException("A soma das percentagens de inicialização têm que ser igual a 100");
        }

        ArrayList<Cell> emptyCells = getEmptyCells();
        int availableCells = emptyCells.size();

        if(plantsRate > 0){
            int occupiedCells = (int) ((plantsRate/100.0) * availableCells);
            for(int i = 0; i < occupiedCells; i++){
                createEntity(emptyCells, LayerType.PLANT);
            }
        }

        if(animalsRate > 0){
            int occupiedCells = (int) ((animalsRate/100.0) * availableCells);
            for(int i = 0; i < occupiedCells; i++){
                createEntity(emptyCells, LayerType.ANIMAL);
            }
        }
    }

    public void cleanCorpses() {
        Iterator<Map.Entry<Coord, Entity>> it = entities.entrySet().iterator();

        while (it.hasNext()) {
            Entity e = it.next().getValue();

            boolean isRottenAnimal = (e instanceof Animal && e.getDecomposeTimer() >= 1);
            boolean isRottenPlant = (e instanceof Plant && e.getDecomposeTimer() >= 2);

            if (isRottenAnimal || isRottenPlant) {
                CellUtils.findCell(this, e.getCoords()).setCurrentOcupant(LayerType.NONE);
                it.remove();
            }
        }
    }

    /**
     * Atualização do mundo, ou seja como se decorresse um ano, atualiza-se o estado de cada organismo presente no mundo.
     * Esta atualização tem de respeitar uma ordem específica
     * 1. Incrementação de idade
     * 2. Movimentos no caso de animais
     * 3. Atualização da fome, energia, sede...
     * 4. Reprodução
     */
    public void updateEntities(){
        cleanCorpses();
        for(Entity e : entities.values()){
            e.updateAge();
            if(e instanceof Animal){
                ((Animal) e).move();
            }
            e.updateStats();
            e.reproduction();
        }
    }

    /**
     * Visualização do mundo na linha de comandos
     * @return String A sequencia de caracteres que representa o mundo.
     */
    public String worldView(){
        String res = "";
        for(ArrayList<Cell> rows: grid){
            for(Cell c: rows){
                char symbol = ' ';
                if (c.getCurrentOcupant() != LayerType.NONE && entities.containsKey(c.getCoord())) {
                    Object entity = entities.get(c.getCoord());

                    if (entity instanceof Plant) {
                        symbol = ((Plant) entity).getType().getSymb();
                    } else if (entity instanceof Animal) {
                        symbol = ((Animal) entity).getType().getSymb();
                    } else {
                        symbol = '?';
                    }
                }
                else {
                    symbol = c.getType().getSymb();
                }
                res += symbol + "  ";
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
