package ordenacao;

import java.util.Comparator;

/** Ordena o próprio vetor; o comparador define qual elemento vem antes. */
public final class MergeSort {
    private MergeSort() { }

    /** Versão para Integer, String e outros tipos que implementam Comparable. */
    public static <T extends Comparable<? super T>> void ordenar(T[] vetor) {
        ordenar(vetor, Comparator.naturalOrder());
    }

    /** O(n log n) em todos os casos; O(n) auxiliar e O(log n) de pilha. Estável. */
    public static <T> void ordenar(T[] vetor, Comparator<? super T> comparador) {
        if (vetor.length < 2) return;
        T[] auxiliar = vetor.clone(); // Um único auxiliar reutilizado nas intercalações.
        ordenar(vetor, auxiliar, 0, vetor.length - 1, comparador);
    }

    private static <T> void ordenar(T[] v, T[] aux, int inicio, int fim,
                                    Comparator<? super T> c) {
        if (inicio >= fim) return;
        int meio = inicio + (fim - inicio) / 2;
        ordenar(v, aux, inicio, meio, c);
        ordenar(v, aux, meio + 1, fim, c);
        intercalar(v, aux, inicio, meio, fim, c);
    }

    private static <T> void intercalar(T[] v, T[] aux, int inicio, int meio, int fim,
                                      Comparator<? super T> c) {
        for (int k = inicio; k <= fim; k++) aux[k] = v[k];
        int esquerda = inicio;
        int direita = meio + 1;
        for (int k = inicio; k <= fim; k++) {
            if (esquerda > meio) v[k] = aux[direita++];
            else if (direita > fim) v[k] = aux[esquerda++];
            // Empate: retirar primeiro da esquerda mantém a estabilidade.
            else if (c.compare(aux[esquerda], aux[direita]) <= 0) v[k] = aux[esquerda++];
            else v[k] = aux[direita++];
        }
    }
}
