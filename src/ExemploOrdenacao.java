import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import ordenacao.*;

/** Exemplos de consulta: mesmo algoritmo, critérios diferentes. */
public class ExemploOrdenacao {
    public static void main(String[] args) {
        Integer[] numeros = {5, -2, 5, 0, 9, 1};
        QuickSort.ordenar(numeros);
        System.out.println("Crescente: " + Arrays.toString(numeros));
        HeapSort.ordenar(numeros, Comparator.reverseOrder());
        System.out.println("Decrescente: " + Arrays.toString(numeros));

        Produto[] produtos = {
            new ProdutoNaoPerecivel("Caderno", 10, 0.20),
            new ProdutoPerecivel("Iogurte", 4, LocalDate.now().plusDays(5)),
            new ProdutoNaoPerecivel("Caneta azul", 2, 0.20),
            new ProdutoNaoPerecivel("Caneta preta", 2, 0.20)
        };
        Comparator<Produto> porDescricao = Comparator.comparing(
                Produto::getDescricao, String.CASE_INSENSITIVE_ORDER);
        Comparator<Produto> porPreco = Comparator.comparingDouble(Produto::valorDeVenda);
        Comparator<Produto> porPrecoEDescricao = porPreco.thenComparing(porDescricao);
        MergeSort.ordenar(produtos, porPrecoEDescricao);
        System.out.println("\nPreço crescente; empates por descrição:");
        for (Produto p : produtos) System.out.println(p);

        // reversed() antes de thenComparing inverte só o primeiro critério.
        SelectionSort.ordenar(produtos, porPreco.reversed().thenComparing(porDescricao));
        System.out.println("\nPreço decrescente; descrição crescente nos empates:");
        for (Produto p : produtos) System.out.println(p);

        String[] nomes = {"bia", "Ana", "caio"};
        InsertionSort.ordenar(nomes, String.CASE_INSENSITIVE_ORDER);
        BubbleSort.ordenar(nomes); // Ordem natural de String distingue maiúsculas/minúsculas.
    }
}
