package br.com.dio.ui.custom.screen;

import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.EventEnum;
import br.com.dio.service.NotifierService;
import br.com.dio.ui.custom.button.CheckGameStatusButton;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.input.NumberText;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainScreen {
    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;

    public MainScreen (final Map<String, String> gameConfig){
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();

    }
    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension , mainPanel);
        for (int r = 0; r < 9; r+=3) {
            var endRow = r + 2;
            for (int c = 0; c < 9; c+=3) {
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardService.getSpaces(), c, endCol, r, endRow);
                JPanel sector = generateSection(spaces);
                mainPanel.add(sector);
            }
            
        }
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();

    }

    private List<Space> getSpacesFromSector( final List<List<Space>> spaces,
                                             final int initColl, final int endColl,
                                             final int initRow, final int endRow){
        List <Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++) {
            for (int c = initColl; c <= endColl; c++) {
                spaceSector.add(spaces.get(c).get(r));
                
            }
            
        }
        return spaceSector;

    }
    private JPanel generateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscribe(EventEnum.CLEAR_SPACE, t));
        return new SudokuSector(fields);
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
         checkGameStatusButton = new CheckGameStatusButton(e -> {
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
            notifierService.notify(EventEnum.CLEAR_SPACE);
        }
        });
        mainPanel.add(resetButton);
    }
}
