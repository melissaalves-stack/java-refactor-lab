package projetoprincipiosdesign;

// Substituição de Liskov: EntregaRetiradaLoja lançava exceção dentro de calcularFrete
// quando o total era baixo, quebrando o contrato da classe pai Entrega.
// A solução foi criar uma interface com um método próprio para verificar disponibilidade,
// assim nenhuma implementação precisa lançar exceção inesperadamente.
public interface IEntrega {
    boolean disponivelPara(double total);
    double calcularFrete(double total);
}
