package recursividade;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AppRecursividade {
    private static int lerInteiro(Scanner teclado, String mensagem) {
        System.out.print(mensagem);
        return Integer.parseInt(teclado.nextLine().trim());
    }

    private static int lerTamanho(Scanner teclado) {
        int tamanho = lerInteiro(teclado, "Quantidade de elementos: ");
        if (tamanho < 0) throw new IllegalArgumentException("Tamanho negativo.");
        return tamanho;
    }

    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.println("\n1 - Somar pares até um limite\n2 - Somar vetor de doubles"
                        + "\n3 - Contar repetições\n0 - Sair");
                if (!teclado.hasNextLine()) return;
                try {
                    int opcao = lerInteiro(teclado, "Opção: ");
                    switch (opcao) {
                        case 0 -> { return; }
                        case 1 -> System.out.println("Soma: " + Recursividade.somarPares(
                                lerInteiro(teclado, "Limite não negativo: ")));
                        case 2 -> {
                            double[] vetor = new double[lerTamanho(teclado)];
                            for (int i = 0; i < vetor.length; i++) {
                                System.out.print("Elemento [" + i + "]: ");
                                vetor[i] = Double.parseDouble(teclado.nextLine().replace(',', '.'));
                            }
                            System.out.println("Soma: " + Recursividade.somarVetor(vetor));
                        }
                        case 3 -> {
                            int[] vetor = new int[lerTamanho(teclado)];
                            for (int i = 0; i < vetor.length; i++)
                                vetor[i] = lerInteiro(teclado, "Elemento [" + i + "]: ");
                            int numero = lerInteiro(teclado, "Número procurado: ");
                            System.out.println("Repetições: " + Recursividade.contarRepeticoes(vetor, numero));
                        }
                        default -> System.out.println("Opção inválida.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Entrada inválida: " + e.getMessage());
                } catch (java.util.NoSuchElementException e) {
                    return;
                }
            }
        }
    }
}
