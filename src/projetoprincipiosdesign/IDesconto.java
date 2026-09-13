package projetoprincipiosdesign;

// Aberto/Fechado: para adicionar um novo tipo de desconto basta criar
// uma nova classe que implemente essa interface, sem mexer no PedidoService.
public interface IDesconto {
    double aplicar(double valor);
}
