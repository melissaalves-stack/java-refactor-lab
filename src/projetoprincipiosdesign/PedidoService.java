package projetoprincipiosdesign;

// Composição no lugar de herança: antes PedidoService estendia PagamentoCartao,
// o que não faz sentido (um serviço de pedido não "é um" tipo de pagamento).
// Agora ele recebe colaboradores por composição.
public class PedidoService {

    private final IPedidoRepository repository;

    // Inversão de Dependência: o repositório é injetado, não instanciado aqui dentro.
    // PedidoService conhece apenas a interface, não a implementação concreta.
    public PedidoService(IPedidoRepository repository) {
        this.repository = repository;
    }

    // Aberto/Fechado: o desconto chega como interface IDesconto.
    // Para adicionar um novo tipo de desconto basta criar uma nova classe,
    // sem precisar editar esse método.
    public double calcularTotal(Pedido pedido, IDesconto desconto) {
        double subtotal = 0.0;

        for (ItemPedido item : pedido.getItens()) {
            subtotal += item.getPreco() * item.getQuantidade();
        }

        return desconto.aplicar(subtotal);
    }

    // Responsabilidade Única: finalizarPedido agora orquestra etapas,
    // cada uma delegada a um colaborador especializado.
    // A forma de pagamento e o desconto chegam prontos como parâmetros,
    // então esse método não precisa mais de if/else para decidir qual usar.
    public void finalizarPedido(Pedido pedido, IDesconto desconto, IPagamento pagamento) {
        double total = calcularTotal(pedido, desconto);

        repository.salvar(pedido, total);

        System.out.println("Gerando resumo do pedido...");
        System.out.println("Cliente: " + pedido.getCliente().getNome());
        System.out.printf("Total: R$ %.2f%n", total);

        pagamento.processar(total);

        System.out.println("Enviando mensagem para " + pedido.getCliente().getNome() + ": pedido finalizado.");
    }
}
