package ordenacao;

import java.util.Comparator;

/** Ordena o próprio vetor; o comparador define qual elemento vem antes. */
public final class InsertionSort {
    private InsertionSort() { }

    /** Versão para Integer, String e outros tipos que implementam Comparable. */
    public static <T extends Comparable<? super T>> void ordenar(T[] vetor) {
        ordenar(vetor, Comparator.naturalOrder());
    }

    /** Melhor O(n), médio/pior O(n²); O(1) extra. Estável. */
    public static <T> void ordenar(T[] vetor, Comparator<? super T> comparador) {
        for (int i = 1; i < vetor.length; i++) {
            // [0, i) está ordenado. Abrimos espaço para inserir a chave.
            T chave = vetor[i];
            int j = i - 1;
            while (j >= 0 && comparador.compare(vetor[j], chave) > 0) {
                vetor[j + 1] = vetor[j]; // Deslocamento, sem trocar a cada passo.
                j--;
            }
            vetor[j + 1] = chave;
            // Não deslocar iguais preserva sua ordem original (estabilidade).
        }
    }
}
