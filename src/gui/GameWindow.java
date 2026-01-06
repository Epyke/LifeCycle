package gui;

import world.World;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.BorderLayout;

public class GameWindow  extends JFrame{

    private GamePanel gamePanel;
    private StatsPanel statsPanel;
    private ControlPanel controlPanel;

    public GameWindow(GamePanel gamePanel, StatsPanel statsPanel, ControlPanel controlPanel) throws UnsupportedLookAndFeelException {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("LifeCycle Simulation");
        this.setLayout(new BorderLayout());

        this.gamePanel = gamePanel;
        this.statsPanel = statsPanel;
        this.controlPanel = controlPanel;

        this.add(controlPanel, BorderLayout.WEST);
        this.add(gamePanel, BorderLayout.CENTER);
        this.add(statsPanel, BorderLayout.EAST);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void initGameThread(){
        gamePanel.startGameThread();
    }
}
