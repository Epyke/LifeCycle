import entities.AnimalType;
import gui.ControlPanel;
import gui.StatsPanel;
import obstacles.Rock;
import world.Time;
import world.World;

import gui.GamePanel;

import javax.swing.*;
import java.awt.*;

public static void main() throws UnsupportedLookAndFeelException {
    JFrame window = new JFrame();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle("LifeCycle Simulation");

    window.setLayout(new BorderLayout());

    GamePanel gamePanel = new GamePanel();

    // Stats Panel (Direita)
    StatsPanel statsPanel = new StatsPanel(gamePanel.world, gamePanel.screenHeight);
    gamePanel.statsPanel = statsPanel;

    // Control Panel (Esquerda) - NOVO
    ControlPanel controlPanel = new ControlPanel(gamePanel, gamePanel.screenHeight);

    // Montar Layout
    window.add(controlPanel, BorderLayout.WEST);
    window.add(gamePanel, BorderLayout.CENTER);
    window.add(statsPanel, BorderLayout.EAST);

    window.pack();
    window.setLocationRelativeTo(null);
    window.setVisible(true);

    gamePanel.startGameThread();

  /**World world1 = new World(30);
  world1.worldGen();sd
  Time worldTime = new Time(world1);
  worldTime.specieExinction(AnimalType.WOLF);*/
}
