package recursividade;

/** Os três exercícios usam caso base + redução para uma instância menor. */
public final class Recursividade {
    private Recursividade() { }

    /** Soma 0 + 2 + ... até o limite, inclusive. Tempo e pilha O(limite/2). */
    public static long somarPares(int limite) {
        if (limite < 0) throw new IllegalArgumentException("O limite deve ser não negativo.");
        return somarParesAte(limite - limite % 2);
    }

    private static long somarParesAte(int par) {
        if (par == 0) return 0; // Caso base: não há outro par positivo a somar.
        return par + somarParesAte(par - 2);
    }

    /** Tempo O(n) e pilha O(n); vetor vazio tem soma zero. */
    public static double somarVetor(double[] vetor) {
        return somarVetor(vetor, 0);
    }

    private static double somarVetor(double[] vetor, int indice) {
        if (indice == vetor.length) return 0;
        return vetor[indice] + somarVetor(vetor, indice + 1);
    }

    /** Conta valores inteiros iguais ao procurado. Tempo e pilha O(n). */
    public static int contarRepeticoes(int[] vetor, int procurado) {
        return contarRepeticoes(vetor, procurado, 0);
    }

    private static int contarRepeticoes(int[] vetor, int procurado, int indice) {
        if (indice == vetor.length) return 0;
        int atual = vetor[indice] == procurado ? 1 : 0;
        return atual + contarRepeticoes(vetor, procurado, indice + 1);
    }
}
