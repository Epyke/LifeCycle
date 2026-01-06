import entities.AnimalType;
import gui.*;
import obstacles.Rock;
import world.Time;
import world.World;

import javax.swing.*;
import java.awt.*;

public static void main() throws UnsupportedLookAndFeelException {
    GamePanel gp = new GamePanel();
    StatsPanel sp = new StatsPanel(gp.getWorld(), gp.getScreenHeight());
    gp.setStatsPanel(sp);
    ControlPanel cp = new ControlPanel(gp, gp.getScreenHeight());

    GameWindow newGame = new GameWindow(gp,sp,cp);
    newGame.initGameThread();

     /**World world1 = new World(30);
     world1.worldGen();
     Time worldTime = new Time(world1);
     worldTime.specieExinction(AnimalType.FOX);*/
}
