package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;
    public static void main(String[] args) {
        final var positions = Stream.of(args).collect(Collectors.toMap(
            k -> k.split(";")[0],
            v -> v.split(";")[1]
    ));

        var option = -1;
        while(true){
            System.out.println("Selecione uma opção:");
            System.out.println("1 - Iniciar novo jogo");
            System.out.println("2 - Colocar novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Mostrar tabuleiro");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar tabuleiro");
            System.out.println("7 - Finalizar jogo atual");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame (positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 ->showGameStatus();
                case 6 -> clearBoard();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida. Tente novamente.");

            }
        }

    }

    private static void clearBoard() {
        if (isNull(board)){
            System.out.println("Nenhum jogo em andamento. Inicie um novo jogo primeiro.");
            return;
        }
        System.out.println("Tem certeza que deseja limpar o board e perder todo o progresso? (S/N)");
        var confirmation = scanner.next();
        while (!confirmation.equalsIgnoreCase("S") && !confirmation.equalsIgnoreCase("N")){
            System.out.println("Opção inválida. Digite S para sim ou N para não.");
            confirmation = scanner.next();
        }
        if(confirmation.equalsIgnoreCase("S")) {
            board.reset();
        }
    }

    private static void showGameStatus() {
        if (isNull(board)){
            System.out.println("Nenhum jogo em andamento. Inicie um novo jogo primeiro.");
            return;
        }
        System.out.printf("O seu jogo está no status %s\n", board.getStatus().getLabel());
        if(board.hasErrors()){
            System.out.println("O jogo contém erros");
        }else{
            System.out.println("O jogo não contém erros");
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)){
            System.out.println("Nenhum jogo em andamento. Inicie um novo jogo primeiro.");
            return;
        }
        var args = new Object[81];
        var argPos = 0;
        var colSize = BOARD_LIMIT;
        for (int i = 0; i < colSize; i++) {
            for(var col:board.getSpaces()){
                args[argPos++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }

        }
        System.out.println("Seu jogo se encontra da seguinte forma:");
        System.out.printf((BOARD_TEMPLATE) + "\n", args);
    }

    private static void removeNumber() {
        if (isNull(board)){
            System.out.println("Nenhum jogo em andamento. Inicie um novo jogo primeiro.");
            return;
        }
        System.out.println("Informe a coluna do número a ser removido:");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha do número a ser removido:");
        var row = runUntilGetValidNumber(0, 8);
        if(!board.clearValue(col, row)){
            System.out.printf("A posição [%s, %s] é fixa e não pode ser alterada.\n", col, row);
        }

    }

    private static void finishGame() {
        if (isNull(board)){
            System.out.println("Nenhum jogo em andamento. Inicie um novo jogo primeiro.");
            return;
        }
        if (board.isGameFinished()){
            System.out.println("Parabéns! Você completou o jogo com sucesso!");
            showCurrentGame();
            board =null;
        } else if (board.hasErrors()) {
            System.out.println("O jogo contém erros. Corrija-os antes de finalizar.");
        } else {
            System.out.println("O jogo ainda não está completo. Continue jogando!");
            
        }


    }

    private static void inputNumber() {
        if (isNull(board)){
            System.out.println("Nenhum jogo em andamento. Inicie um novo jogo primeiro.");
            return;
        }
        //TODO: validar se o usuário digitou um número
        System.out.println("Informe a coluna em que o número será inserido:");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha em que o número será inserido:");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número a ser inserido em [%s, %s]:\n", col, row);
        var value = runUntilGetValidNumber(1, 9);
        if(!board.changeValue(col, row, value)){
            System.out.printf("A posição [%s, %s] é fixa e não pode ser alterada.\n", col, row);
        }
    }

    private static void startGame(Map<String, String> positions) {
        if (nonNull(board)){
            System.out.println("Já existe um jogo em andamento. Finalize o jogo atual antes de iniciar um novo.");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = positions.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);

            }
        }
        board = new Board(spaces);
        System.out.println("Novo jogo iniciado!");
    }
    private static int runUntilGetValidNumber(final int min, final int max){
        var current = scanner.nextInt();
        while (current < min || current > max){
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }
}