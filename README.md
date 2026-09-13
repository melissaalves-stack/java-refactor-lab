# Laboratório de Refatoração — Princípios de Projeto

Disciplina: Gestão do Ciclo de Vida da Aplicação (DCE795)

## Como executar

Pré-requisito: JDK 17 ou superior.

Compile na pasta raiz do projeto:

```bash
javac -d out src/projetoprincipiosdesign/*.java
```

Execute:

```bash
java -cp out projetoprincipiosdesign.Main
```

## O que foi refatorado e por quê

### 1. Responsabilidade Única (SRP)

**Problema:** o método `finalizarPedido` em `PedidoService` fazia tudo junto: calculava o total, gravava em arquivo, imprimia o resumo, decidia o pagamento e ainda notificava. Qualquer mudança em uma dessas frentes obrigava a editar o mesmo método.

**Correção:** a lógica de persistência foi extraída para `PedidoRepositoryImpl`, que implementa `IPedidoRepository`. O `PedidoService` agora só orquestra as etapas, delegando cada responsabilidade para quem é dono dela.

Também removi o método `obterCidadeEntrega` de `PedidoService`, que só repassava uma chamada que já pertencia ao `Cliente` (ver Lei de Demeter abaixo).

---

### 2. Aberto/Fechado (OCP)

**Problema:** `calcularTotal` comparava uma string (`"ALUNO"`, `"PROFESSOR"`, `"FUNCIONARIO"`) para decidir o desconto. Adicionar um novo tipo de desconto exigia editar esse método e arriscar quebrar o que já funcionava.

**Correção:** criei a interface `IDesconto` com o método `aplicar(double valor)`. Cada tipo de cliente ganhou sua própria classe (`DescontoAluno`, `DescontoProfessor`, `DescontoFuncionario`). O método `calcularTotal` recebe um `IDesconto` pronto e não sabe quantos tipos existem. Para adicionar um desconto de Black Friday, basta criar uma nova classe, sem tocar no serviço.

O mesmo raciocínio eliminou o `if/else` de `formaPagamento.equals("CARTAO"/"PIX"/"BOLETO")` dentro de `finalizarPedido`.

---

### 3. Substituição de Liskov (LSP)

**Problema 1:** `EntregaRetiradaLoja` herdava de `Entrega` e lançava `IllegalStateException` dentro de `calcularFrete` quando o total era baixo. Qualquer código escrito para `Entrega` poderia receber uma exceção inesperada ao usar o subtipo.

**Correção:** criei a interface `IEntrega` com um método `disponivelPara(double total)`. Assim quem usa pode verificar antes de chamar `calcularFrete`, e nenhuma implementação precisa lançar exceção. `EntregaRetiradaLoja` passou a implementar `IEntrega` diretamente.

**Problema 2:** `IPagamento` original tinha `parcelar` e `gerarBoleto`, que `PagamentoPix` não suportava e lançava `UnsupportedOperationException`. Isso foi resolvido junto com o ISP abaixo.

---

### 4. Segregação de Interfaces (ISP)

**Problema:** `IPagamento` tinha três métodos: `pagar`, `parcelar` e `gerarBoleto`. Pix não parcela, cartão não gera boleto. As classes eram obrigadas a implementar métodos com corpos falsos ou exceções.

**Correção:** separei em três interfaces menores:
- `IPagamento` com apenas `processar(double valor)`
- `IParcelavel` com `parcelar(double valor, int parcelas)`
- `IGeradorBoleto` com `gerarBoleto(double valor)`

`PagamentoCartao` implementa `IPagamento` e `IParcelavel`. `PagamentoBoleto` implementa `IPagamento` e `IGeradorBoleto`. `PagamentoPix` implementa só `IPagamento`. Cada classe promete apenas o que realmente cumpre.

---

### 5. Inversão de Dependência (DIP)

**Problema:** `PedidoService` instanciava `PagamentoCartao`, `PagamentoPix` e `PagamentoBoleto` diretamente dentro do método, com `new`. A lógica de I/O (gravar em arquivo) também estava embutida no mesmo método, sem nenhuma abstração.

**Correção:** `PedidoService` recebe `IPedidoRepository` no construtor (injeção de dependência) e recebe `IDesconto` e `IPagamento` como parâmetros de `finalizarPedido`. Ele depende apenas de interfaces. Quem decide qual implementação concreta usar é o `Main`, que funciona como raiz de composição.

---

### 6. Composição no lugar de Herança

**Problema:** `PedidoService extends PagamentoCartao`. Um serviço de pedido não tem nenhuma relação de "é um tipo de" pagamento no cartão. A herança foi usada só para acessar algum comportamento, sem relação conceitual real.

**Correção:** removi o `extends`. `PedidoService` agora tem um repositório como atributo (composição) e recebe pagamento e desconto como colaboradores. A pergunta "X é um tipo de Y?" é a checagem que uso antes de usar `extends`: se a resposta for não, é composição.

`EntregaRetiradaLoja` também parou de herdar de `Entrega` e passou a implementar `IEntrega`.

---

### 7. Lei de Demeter

**Problema:** `obterCidadeEntrega` em `PedidoService` navegava assim:
```java
pedido.getCliente().getEndereco().getCidade().getNome();
```
Isso expõe a estrutura interna de `Cliente`, `Endereco` e `Cidade` para `PedidoService`, que não precisa saber nada disso.

**Correção:** adicionei o método `getCidadeEntrega()` em `Cliente`, que é quem de fato conhece sua estrutura interna. No `Main`, a chamada vira simplesmente `cliente.getCidadeEntrega()`. O método de repasse em `PedidoService` foi removido porque não precisava existir lá.

---

## Estrutura de arquivos após refatoração

```
src/projetoprincipiosdesign/
    Cidade.java
    Endereco.java
    Cliente.java               getCidadeEntrega() adicionado
    ItemPedido.java
    Pedido.java
    IDesconto.java             nova interface (OCP)
    DescontoAluno.java         nova classe (OCP)
    DescontoProfessor.java     nova classe (OCP)
    DescontoFuncionario.java   nova classe (OCP)
    IPagamento.java            simplificada (ISP)
    IParcelavel.java           nova interface (ISP)
    IGeradorBoleto.java        nova interface (ISP)
    PagamentoCartao.java       refatorado (ISP, LSP)
    PagamentoPix.java          refatorado (ISP, LSP)
    PagamentoBoleto.java       refatorado (ISP)
    IEntrega.java              nova interface (LSP)
    Entrega.java               refatorado (LSP)
    EntregaRetiradaLoja.java   refatorado (LSP, Composição)
    IPedidoRepository.java     nova interface (DIP, SRP)
    PedidoRepositoryImpl.java  nova classe (DIP, SRP)
    PedidoService.java         refatorado (SRP, OCP, DIP, Composição)
    Main.java                  refatorado (DIP, Lei de Demeter)
```
