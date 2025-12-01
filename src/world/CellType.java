package world;

/**
 * A class CellType representa o tipo de cada célula presente no mundo, cada tipo está associado à um caracter.
 */
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
