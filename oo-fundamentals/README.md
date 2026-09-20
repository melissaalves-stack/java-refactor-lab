# OO Fundamentals — Loja de Consoles

Evolução progressiva de um sistema de loja de consoles em cinco etapas, cada uma construída sobre o resultado da anterior. O ponto de partida é um código funcional que usa atributos públicos, sem construtores, sem interfaces e com toda a lógica de tipos concentrada em um `if/else` dentro da classe `Loja`.

---

## Etapa 1 — Encapsulamento

**Problema:** atributos `nome`, `tipo` e `preco` eram públicos — qualquer código podia atribuir um preço negativo ou um tipo inválido sem que a classe tivesse como impedir.

**Solução:** atributos tornados `private`; getters criados para leitura controlada. Sem setters: uma vez construído, o estado não muda externamente.

---

## Etapa 2 — Construtores

**Problema:** `Console` podia ser criado "pela metade" — esquecer de definir `preco` resultava em `0.0` silenciosamente, um bug difícil de rastrear.

**Solução:** construtor que exige `nome`, `tipo` e `preco` como parâmetros obrigatórios. Sem eles, o compilador rejeita a criação. Estado inválido deixa de ser possível.

---

## Etapa 3 — Interface e Composição

**Problema:** a classe `Console` distinguia tipos via campo de texto (`"nintendo"`, `"playstation"`) verificado por `if/else` na `Loja` — adicionar um novo console exigia editar a loja.

**Solução:**

- `IConsole` define o contrato: `ligar()`, `calcularPreco()`, `getNome()`
- `DadosConsole` encapsula os dados comuns (`nome` e `precoBase`) em um único lugar
- `Nintendo` e `Playstation` implementam `IConsole` e **têm** um `DadosConsole` (composição), delegando a ele em vez de duplicar os campos
- A lógica que estava no `if/else` da `Loja` foi movida para dentro de cada classe

**Por que `DadosConsole` separado?** Se `nome` e `precoBase` ficassem repetidos em `Nintendo` e `Playstation`, qualquer mudança nessa estrutura precisaria ser feita em dois lugares. Com composição, muda em um só — e cada console adicional só precisa *ter* um `DadosConsole`.

---

## Etapa 4 — Herança

**Problema:** `PlaystationPortatil` precisaria repetir toda a implementação de `Playstation` apenas para mudar dois comportamentos.

**Solução:** `PlaystationPortatil extends Playstation`, sobrescrevendo `ligar()` e `calcularPreco()` com seus próprios valores — sem quebrar nenhuma promessa da superclasse.

Isso é diferente do antipadrão de herança onde uma subclasse sobrescreve um método apenas para lançar exceção (`UnsupportedOperationException`), recusando o que a superclasse prometia. Ali a subclasse quebra o contrato e viola o Princípio de Substituição de Liskov. Aqui, `PlaystationPortatil` ainda liga e ainda calcula preço — apenas de forma diferente.

---

## Etapa 5 — Polimorfismo e OCP

**Problema:** `venderConsole` recebia `Console` e usava `if/else` para decidir o comportamento — código fechado para extensão, aberto para modificação.

**Solução:**

- `venderConsole(IConsole console)` apenas chama `console.ligar()` e `console.calcularPreco()` — sem `if/else`, sem `instanceof`
- `venderVarios(List<IConsole>)` itera a lista chamando `venderConsole()` para cada item
- `calcularFaturamentoTotal(List<IConsole>)` soma `calcularPreco()` de todos

`Xbox` foi adicionado implementando `IConsole` com seu próprio percentual — **sem alterar uma linha de `Loja`**. Esse é o Princípio Aberto/Fechado na prática: a classe está fechada para modificação e aberta para extensão via interface.

---

## Estrutura

```
src/
└── Atividade_POO_Solucao.java
    ├── interface IConsole
    ├── class DadosConsole
    ├── class Nintendo         implements IConsole
    ├── class Playstation      implements IConsole
    ├── class PlaystationPortatil  extends Playstation
    ├── class Xbox             implements IConsole
    ├── class Loja
    └── class Atividade_POO_Solucao (main)
```

## Como executar

```bash
javac src/Atividade_POO_Solucao.java -d out
java -cp out Atividade_POO_Solucao
```
