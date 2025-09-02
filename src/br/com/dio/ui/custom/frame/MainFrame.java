package br.com.dio.ui.custom.frame;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {
    public MainFrame(final Dimension dimension, final JPanel mainPanel){
        super ("Sudoku com Java Swing");
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        setResizable(false);
        this.add(mainPanel);
    }
}
