# SOLID Principles — Sistema de Pedidos

Refatoração de um sistema de pedidos de loja que violava os cinco princípios SOLID, além de usar herança inadequada e ignorar a Lei de Demeter.

O ponto de partida é um código que compila e roda, mas concentra lógica demais em lugares errados: um único método decide pagamento, calcula total, persiste dados e imprime resumo; tipos de cliente são distinguidos por strings comparadas em `if/else`; interfaces forçam classes a implementar métodos que não suportam.

---

## O que foi corrigido

### SRP — Single Responsibility Principle
**Problema:** `finalizarPedido` em `PedidoService` acumulava instanciação, cálculo, persistência e notificação no mesmo método.

**Correção:** persistência extraída para `PedidoRepositoryImpl`, que implementa `IPedidoRepository`. `PedidoService` passou a apenas orquestrar as etapas, delegando cada responsabilidade para quem a possui.

---

### OCP — Open/Closed Principle
**Problema:** `calcularTotal` comparava strings (`"ALUNO"`, `"PROFESSOR"`, `"FUNCIONARIO"`) para aplicar desconto. Adicionar um novo tipo exigia editar o método e arriscar quebrar o que já funcionava.

**Correção:** interface `IDesconto` com `aplicar(double valor)`. Cada tipo virou uma classe (`DescontoAluno`, `DescontoProfessor`, `DescontoFuncionario`). O método recebe um `IDesconto` pronto e não sabe quantos tipos existem. Para adicionar desconto de Black Friday: nova classe, zero linhas alteradas no serviço.

---

### LSP — Liskov Substitution Principle
**Problema:** `EntregaRetiradaLoja extends Entrega` lançava `IllegalStateException` dentro de `calcularFrete` quando o total era baixo. Código escrito para `Entrega` podia receber uma exceção inesperada ao usar o subtipo.

**Correção:** interface `IEntrega` com `disponivelPara(double total)`. Quem usa verifica disponibilidade antes de chamar `calcularFrete`; nenhuma implementação precisa lançar exceção. `EntregaRetiradaLoja` passou a implementar `IEntrega` diretamente.

---

### ISP — Interface Segregation Principle
**Problema:** `IPagamento` tinha `pagar`, `parcelar` e `gerarBoleto`. Pix não parcela, cartão não gera boleto — as classes eram forçadas a implementar métodos com corpos falsos ou exceções.

**Correção:** três interfaces menores:
- `IPagamento` → `processar(double valor)`
- `IParcelavel` → `parcelar(double valor, int parcelas)`
- `IGeradorBoleto` → `gerarBoleto(double valor)`

`PagamentoCartao` implementa `IPagamento` + `IParcelavel`. `PagamentoBoleto` implementa `IPagamento` + `IGeradorBoleto`. `PagamentoPix` implementa só `IPagamento`. Cada classe promete apenas o que cumpre.

---

### DIP — Dependency Inversion Principle
**Problema:** `PedidoService` instanciava `PagamentoCartao`, `PagamentoPix` e `PagamentoBoleto` com `new` dentro do método. Dependia de implementações concretas.

**Correção:** `PedidoService` recebe `IPedidoRepository` no construtor e `IDesconto` + `IPagamento` como parâmetros de `finalizarPedido`. Depende apenas de interfaces. O `Main` funciona como raiz de composição e decide qual implementação concreta injetar.

---

### Composição no lugar de herança
**Problema:** `PedidoService extends PagamentoCartao` — um serviço de pedido não tem relação "é um tipo de" com pagamento em cartão. A herança foi usada apenas para acessar comportamento.

**Correção:** `extends` removido. `PedidoService` recebe pagamento como colaborador. A checagem: "X é um tipo de Y?" — se a resposta for não, é composição.

---

### Lei de Demeter
**Problema:** `obterCidadeEntrega` em `PedidoService` navegava `pedido.getCliente().getEndereco().getCidade().getNome()`, expondo a estrutura interna de três classes que o serviço não deveria conhecer.

**Correção:** método `getCidadeEntrega()` adicionado em `Cliente`, que é quem conhece sua própria estrutura. A chamada no `Main` virou simplesmente `cliente.getCidadeEntrega()`.

---

## Estrutura de arquivos

```
src/projetoprincipiosdesign/
├── Cidade.java
├── Endereco.java
├── Cliente.java               # getCidadeEntrega() — Lei de Demeter
├── ItemPedido.java
├── Pedido.java
├── IDesconto.java             # OCP
├── DescontoAluno.java         # OCP
├── DescontoProfessor.java     # OCP
├── DescontoFuncionario.java   # OCP
├── IPagamento.java            # ISP
├── IParcelavel.java           # ISP
├── IGeradorBoleto.java        # ISP
├── PagamentoCartao.java       # ISP, LSP
├── PagamentoPix.java          # ISP, LSP
├── PagamentoBoleto.java       # ISP
├── IEntrega.java              # LSP
├── Entrega.java               # LSP
├── EntregaRetiradaLoja.java   # LSP, composição
├── IPedidoRepository.java     # DIP, SRP
├── PedidoRepositoryImpl.java  # DIP, SRP
├── PedidoService.java         # SRP, OCP, DIP, composição
└── Main.java                  # DIP, Lei de Demeter
```

## Como executar

```bash
javac -d out src/projetoprincipiosdesign/*.java
java -cp out projetoprincipiosdesign.Main
```
