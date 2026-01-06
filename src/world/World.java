package world;

import entities.*;
import obstacles.Lake;
import obstacles.Obstacle;
import obstacles.Rock;
import structures.HabitatType;
import utils.CellUtils;
import utils.Rand;
import world.stat.StatsManager;

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
    private StatsManager statsManager;
    /**
     * Um unico HashMap para simplificar a atualização dos organismos
     */
    private HashMap<Coord, Entity> entities;
    private ArrayList<Obstacle> terrain;

    public World(int size){
        grid = new ArrayList<>();
        this.size = size;
        entities = new HashMap<>();
        statsManager = new StatsManager();
        terrain = new ArrayList<>();
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
            Lake newLake = new Lake(this, rdmSize);
            newLake.genObstacle();
            terrain.add(newLake);
        }
    }

    /**
     * Generação de rochas no mundo, estas são por enquanto apenas obstaculos.
     * @param limit O numero máximo de Rochas no mundo, por padrão aparecem pelo menos uma.
     */
    public void genRandomRocks(int limit){
        int rdmNumb = Rand.getRandomNmb(1,limit);
        for(int i = 0;i < rdmNumb; i++){
           Rock newRock = new Rock(this);
           newRock.genObstacle();
           terrain.add(newRock);
        }
    }

    /**
     * Generação do mundo, incluido obstaculos do mapa.
     */
    public void worldGen(){
        //Se a percentagem de aparicao das especies, nao estiver correta, nem vale a pena criar um mundo
        verifySpawnRates();
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
        genEntities(30, 20, 50);
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
     * Condição que verifica se a célula está vazia para nascer um organismo
     * @param c Coordenadas da célula
     * @return boolean se criou
     */
    private boolean canSpawnAt(Cell c) {
        return c.getCurrentOcupant() == LayerType.NONE
                && c.getHabitat() == HabitatType.NONE
                && c.getType() == CellType.GRASS;
    }

    /**
     * Nascimento de animal após reprodução
     * @param c Coordenadas do recém nascido
     * @param type Tipo de animal
     * @return boolean true se foi criado com sucesso
     */
    public boolean bornEntity(Coord c, AnimalType type) {
        Cell currCell = CellUtils.findCell(this, c);

        if (canSpawnAt(currCell)) {
            Animal baby = new Animal(this, c, type);
            entities.put(c, baby);
            currCell.setCurrentOcupant(LayerType.ANIMAL);
            statsManager.registerSpawn(type.toString(), true);
            return true;
        }
        return false;
    }

    /**
     * Nascimento de planta após reprodução
     * @param c Coordenadas do nova planta
     * @param type Tipo de planta
     * @return boolean true se foi criada com sucesso
     */
    public boolean bornEntity(Coord c, PlantType type) {
        Cell currCell = CellUtils.findCell(this, c);
        if (canSpawnAt(currCell)) {
            Plant sapling = new Plant(this, c, type);
            entities.put(c, sapling);
            currCell.setCurrentOcupant(LayerType.PLANT);
            statsManager.registerSpawn(type.toString(), true);
            return true;
        }
        return false;
    }

    /**
     * Verificar se os percentagems de aparicao de cada especie estao corretas
     */
    private void verifySpawnRates() {
        double animalSum = 0;
        for (AnimalType t : AnimalType.values()) {
            animalSum += t.getSpawnRate();
        }
        if (animalSum != 100.0) {
            throw new RuntimeException("A soma das percentagems de aparicao de cada especies de animal tem que ser 100, a atual: " + animalSum);
        }

        double plantSum = 0;
        for (PlantType t : PlantType.values()) {
            plantSum += t.getSpawnRate();
        }
        if (plantSum != 100.0) {
            throw new RuntimeException("A soma das percentagems de aparicao de cada especies de planta tem que ser 100, a atual: " + plantSum);
        }
    }

    /**
     * Criação de um organismo
     * @param emptyCells Lista de células vazias do tipo GRASS
     * @param type Tipo de organismo, planta ou animal
     */
    public void createEntityRandom(ArrayList<Cell> emptyCells, LayerType type) {
        if(type == LayerType.NONE){
            throw new IllegalArgumentException("O tipo de organismo não pode ser nulo");
        }

        Cell selectedCell = Rand.getRandomItem(emptyCells);
        selectedCell.setCurrentOcupant(type);
        if(type == LayerType.PLANT){
            Plant newPlant = new Plant(this, selectedCell.getCoord(), PlantType.getRandomWeighted());
            entities.put(newPlant.getCoords(), newPlant);
            statsManager.registerSpawn(newPlant.getType().toString(), false);
        } else {
            Animal newAnimal = new Animal(this, selectedCell.getCoord(), AnimalType.getRandomWeighted());
            entities.put(newAnimal.getCoords(), newAnimal);
            statsManager.registerSpawn(newAnimal.getType().toString(), false);
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
                createEntityRandom(emptyCells, LayerType.PLANT);
            }
        }

        if(animalsRate > 0){
            int occupiedCells = (int) ((animalsRate/100.0) * availableCells);
            for(int i = 0; i < occupiedCells; i++){
                createEntityRandom(emptyCells, LayerType.ANIMAL);
            }
        }
    }

    /**
     * Remover uma entidade do mundo
     * @param c coordenadas do organismo que vai ser removido
     */
    public void removeEntity(Coord c) {
        if (entities.containsKey(c)) {
            entities.remove(c); // Remove from HashMap
            CellUtils.findCell(this, c).setCurrentOcupant(LayerType.NONE);
        }
    }

    /**
     * Mover um organismo no mundo
     * @param oldPos Coordenadas antigas do organismo
     * @param newPos Coordenadas novas do organismo
     */
    public void moveEntity(Coord oldPos, Coord newPos) {
        if(!entities.containsKey(oldPos)){
            return;
        }

        Entity e = entities.get(oldPos);

        entities.remove(oldPos);

        e.setCoords(newPos);

        entities.put(newPos, e);

        CellUtils.findCell(this, oldPos).setCurrentOcupant(LayerType.NONE);

        LayerType newLayer;
        if(e instanceof  Plant){
            newLayer = LayerType.PLANT;
        } else {
            newLayer = LayerType.ANIMAL;
        }
        CellUtils.findCell(this, newPos).setCurrentOcupant(newLayer);
    }

    public void cleanCorpses() {
        Iterator<Map.Entry<Coord, Entity>> it = entities.entrySet().iterator();

        while (it.hasNext()) {
            Entity e = it.next().getValue();

            boolean isDeadAnimal = (e instanceof Animal && e.getIsDead());
            boolean isDeadPlant = (e instanceof Plant && e.getIsDead());

            if ((isDeadAnimal && e.getDecomposeTimer() >= 1) ||
                    (isDeadPlant && e.getDecomposeTimer() >= 0)) {

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
        //Criar uma copia das entidades atuais
        //Porque durante uma iteração de um HasMap se um organismo se mover, a sua chave altera, o que faz com o que sistema crasha
        //Assim usamos uma cópia (ArrayList) do HashMaps para ler as coordenadas das entidades e altera-las.
        ArrayList<Entity> currentEntities = new ArrayList<>(entities.values());

        for(Entity e : currentEntities){
            //Verificação se o organismo foi retirado do mundo
            if(!entities.containsValue(e)) {
                continue;
            }

            e.updateAge();
            if(e instanceof Animal){
                ((Animal) e).move();
            }
            e.updateStats();
            e.reproduction();
        }
        statsManager.incrementYear();
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
                        if(((Plant) entity).getIsDead()){
                            symbol = 'X';
                        } else {
                            symbol = ((Plant) entity).getType().getSymb();
                        }
                    } else if (entity instanceof Animal) {
                        if(((Animal) entity).getIsDead()){
                            symbol = 'x';
                        } else {
                            symbol = ((Animal) entity).getType().getSymb();
                        }
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

    public ArrayList<Obstacle> getObstacles(){
        return terrain;
    }

    public ArrayList<ArrayList<Cell>> getGrid(){
        return grid;
    }

    public int getSize(){
        return size;
    }

    public HashMap<Coord, Entity> getEntities(){
        return entities;
    }

    public StatsManager getStats() {
        return statsManager;
    }
}
