package ordenacao;

import java.util.Comparator;

/** Ordena o próprio vetor; o comparador define qual elemento vem antes. */
public final class QuickSort {
    private QuickSort() { }

    /** Versão para Integer, String e outros tipos que implementam Comparable. */
    public static <T extends Comparable<? super T>> void ordenar(T[] vetor) {
        ordenar(vetor, Comparator.naturalOrder());
    }

    /** Melhor/médio O(n log n), pior O(n²). Não é estável; pilha O(log n). */
    public static <T> void ordenar(T[] vetor, Comparator<? super T> comparador) {
        ordenar(vetor, 0, vetor.length - 1, comparador);
    }

    private static <T> void ordenar(T[] v, int inicio, int fim, Comparator<? super T> c) {
        while (inicio < fim) {
            int i = inicio;
            int j = fim;
            // Pivô central é simples, mas não elimina o pior caso quadrático.
            T pivo = v[inicio + (fim - inicio) / 2];
            while (i <= j) {
                while (i <= fim && c.compare(v[i], pivo) < 0) i++;
                while (j >= inicio && c.compare(v[j], pivo) > 0) j--;
                if (i <= j) {
                    T temporario = v[i];
                    v[i] = v[j];
                    v[j] = temporario;
                    i++;
                    j--; // Avançar nos empates evita travar com valores repetidos.
                }
            }
            // As partições restantes são [inicio, j] e [i, fim].
            // Recursão só na menor; a maior continua no laço. Assim a pilha
            // fica O(log n) mesmo quando o tempo chega ao pior caso O(n²).
            if (j - inicio < fim - i) {
                if (inicio < j) ordenar(v, inicio, j, c);
                inicio = i;
            } else {
                if (i < fim) ordenar(v, i, fim, c);
                fim = j;
            }
        }
    }
}
