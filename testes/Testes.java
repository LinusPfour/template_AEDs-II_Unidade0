import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import ordenacao.*;
import recursividade.Recursividade;

/** Testes sem bibliotecas externas: execute java -cp bin Testes. */
public class Testes {
    private static int verificacoes;

    private static void verificar(boolean condicao, String mensagem) {
        verificacoes++;
        if (!condicao) throw new AssertionError(mensagem);
    }

    private static void perto(double esperado, double obtido) {
        verificar(Math.abs(esperado - obtido) < 1e-9, "Esperado " + esperado + ", obtido " + obtido);
    }

    private static void falha(Class<? extends Throwable> tipo, Runnable acao) {
        try {
            acao.run();
        } catch (Throwable erro) {
            verificar(tipo.isInstance(erro), "Exceção inesperada: " + erro);
            return;
        }
        throw new AssertionError("Deveria lançar " + tipo.getSimpleName());
    }

    private static <T> void ordenar(int algoritmo, T[] v, Comparator<? super T> c) {
        switch (algoritmo) {
            case 0 -> SelectionSort.ordenar(v, c);
            case 1 -> BubbleSort.ordenar(v, c);
            case 2 -> InsertionSort.ordenar(v, c);
            case 3 -> MergeSort.ordenar(v, c);
            case 4 -> HeapSort.ordenar(v, c);
            case 5 -> QuickSort.ordenar(v, c);
            default -> throw new IllegalArgumentException();
        }
    }

    private record Item(int chave, int posicaoOriginal) { }

    private static void testarOrdenacao() {
        Random aleatorio = new Random(2026);
        for (int algoritmo = 0; algoritmo < 6; algoritmo++) {
            for (int tamanho = 0; tamanho <= 100; tamanho++) {
                Integer[] entrada = new Integer[tamanho];
                for (int i = 0; i < tamanho; i++) entrada[i] = aleatorio.nextInt(21) - 10;
                for (Comparator<Integer> c : Arrays.asList(Comparator.<Integer>naturalOrder(),
                        Comparator.<Integer>reverseOrder())) {
                    Integer[] esperado = entrada.clone();
                    Arrays.sort(esperado, c); // Oráculo independente das implementações.
                    Integer[] resultado = entrada.clone();
                    ordenar(algoritmo, resultado, c);
                    verificar(Arrays.equals(esperado, resultado), "Algoritmo " + algoritmo + ", n=" + tamanho);
                    ordenar(algoritmo, resultado, c); // Entrada já ordenada.
                    verificar(Arrays.equals(esperado, resultado), "Reordenação " + algoritmo);
                }
            }
            Integer[] extremos = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, Integer.MAX_VALUE};
            ordenar(algoritmo, extremos, Integer::compare);
            verificar(Arrays.equals(extremos, new Integer[]{Integer.MIN_VALUE, 0,
                    Integer.MAX_VALUE, Integer.MAX_VALUE}), "Limites inteiros");
            Integer[] iguais = new Integer[1000];
            Arrays.fill(iguais, 7);
            ordenar(algoritmo, iguais, Integer::compare);
            verificar(Arrays.stream(iguais).allMatch(n -> n == 7), "Todos iguais");
            String[] palavras = {"Bia", null, "ana", "Caio"};
            ordenar(algoritmo, palavras, Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER));
            verificar(Arrays.equals(palavras, new String[]{null, "ana", "Bia", "Caio"}), "Comparador com null");
            Item[] itens = new Item[80];
            for (int i = 0; i < itens.length; i++) itens[i] = new Item(i % 4, i);
            Comparator<Item> porChave = Comparator.comparingInt(Item::chave);
            if (algoritmo == 1 || algoritmo == 2 || algoritmo == 3) {
                Item[] esperado = itens.clone();
                Arrays.sort(esperado, porChave);
                ordenar(algoritmo, itens, porChave);
                verificar(Arrays.equals(esperado, itens), "Estabilidade " + algoritmo);
            }
            Comparator<Item> desempate = porChave.reversed().thenComparingInt(Item::posicaoOriginal);
            Item[] esperado = itens.clone();
            Arrays.sort(esperado, desempate);
            ordenar(algoritmo, itens, desempate);
            verificar(Arrays.equals(esperado, itens), "Critérios compostos " + algoritmo);
        }
        Integer[][] naturais = {{3, 1, 2}, {3, 1, 2}, {3, 1, 2}, {3, 1, 2}, {3, 1, 2}, {3, 1, 2}};
        SelectionSort.ordenar(naturais[0]); BubbleSort.ordenar(naturais[1]);
        InsertionSort.ordenar(naturais[2]); MergeSort.ordenar(naturais[3]);
        HeapSort.ordenar(naturais[4]); QuickSort.ordenar(naturais[5]);
        for (Integer[] v : naturais) verificar(Arrays.equals(v, new Integer[]{1, 2, 3}), "Ordem natural");
        Integer[] grande = new Integer[100000];
        for (int i = 0; i < grande.length; i++) grande[i] = grande.length - i;
        QuickSort.ordenar(grande);
        for (int i = 0; i < grande.length; i++) verificar(grande[i] == i + 1, "QuickSort grande");
    }

    private static void testarProdutos() throws Exception {
        LocalDate hoje = LocalDate.now();
        Produto lapis = new ProdutoNaoPerecivel("Lapis", 10);
        perto(12, lapis.valorDeVenda());
        perto(15, new ProdutoNaoPerecivel("Lapis", 10, .5).valorDeVenda());
        for (int dias : new int[]{0, 1, 7})
            perto(9, new ProdutoPerecivel("Leite", 10, hoje.plusDays(dias)).valorDeVenda());
        for (int dias : new int[]{8, 32, 370})
            perto(12, new ProdutoPerecivel("Leite", 10, hoje.plusDays(dias)).valorDeVenda());
        falha(IllegalArgumentException.class, () -> new ProdutoPerecivel("Leite", 10, hoje.minusDays(1)));
        falha(IllegalArgumentException.class, () -> new ProdutoNaoPerecivel(null, 1));
        falha(IllegalArgumentException.class, () -> new ProdutoNaoPerecivel("ab", 1));
        falha(IllegalArgumentException.class, () -> new ProdutoNaoPerecivel("Lapis", Double.NaN));
        falha(IllegalArgumentException.class, () -> new ProdutoNaoPerecivel("Lapis", 1, 0));
        falha(IllegalArgumentException.class, () -> new ProdutoNaoPerecivel("Lapis;azul", 1));
        Produto outro = new ProdutoPerecivel("LAPIS", 2, hoje.plusDays(10));
        verificar(lapis.equals(outro) && outro.equals(lapis), "Igualdade por descrição");
        verificar(lapis.hashCode() == outro.hashCode(), "Contrato de hashCode");
        verificar(!lapis.equals(null) && !lapis.equals("Lapis"), "Igualdade com outros tipos");
        verificar(lapis.gerarDadosTexto().equals("1;Lapis;10.00;0.20"), "Formato decimal");
        for (Produto p : new Produto[]{lapis, outro}) {
            Produto copia = Produto.criarDoTexto(p.gerarDadosTexto());
            verificar(copia.getClass() == p.getClass(), "Tipo restaurado");
            verificar(copia.gerarDadosTexto().equals(p.gerarDadosTexto()), "Ida e volta do texto");
            perto(p.valorDeVenda(), copia.valorDeVenda());
        }
        falha(DateTimeParseException.class, () -> Produto.criarDoTexto("2;Leite;1.00;0.20;31/02/2099"));
        falha(IllegalArgumentException.class, () -> Produto.criarDoTexto("3;Lapis;1;0.2"));
        falha(IllegalArgumentException.class, () -> Produto.criarDoTexto("1;Lapis;1;0.2;extra"));
        // Simula a passagem do tempo sem alterar o relógio do computador.
        ProdutoPerecivel vencido = new ProdutoPerecivel("Leite", 1, hoje);
        var campo = ProdutoPerecivel.class.getDeclaredField("dataDeValidade");
        campo.setAccessible(true);
        campo.set(vencido, hoje.minusDays(1));
        falha(IllegalArgumentException.class, vencido::valorDeVenda);

        Path arquivo = Files.createTempFile("aeds-produtos-", ".csv");
        try {
            App.produtosCadastrados = new Produto[]{lapis, outro, null};
            App.quantosProdutos = 2;
            App.salvarProdutos(arquivo.toString());
            Produto[] lidos = App.lerProdutos(arquivo.toString());
            verificar(!App.falhaNaLeitura && lidos.length == 2, "Leitura do arquivo salvo");
            verificar(lidos[1].gerarDadosTexto().equals(outro.gerarDadosTexto()), "Dados preservados");
            for (String ruim : new String[]{"", "-1\n", "2\n1;Lapis;1;0.2\n",
                    "0\n1;Lapis;1;0.2\n", "1\n2;Leite;1;0.2;01/01/2000\n"}) {
                Files.writeString(arquivo, ruim, StandardCharsets.UTF_8);
                verificar(App.lerProdutos(arquivo.toString()).length == 0 && App.falhaNaLeitura,
                        "Arquivo inválido deve retornar vetor vazio");
                verificar(Files.readString(arquivo).equals(ruim), "Leitura não altera arquivo");
            }
            App.quantosProdutos = 0;
            App.salvarProdutos(arquivo.toString());
            verificar(App.lerProdutos(arquivo.toString()).length == 0 && !App.falhaNaLeitura, "Cadastro vazio");
        } finally {
            Files.deleteIfExists(arquivo);
        }
        verificar(App.lerProdutos(arquivo.toString()).length == 0 && !App.falhaNaLeitura, "Arquivo inexistente");
    }

    private static void testarRecursividade() {
        verificar(Recursividade.somarPares(0) == 0, "Pares zero");
        verificar(Recursividade.somarPares(1) == 0, "Pares um");
        verificar(Recursividade.somarPares(10) == 30, "Limite par inclusivo");
        verificar(Recursividade.somarPares(11) == 30, "Limite ímpar");
        falha(IllegalArgumentException.class, () -> Recursividade.somarPares(-1));
        perto(0, Recursividade.somarVetor(new double[0]));
        perto(2.5, Recursividade.somarVetor(new double[]{1.5, -2, 3}));
        verificar(Recursividade.contarRepeticoes(new int[0], 2) == 0, "Contagem vazia");
        verificar(Recursividade.contarRepeticoes(new int[]{2, 1, 2, 2}, 2) == 3, "Contagem repetida");
        verificar(Recursividade.contarRepeticoes(new int[]{1, 3}, 2) == 0, "Número ausente");
    }

    public static void main(String[] args) throws Exception {
        testarOrdenacao();
        testarProdutos();
        testarRecursividade();
        System.out.println("OK: " + verificacoes + " verificações.");
    }
}
