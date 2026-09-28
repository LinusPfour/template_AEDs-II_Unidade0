package ordenacao;

import java.util.Comparator;

/** Ordena o próprio vetor; o comparador define qual elemento vem antes. */
public final class HeapSort {
    private HeapSort() { }

    /** Versão para Integer, String e outros tipos que implementam Comparable. */
    public static <T extends Comparable<? super T>> void ordenar(T[] vetor) {
        ordenar(vetor, Comparator.naturalOrder());
    }

    /** O(n log n) médio/pior; O(1) extra. Não é estável. */
    public static <T> void ordenar(T[] vetor, Comparator<? super T> comparador) {
        // Índices a partir de zero: filhos de i são 2*i+1 e 2*i+2.
        // As folhas já são heaps. Arrumamos de baixo para cima em O(n).
        for (int i = vetor.length / 2 - 1; i >= 0; i--)
            descer(vetor, i, vetor.length, comparador);
        for (int fim = vetor.length - 1; fim > 0; fim--) {
            // A raiz é o maior. Vai para a posição final e sai do heap ativo.
            T temporario = vetor[0];
            vetor[0] = vetor[fim];
            vetor[fim] = temporario;
            descer(vetor, 0, fim, comparador);
        }
    }

    /** tamanho é exclusivo: [0, tamanho) ainda pertence ao heap. */
    private static <T> void descer(T[] v, int pai, int tamanho, Comparator<? super T> c) {
        while (pai < tamanho / 2) {
            int filho = 2 * pai + 1;
            if (filho + 1 < tamanho && c.compare(v[filho + 1], v[filho]) > 0) filho++;
            if (c.compare(v[pai], v[filho]) >= 0) return;
            T temporario = v[pai];
            v[pai] = v[filho];
            v[filho] = temporario;
            pai = filho;
        }
    }
}
