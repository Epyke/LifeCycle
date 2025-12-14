package world.stat;

public class YearStat {
    public int born_this_turn = 0;
    public int died_this_turn = 0;
    public int eaten_this_turn = 0;

    public void reset() {
        born_this_turn = 0;
        died_this_turn = 0;
        eaten_this_turn = 0;
    }
}
