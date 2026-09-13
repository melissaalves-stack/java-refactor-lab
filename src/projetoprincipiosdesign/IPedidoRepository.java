package projetoprincipiosdesign;

// Inversão de Dependência: PedidoService depende dessa abstração,
// não da implementação concreta que grava em arquivo.
public interface IPedidoRepository {
    void salvar(Pedido pedido, double total);
}
