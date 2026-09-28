# Consulta rápida: métodos de ordenação

Cada classe tem `ordenar(vetor)` para ordem natural e `ordenar(vetor, comparador)` para definir o critério. O próprio vetor é alterado; use `vetor.clone()` antes da chamada se precisar preservar a entrada. Os métodos genéricos recebem `Integer[]`, `Double[]`, `String[]`, `Produto[]` etc., não vetores primitivos como `int[]`. Vetores vazios e unitários são aceitos. O vetor e o comparador devem existir; elementos nulos exigem um comparador que os trate, como `Comparator.nullsFirst(...)`.

| Classe | Melhor tempo | Tempo médio | Pior tempo | Espaço adicional | Estável? |
| --- | --- | --- | --- | --- | --- |
| SelectionSort | O(n²) | O(n²) | O(n²) | O(1) | Não |
| BubbleSort | O(n) | O(n²) | O(n²) | O(1) | Sim |
| InsertionSort | O(n) | O(n²) | O(n²) | O(1) | Sim |
| MergeSort | O(n log n) | O(n log n) | O(n log n) | O(n) | Sim |
| HeapSort | O(n)* | O(n log n) | O(n log n) | O(1) | Não |
| QuickSort | O(n log n) | O(n log n) | O(n²) | O(log n)** | Não |

As análises supõem comparações O(1). Comparar strings, por exemplo, também depende do comprimento delas. Estabilidade significa manter a ordem original dos elementos que empatam no comparador.

*Neste HeapSort, `descer` encerra quando o pai já domina os filhos. Com todas as chaves iguais, a execução é linear; para chaves distintas, a análise usual do melhor caso é O(n log n). A construção inicial do heap é O(n), não O(n log n).

**Este QuickSort chama recursivamente só a partição menor e trata a maior em um laço. Isso limita a pilha a O(log n), mas não evita tempo O(n²) em partições ruins. Na implementação tradicional com duas chamadas recursivas, a pilha pode atingir O(n).

## Como lembrar

- **Seleção:** procurar o menor no trecho não ordenado e trocar com o início. Faz poucas trocas, mas sempre compara todos os pares de posições necessários.
- **Bolha:** trocar vizinhos fora de ordem. A cada passagem, o maior chega ao fim. Sem trocas, pode parar.
- **Inserção:** guardar a chave, deslocar os maiores para a direita e inserir no espaço aberto. Funciona bem em vetores pequenos ou quase ordenados.
- **Merge:** dividir ao meio, ordenar as metades e intercalar. Nos empates, retirar primeiro da esquerda para preservar a estabilidade.
- **Heap:** construir um heap máximo, levar a raiz ao final e restaurar o heap reduzido. Filhos de `i`: `2*i+1` e `2*i+2`; pai: `(i-1)/2`, se `i > 0`.
- **Quick:** separar elementos em torno de um pivô e ordenar as partições. Avançar os dois índices depois da troca é essencial quando há valores iguais.

## Comparadores

O sinal de `compare(a, b)` indica a ordem: negativo coloca `a` antes de `b`; zero representa empate; positivo coloca `a` depois de `b`. Não é necessário retornar exatamente -1 ou 1. O critério deve ser consistente e transitivo durante a ordenação.

```java
Integer[] numeros = {7, 2, 7, -1};
QuickSort.ordenar(numeros); // -1, 2, 7, 7
HeapSort.ordenar(numeros, Comparator.reverseOrder()); // 7, 7, 2, -1

Comparator<Produto> nome = Comparator.comparing(
        Produto::getDescricao, String.CASE_INSENSITIVE_ORDER);
Comparator<Produto> preco = Comparator.comparingDouble(Produto::valorDeVenda);
MergeSort.ordenar(produtos, preco.thenComparing(nome));

// Preço decrescente, nome crescente nos empates:
InsertionSort.ordenar(produtos, preco.reversed().thenComparing(nome));
// Inverter depois da composição inverte ambos os critérios:
BubbleSort.ordenar(produtos, preco.thenComparing(nome).reversed());
```

Use `Integer.compare(a, b)` e `Double.compare(a, b)` em comparadores manuais. `a - b` pode estourar um inteiro; converter a diferença de doubles para int pode criar empates incorretos. `Comparator.comparingInt` e `comparingDouble` evitam esses erros.

`Comparable` define uma ordem natural na classe; `Comparator` permite vários critérios sem mudar a classe. `Produto` usa comparadores, pois faz sentido ordená-lo por diferentes atributos. A igualdade de produtos continua sendo por descrição, como pede a oficina, mesmo quando o comparador usa preço. Ordenar por valor de venda exige produtos ainda válidos.

Para adaptar os algoritmos a `int[]` numa questão específica: substitua `T` por `int`, use comparação direta no lugar de `comparador.compare(...)` e mantenha os mesmos limites e movimentos. Não use `<` e `>` diretamente em objetos; nesse caso, mantenha o comparador.

O exemplo executável completo está em `src/ExemploOrdenacao.java`.
