package projetoprincipiosdesign;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== LOJA ACADÊMICA ===");

        Cliente cliente = new Cliente(
            "Ana",
            new Endereco(
                "Rua das Flores",
                new Cidade("Belo Horizonte")
            )
        );

        Pedido pedido = new Pedido(
            cliente,
            List.of(
                new ItemPedido("Livro de Engenharia de Software", 120.0, 1),
                new ItemPedido("Caderno", 20.0, 2)
            )
        );

        // Inversão de Dependência: o Main é o único lugar que conhece
        // as implementações concretas. O PedidoService só vê interfaces.
        IPedidoRepository repository = new PedidoRepositoryImpl();
        PedidoService servico = new PedidoService(repository);

        // Lei de Demeter: a cidade de entrega vem direto do cliente,
        // que é quem conhece sua estrutura interna. Antes era
        // pedido.getCliente().getEndereco().getCidade().getNome().
        System.out.println();
        System.out.println("Cidade de entrega:");
        System.out.println(cliente.getCidadeEntrega());

        // Aberto/Fechado: o desconto é escolhido aqui e passado para o serviço.
        IDesconto desconto = new DescontoAluno();

        System.out.println();
        System.out.println("Total com desconto:");
        System.out.printf("R$ %.2f%n", servico.calcularTotal(pedido, desconto));

        // A forma de pagamento também é escolhida aqui, não dentro do serviço.
        IPagamento pagamento = new PagamentoCartao();

        System.out.println();
        System.out.println("Pagamento:");
        servico.finalizarPedido(pedido, desconto, pagamento);

        // Exemplo de verificação de disponibilidade de entrega (LSP).
        IEntrega entrega = new EntregaRetiradaLoja();
        double totalPedido = servico.calcularTotal(pedido, desconto);
        if (entrega.disponivelPara(totalPedido)) {
            System.out.printf("Frete retirada na loja: R$ %.2f%n", entrega.calcularFrete(totalPedido));
        } else {
            System.out.println("Retirada na loja não disponível para este pedido.");
        }

        System.out.println();
        System.out.println("Programa executado com sucesso.");
    }
}
