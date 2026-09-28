import java.util.Locale;

public class ProdutoNaoPerecivel extends Produto {
    public ProdutoNaoPerecivel(String desc, double custo, double margem) {
        super(desc, custo, margem);
    }

    public ProdutoNaoPerecivel(String desc, double custo) {
        super(desc, custo);
    }

    @Override
    public double valorDeVenda() {
        return precoCusto * (1.0 + margemLucro);
    }

    @Override
    public String gerarDadosTexto() {
        // Locale.ROOT fixa o ponto decimal, independentemente do computador.
        return String.format(Locale.ROOT, "1;%s;%.2f;%.2f", descricao, precoCusto, margemLucro);
    }
}
