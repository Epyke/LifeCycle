package world.stat;

public class YearStat {
    private int born_this_turn;
    private int died_this_turn;
    private int eaten_this_turn;

    public YearStat() {
        born_this_turn = 0;
        died_this_turn = 0;
        eaten_this_turn = 0;
    }

    public void increment_born_this_turn() {
        born_this_turn++;
    }

    public void increment_eaten_this_turn() {
        eaten_this_turn++;
    }

    public void increment_died_this_turn(){
        died_this_turn++;
    }

    public int getBorn_this_turn() {
        return born_this_turn;
    }

    public int getDied_this_turn() {
        return died_this_turn;
    }

    public int getEaten_this_turn() {
        return eaten_this_turn;
    }

    public void reset() {
        born_this_turn = 0;
        died_this_turn = 0;
        eaten_this_turn = 0;
    }
}
