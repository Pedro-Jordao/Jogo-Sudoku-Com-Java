package br.com.dio.ui.custom.screen;

import br.com.dio.service.BoardService;
import br.com.dio.ui.custom.button.CheckGameStatusButton;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.panel.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MainScreen {
    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;

    public MainScreen (final Map<String, String> gameConfig){
        this.boardService = new BoardService(gameConfig);

    }
    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension , mainPanel);
        for (int i = 0; i < 9; i++) {
            
        }
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();

    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
           if (boardService.IsGameFinished()){
                JOptionPane.showMessageDialog(null, "Parabéns! Você completou o Sudoku com sucesso!");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
           } else {
               var message = "O jogo ainda não foi concluído. Continue tentando!";
               JOptionPane.showMessageDialog(null, message );
           }
        });
        mainPanel.add(MainScreen.this.finishGameButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
         checkGameStatusButton = new FinishGameButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus){
                case NON_STARTED -> "O jogo não foi iniciado.";
                case INCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo foi concluído com sucesso!";
            };
            message += hasErrors ? "\nExistem erros no tabuleiro." : "\nNão existem erros no tabuleiro.";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(MainScreen.this.checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
         resetButton = new ResetButton(e ->{
            var dialogResult = JOptionPane.showConfirmDialog(null, "Deseja reiniciar o jogo?", "Limpar jogo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
            );
        if (dialogResult == 0) {
            boardService.reset();
        }
        });
        mainPanel.add(resetButton);
    }
}
