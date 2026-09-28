package ordenacao;

import java.util.Comparator;

/** Ordena o próprio vetor; o comparador define qual elemento vem antes. */
public final class SelectionSort {
    private SelectionSort() { }

    /** Versão para Integer, String e outros tipos que implementam Comparable. */
    public static <T extends Comparable<? super T>> void ordenar(T[] vetor) {
        ordenar(vetor, Comparator.naturalOrder());
    }

    /** O(n²) em todos os casos; O(1) extra. Não é estável. */
    public static <T> void ordenar(T[] vetor, Comparator<? super T> comparador) {
        for (int i = 0; i < vetor.length - 1; i++) {
            // [0, i) já está pronto. Procuramos o menor no trecho restante.
            int menor = i;
            for (int j = i + 1; j < vetor.length; j++) {
                if (comparador.compare(vetor[j], vetor[menor]) < 0) menor = j;
            }
            // Uma troca por rodada. Ela pode inverter elementos de chaves iguais.
            if (menor != i) {
                T temporario = vetor[i];
                vetor[i] = vetor[menor];
                vetor[menor] = temporario;
            }
        }
    }
}
