package world;

/**
 * A class Cell representa cada cellula do mundo, cada uma tem uma coordenada e um tipo de célula.
 */
public class Cell {
    private Coord coords;
    private CellType type;

    public Cell(Coord coords, CellType type){
        this.coords = coords;
        this.type = type;
    }

    public Coord getCoord(){
        return coords;
    }

    public CellType getType(){
        return type;
    }

    public void setCellType(CellType type){
        this.type = type;
    }
}
