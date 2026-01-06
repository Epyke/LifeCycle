import entities.*;
import gui.*;
import obstacles.Lake;
import obstacles.Obstacle;
import obstacles.Rock;
import structures.Habitat;
import structures.HabitatType;
import utils.Adjacent;
import utils.CellUtils;
import utils.Rand;
import world.*;
import world.stat.GlobalStat;
import world.stat.SpecieStat;
import world.stat.StatsManager;
import world.stat.YearStat;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public static void main() throws UnsupportedLookAndFeelException {
    GamePanel gp = new GamePanel();
    StatsPanel sp = new StatsPanel(gp.getWorld(), gp.getScreenHeight());
    gp.setStatsPanel(sp);
    ControlPanel cp = new ControlPanel(gp, gp.getScreenHeight());

    GameWindow newGame = new GameWindow(gp,sp,cp);
    newGame.initGameThread();

     World world1 = new World(30);
     world1.worldGen();
     Time worldTime = new Time(world1);
     worldTime.specieExinction(AnimalType.FOX);
}
