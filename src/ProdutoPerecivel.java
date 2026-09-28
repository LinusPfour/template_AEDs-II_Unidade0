import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class ProdutoPerecivel extends Produto {
    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double custo, double margem, LocalDate validade) {
        super(desc, custo, margem);
        definirValidade(validade);
    }

    public ProdutoPerecivel(String desc, double custo, LocalDate validade) {
        super(desc, custo);
        definirValidade(validade);
    }

    private void definirValidade(LocalDate validade) {
        if (validade == null || validade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A validade não pode ser anterior a hoje.");
        }
        dataDeValidade = validade;
    }

    public LocalDate getDataDeValidade() { return dataDeValidade; }

    @Override
    public double valorDeVenda() {
        LocalDate hoje = LocalDate.now();
        if (dataDeValidade.isBefore(hoje)) {
            throw new IllegalArgumentException("Produto vencido: " + descricao);
        }
        double valor = precoCusto * (1.0 + margemLucro);
        // DAYS.between conta TODOS os dias, inclusive ao atravessar meses e anos.
        // Period.getDays() devolveria apenas a parte dos dias restante após os meses.
        long dias = ChronoUnit.DAYS.between(hoje, dataDeValidade);
        if (dias <= PRAZO_DESCONTO) valor *= 1.0 - DESCONTO;
        return valor;
    }

    @Override
    public String toString() {
        return super.toString() + " | Válido até " + dataDeValidade.format(FORMATO_DATA);
    }

    @Override
    public String gerarDadosTexto() {
        return String.format(Locale.ROOT, "2;%s;%.2f;%.2f;%s", descricao, precoCusto,
                margemLucro, dataDeValidade.format(FORMATO_DATA));
    }
}
