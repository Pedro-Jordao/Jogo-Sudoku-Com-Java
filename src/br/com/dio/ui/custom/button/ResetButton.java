package br.com.dio.ui.custom.button;

public class ResetButton extends javax.swing.JButton {

    public ResetButton(final java.awt.event.ActionListener actionListener) {
        this.setText("Reiniciar jogo");
        this.addActionListener(actionListener);
    }
}
