package gui;

import entities.AnimalType;
import entities.PlantType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class ControlPanel extends JPanel {

    private GamePanel gp;
    private final int width = 200;

    JButton btnStartStop;
    JTextField txtYears;
    JButton btnRunYears;
    JComboBox<String> comboSpecies;
    JButton btnRunExtinction;
    JSlider sliderSpeed;
    JButton btnReset;

    public ControlPanel(GamePanel gp, int height) throws UnsupportedLookAndFeelException {
        this.gp = gp;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(40, 40, 40));

        this.setLayout(new GridLayout(10, 1, 10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();

        UIManager.setLookAndFeel(new NimbusLookAndFeel());
    }

    private void initUI() {
        TitledBorder border;

        btnStartStop = new JButton("INICIAR");
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

        sliderSpeed = new JSlider(1, 20, 4);
        sliderSpeed.setBackground(getBackground());
        sliderSpeed.setForeground(Color.WHITE);
        sliderSpeed.setPaintTicks(true);
        sliderSpeed.setMajorTickSpacing(5);
        sliderSpeed.setMinorTickSpacing(1);

        sliderSpeed.addChangeListener(e -> {
            gp.setFPS(sliderSpeed.getValue());
        });

        pnlSpeed.add(sliderSpeed);
        this.add(pnlSpeed);

        this.add(new JLabel(" "));

        JPanel pnlYears = new JPanel(new GridLayout(2, 1));
        pnlYears.setBackground(getBackground());
        pnlYears.setBorder(BorderFactory.createTitledBorder(null, "Correr N Anos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.WHITE));

        txtYears = new JTextField("50");
        btnRunYears = new JButton("INICIAR");
        btnRunYears.addActionListener(e -> {
            try {
                int y = Integer.parseInt(txtYears.getText());
                gp.startYears(y);
                btnStartStop.setText("CONTINUAR (ANOS)");
                btnStartStop.setBackground(Color.ORANGE);
            } catch (NumberFormatException ex) {
                txtYears.setText("Erro");
            }
        });

        pnlYears.add(txtYears);
        pnlYears.add(btnRunYears);
        this.add(pnlYears);

        this.add(new JLabel(" "));

        JPanel pnlExtinction = new JPanel(new GridLayout(2, 1));
        pnlExtinction.setBackground(getBackground());
        pnlExtinction.setBorder(BorderFactory.createTitledBorder(null, "Até Extinção", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, Color.WHITE));

        comboSpecies = new JComboBox<>();

        for (AnimalType t : AnimalType.values()) comboSpecies.addItem(t.toString());
        for (PlantType t : PlantType.values()) comboSpecies.addItem(t.toString());

        btnRunExtinction = new JButton("Correr até Extinção");
        btnRunExtinction.addActionListener(e -> {
            String species = (String) comboSpecies.getSelectedItem();
            gp.startUntilExtinction(species);
            btnStartStop.setText("CONTINUAR (Extinção)");
            btnStartStop.setBackground(Color.ORANGE);
        });

        pnlExtinction.add(comboSpecies);
        pnlExtinction.add(btnRunExtinction);
        this.add(pnlExtinction);

        this.add(new JLabel(" "));

        btnReset = new JButton("REINICIAR");
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