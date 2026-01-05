import entities.AnimalType;
import gui.StatsPanel;
import obstacles.Rock;
import world.Time;
import world.World;

import gui.GamePanel;
import javax.swing.JFrame;
import java.awt.*;

public static void main(){
    JFrame window = new JFrame();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle("LifeCycle Simulation");

    // Usa BorderLayout para permitir painéis laterais
    window.setLayout(new BorderLayout());

    GamePanel gamePanel = new GamePanel();

    // Cria o StatsPanel com a mesma altura do jogo
    StatsPanel statsPanel = new StatsPanel(gamePanel.world, gamePanel.screenHeight);

    // Liga os painéis (para o GamePanel poder atualizar o StatsPanel)
    gamePanel.statsPanel = statsPanel;

    // Adiciona ao ecrã
    window.add(gamePanel, BorderLayout.CENTER); // Jogo no meio
    window.add(statsPanel, BorderLayout.EAST);  // Stats à direita

    window.pack(); // Ajusta o tamanho da janela para caber tudo
    window.setLocationRelativeTo(null);
    window.setVisible(true);

    gamePanel.startGameThread();

  /**World world1 = new World(30);
  world1.worldGen();
  Time worldTime = new Time(world1);
  worldTime.specieExinction(AnimalType.WOLF);*/
}
