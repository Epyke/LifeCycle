import entities.AnimalType;
import gui.*;
import obstacles.Rock;
import world.Time;
import world.World;

import javax.swing.*;
import java.awt.*;

public static void main() throws UnsupportedLookAndFeelException {
    GamePanel gp = new GamePanel();
    GameWindow newGame = new GameWindow(gp,new StatsPanel(gp.getWorld(), gp.getScreenHeight()), new ControlPanel(gp, gp.getScreenHeight()));
    newGame.initGameThread();

     /**World world1 = new World(30);
     world1.worldGen();
     Time worldTime = new Time(world1);
     worldTime.specieExinction(AnimalType.FOX);*/
}
