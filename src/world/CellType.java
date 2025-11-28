package world;

public enum CellType {
    GRASS('.'),
    ROCK('#'),
    WATER('~');

    private final char symb;

    CellType(char symb){
        this.symb = symb;
    }

    public char getSymb(){
        return symb;
    }
}
