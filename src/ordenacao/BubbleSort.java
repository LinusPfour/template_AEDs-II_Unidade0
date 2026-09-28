package ordenacao;

import java.util.Comparator;

/** Ordena o próprio vetor; o comparador define qual elemento vem antes. */
public final class BubbleSort {
    private BubbleSort() { }

    /** Versão para Integer, String e outros tipos que implementam Comparable. */
    public static <T extends Comparable<? super T>> void ordenar(T[] vetor) {
        ordenar(vetor, Comparator.naturalOrder());
    }

    /** Melhor O(n), médio/pior O(n²); O(1) extra. Estável. */
    public static <T> void ordenar(T[] vetor, Comparator<? super T> comparador) {
        for (int fim = vetor.length - 1; fim > 0; fim--) {
            boolean trocou = false;
            // O maior deste trecho sobe até fim por trocas entre vizinhos.
            for (int j = 0; j < fim; j++) {
                if (comparador.compare(vetor[j], vetor[j + 1]) > 0) {
                    T temporario = vetor[j];
                    vetor[j] = vetor[j + 1];
                    vetor[j + 1] = temporario;
                    trocou = true;
                }
            }
            // Nenhuma inversão encontrada: o vetor já está ordenado.
            if (!trocou) return;
        }
    }
}
