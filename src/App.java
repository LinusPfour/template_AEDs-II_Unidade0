import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

/** Oficina 0b: métodos e vetor do esqueleto da professora, agora implementados. */
public class App {
    static final int MAX_NOVOS_PRODUTOS = 10;
    static String nomeArquivoDados;
    static Scanner teclado;
    static Produto[] produtosCadastrados;
    static int quantosProdutos;
    static boolean falhaNaLeitura;

    static void cabecalho() {
        System.out.println("\nAEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }

    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e imprimir os dados de um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Salvar e sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /** Retorna apenas os produtos lidos; a capacidade extra é reservada no main. */
    static Produto[] lerProdutos(String nomeArquivo) {
        falhaNaLeitura = false;
        Path caminho = Path.of(nomeArquivo);
        if (Files.notExists(caminho)) return new Produto[0];
        try (BufferedReader arquivo = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            String cabecalho = arquivo.readLine();
            if (cabecalho == null) throw new IllegalArgumentException("Arquivo vazio.");
            int n = Integer.parseInt(cabecalho.trim());
            // Aceitamos zero para permitir salvar um cadastro ainda vazio.
            if (n < 0) throw new IllegalArgumentException("Quantidade negativa.");
            Produto[] produtos = new Produto[n];
            for (int i = 0; i < n; i++) produtos[i] = Produto.criarDoTexto(arquivo.readLine());
            String linha;
            while ((linha = arquivo.readLine()) != null) {
                if (!linha.isBlank()) throw new IllegalArgumentException("Há produtos além da quantidade indicada.");
            }
            return produtos;
        } catch (IOException | IllegalArgumentException | DateTimeException e) {
            falhaNaLeitura = true;
            System.err.println("Não foi possível carregar os produtos: " + e.getMessage());
            return new Produto[0];
        }
    }

    static void localizarProdutos() {
        System.out.print("Descrição procurada: ");
        String nome = teclado.nextLine().trim();
        boolean encontrou = false;
        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].getDescricao().equalsIgnoreCase(nome)) {
                imprimirProduto(i);
                encontrou = true;
            }
        }
        if (!encontrou) System.out.println("Produto não encontrado.");
    }

    static void imprimirProduto(int i) {
        try {
            System.out.println((i + 1) + " - " + produtosCadastrados[i]);
        } catch (IllegalArgumentException e) {
            // Um programa aberto por vários dias pode ter um produto que venceu nesse intervalo.
            System.out.println((i + 1) + " - " + produtosCadastrados[i].getDescricao() + " (vencido)");
        }
    }

    public static void salvarProdutos(String nomeArquivo) {
        try (BufferedWriter arquivo = Files.newBufferedWriter(Path.of(nomeArquivo), StandardCharsets.UTF_8)) {
            arquivo.write(Integer.toString(quantosProdutos));
            arquivo.newLine();
            for (int i = 0; i < quantosProdutos; i++) {
                arquivo.write(produtosCadastrados[i].gerarDadosTexto());
                arquivo.newLine();
            }
            System.out.println("Produtos salvos em " + nomeArquivo);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível salvar os produtos.", e);
        }
    }

    static void listarTodosOsProdutos() {
        if (quantosProdutos == 0) System.out.println("Nenhum produto cadastrado.");
        for (int i = 0; i < quantosProdutos; i++) imprimirProduto(i);
    }

    static double lerDecimal(String mensagem) {
        System.out.print(mensagem);
        return Double.parseDouble(teclado.nextLine().trim().replace(',', '.'));
    }

    static void cadastrarProduto() {
        if (quantosProdutos == produtosCadastrados.length) {
            System.out.println("Limite de " + MAX_NOVOS_PRODUTOS + " novos produtos nesta execução atingido.");
            return;
        }
        System.out.print("Tipo (1 - não perecível; 2 - perecível): ");
        int tipo = Integer.parseInt(teclado.nextLine());
        if (tipo != 1 && tipo != 2) throw new IllegalArgumentException("Tipo inválido.");
        System.out.print("Descrição: ");
        String descricao = teclado.nextLine();
        double custo = lerDecimal("Preço de custo: ");
        System.out.print("Margem (ex.: 0,20 para 20%; Enter usa 20%): ");
        String entrada = teclado.nextLine().trim();
        double margem = entrada.isEmpty() ? 0.20 : Double.parseDouble(entrada.replace(',', '.'));
        Produto novo;
        if (tipo == 1) {
            novo = new ProdutoNaoPerecivel(descricao, custo, margem);
        } else {
            System.out.print("Validade (dd/mm/aaaa): ");
            LocalDate validade = LocalDate.parse(teclado.nextLine().trim(), Produto.FORMATO_DATA);
            novo = new ProdutoPerecivel(descricao, custo, margem, validade);
        }
        // Só avançamos o contador depois de construir um produto válido.
        produtosCadastrados[quantosProdutos++] = novo;
        System.out.println("Produto cadastrado.");
    }

    public static void main(String[] args) {
        nomeArquivoDados = args.length == 0 ? "dadosProdutos.csv" : args[0];
        Produto[] carregados = lerProdutos(nomeArquivoDados);
        if (falhaNaLeitura) {
            // Não sobrescrever um arquivo com dados inválidos por um cadastro vazio.
            System.err.println("Corrija o arquivo antes de continuar. O conteúdo original foi preservado.");
            return;
        }
        quantosProdutos = carregados.length;
        produtosCadastrados = Arrays.copyOf(carregados, quantosProdutos + MAX_NOVOS_PRODUTOS);
        teclado = new Scanner(System.in, StandardCharsets.UTF_8);
        boolean sair = false;
        while (!sair) {
            try {
                switch (menu()) {
                    case 1 -> listarTodosOsProdutos();
                    case 2 -> localizarProdutos();
                    case 3 -> cadastrarProduto();
                    case 0 -> sair = true;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | DateTimeException e) {
                System.out.println("Entrada inválida: " + e.getMessage());
            } catch (java.util.NoSuchElementException e) {
                sair = true; // EOF durante um cadastro: o produto incompleto não foi inserido.
            }
        }
        salvarProdutos(nomeArquivoDados);
        teclado.close();
    }
}
