package gui;

import entities.AnimalType;
import entities.PlantType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ControlPanel extends JPanel {

    GamePanel gp;
    public final int width = 200; // Largura do painel de controlo

    // Componentes
    JButton btnStartStop;
    JTextField txtYears;
    JButton btnRunYears;
    JComboBox<String> comboSpecies;
    JButton btnRunExtinction;
    JSlider sliderSpeed;
    JButton btnReset;

    public ControlPanel(GamePanel gp, int height) {
        this.gp = gp;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(40, 40, 40));

        // Layout: Uma coluna com vários botões
        this.setLayout(new GridLayout(10, 1, 10, 10)); // 10 linhas, espaçamento de 10px
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
    }

    private void initUI() {
        // Estilo dos Títulos
        TitledBorder border;

        // --- 1. CONTINUOUS ---
        btnStartStop = new JButton("SIMULAÇÃO CONTINUA");
        btnStartStop.setBackground(Color.GREEN);
        btnStartStop.addActionListener(e -> {
            if (gp.isRunning()) {
                gp.pauseSimulation();
                btnStartStop.setText("RETOMAR");
                btnStartStop.setBackground(Color.GREEN);
            } else {
                gp.startContinuous();
                btnStartStop.setText("INTERROMPER");
                btnStartStop.setBackground(Color.RED);
            }
        });
        this.add(btnStartStop);

        this.add(new JLabel(" "));

        JPanel pnlSpeed = new JPanel(new GridLayout(2, 1));
        pnlSpeed.setBackground(getBackground());
        pnlSpeed.setBorder(BorderFactory.createTitledBorder(null, "Velocidade", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.WHITE));

        // Slider de 1 a 60 FPS, começa em 10
        sliderSpeed = new JSlider(1, 20, 4);
        sliderSpeed.setBackground(getBackground());
        sliderSpeed.setForeground(Color.WHITE);
        sliderSpeed.setPaintTicks(true);
        sliderSpeed.setMajorTickSpacing(5);
        sliderSpeed.setMinorTickSpacing(1);

        // Listener: Quando moves o slider, muda o FPS no GamePanel
        sliderSpeed.addChangeListener(e -> {
            gp.setFPS(sliderSpeed.getValue());
        });

        pnlSpeed.add(sliderSpeed);
        this.add(pnlSpeed);

        // Separador
        this.add(new JLabel(" "));

        // --- 2. TIME LIMIT (Anos) ---
        JPanel pnlYears = new JPanel(new GridLayout(2, 1));
        pnlYears.setBackground(getBackground());
        pnlYears.setBorder(BorderFactory.createTitledBorder(null, "Correr N Anos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.WHITE));

        txtYears = new JTextField("50");
        btnRunYears = new JButton("INICIAR");
        btnRunYears.addActionListener(e -> {
            try {
                int y = Integer.parseInt(txtYears.getText());
                gp.startYears(y);
                btnStartStop.setText("SIMULAÇÃO CONTINUA (ANOS)");
                btnStartStop.setBackground(Color.ORANGE);
            } catch (NumberFormatException ex) {
                txtYears.setText("Erro");
            }
        });

        pnlYears.add(txtYears);
        pnlYears.add(btnRunYears);
        this.add(pnlYears);

        // Separador
        this.add(new JLabel(" "));

        // --- 3. EXTINCTION ---
        JPanel pnlExtinction = new JPanel(new GridLayout(2, 1));
        pnlExtinction.setBackground(getBackground());
        pnlExtinction.setBorder(BorderFactory.createTitledBorder(null, "Até Extinção", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.WHITE));

        comboSpecies = new JComboBox<>();
        // Adicionar Animais ao dropdown
        for (AnimalType t : AnimalType.values()) comboSpecies.addItem(t.toString());
        // Adicionar Plantas ao dropdown
        for (PlantType t : PlantType.values()) comboSpecies.addItem(t.toString());

        btnRunExtinction = new JButton("Correr até Extinção");
        btnRunExtinction.addActionListener(e -> {
            String species = (String) comboSpecies.getSelectedItem();
            gp.startUntilExtinction(species);
            btnStartStop.setText("SIMULAÇÃO CONTINUA (Extinção)");
            btnStartStop.setBackground(Color.ORANGE);
        });

        pnlExtinction.add(comboSpecies);
        pnlExtinction.add(btnRunExtinction);
        this.add(pnlExtinction);

        this.add(new JLabel(" "));

        // --- 5. RESET BUTTON (NOVO) ---
        btnReset = new JButton("REINICIAR SIMULAÇÂO");
        btnReset.setBackground(Color.CYAN);
        btnReset.setForeground(Color.BLACK);
        btnReset.setFont(btnReset.getFont().deriveFont(java.awt.Font.BOLD));
        btnReset.addActionListener(e -> {
            gp.restartGame();
            btnStartStop.setText("INICIAR");
            btnStartStop.setBackground(Color.GREEN);
        });
        this.add(btnReset);
    }
}