package world;

import structures.HabitatType;

/**
 * A class Cell representa cada cellula do mundo, cada uma tem uma coordenada e um tipo de célula.
 */
public class Cell {
    private Coord coords;
    private CellType type;
    private LayerType currentOcupant;
    private HabitatType habitat;

    public Cell(Coord coords, CellType type){
        this.coords = coords;
        this.type = type;
    }

    public LayerType getCurrentOcupant() {
        return currentOcupant;
    }

    public Coord getCoord(){
        return coords;
    }

    public HabitatType getHabitat() {
        return habitat;
    }

    public CellType getType(){
        return type;
    }

    public void setCellType(CellType type){
        this.type = type;
    }

    public void setCurrentOcupant(LayerType type){
        currentOcupant = type;
    }
}
