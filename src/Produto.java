import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/** Parte comum aos produtos das oficinas 0a (polimorfismo) e 0b (arquivos). */
public abstract class Produto {
    private static final double MARGEM_PADRAO = 0.20;
    protected static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    protected String descricao;
    protected double precoCusto;
    protected double margemLucro;

    /** Dados inválidos causam exceção; não criamos um produto parcialmente válido. */
    private void init(String desc, double custo, double margem) {
        if (desc == null || desc.trim().length() < 3 || desc.contains(";")
                || desc.contains("\n") || desc.contains("\r")
                || !Double.isFinite(custo) || custo < 0.01
                || !Double.isFinite(margem) || margem < 0.01) {
            throw new IllegalArgumentException("Descrição (mínimo 3 caracteres), custo ou margem inválidos.");
        }
        // O arquivo usa ponto e vírgula como separador, sem campos entre aspas.
        descricao = desc.trim();
        precoCusto = custo;
        margemLucro = margem;
    }

    protected Produto(String desc, double custo, double margem) {
        init(desc, custo, margem);
    }

    protected Produto(String desc, double custo) {
        this(desc, custo, MARGEM_PADRAO);
    }

    public String getDescricao() { return descricao; }
    public double getPrecoCusto() { return precoCusto; }
    public double getMargemLucro() { return margemLucro; }

    public abstract double valorDeVenda();
    public abstract String gerarDadosTexto();

    @Override
    public String toString() {
        NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        return "NOME: " + descricao + ": " + moeda.format(valorDeVenda());
    }

    /** O tipo e o preço não participam da igualdade pedida no enunciado. */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Produto outro && descricao.equalsIgnoreCase(outro.descricao);
    }

    @Override
    public int hashCode() {
        // A mesma normalização de caixa deve valer para equals e hashCode.
        return descricao.codePoints().map(Character::toUpperCase).map(Character::toLowerCase)
                .reduce(0, (hash, letra) -> 31 * hash + letra);
    }

    /** Fábrica: escolhe a subclasse pelo primeiro campo da linha. */
    public static Produto criarDoTexto(String linha) {
        if (linha == null) throw new IllegalArgumentException("Linha de produto ausente.");
        String[] campos = linha.split(";", -1);
        if (campos.length < 4) throw new IllegalArgumentException("Linha incompleta: " + linha);
        int tipo = Integer.parseInt(campos[0].trim());
        double custo = Double.parseDouble(campos[2].trim());
        double margem = Double.parseDouble(campos[3].trim());
        if (tipo == 1 && campos.length == 4) {
            return new ProdutoNaoPerecivel(campos[1], custo, margem);
        }
        if (tipo == 2 && campos.length == 5) {
            LocalDate validade = LocalDate.parse(campos[4].trim(), FORMATO_DATA);
            return new ProdutoPerecivel(campos[1], custo, margem, validade);
        }
        throw new IllegalArgumentException("Tipo ou quantidade de campos inválidos: " + linha);
    }
}
