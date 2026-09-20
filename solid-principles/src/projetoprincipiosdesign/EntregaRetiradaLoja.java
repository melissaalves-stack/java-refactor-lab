package projetoprincipiosdesign;

// Antes essa classe herdava de Entrega e lançava exceção em calcularFrete
// quando o pedido era pequeno, o que quebra a substituição de Liskov.
// Agora ela implementa a interface IEntrega diretamente (composição no lugar
// de herança) e usa disponivelPara para comunicar a restrição sem exceções.
public class EntregaRetiradaLoja implements IEntrega {
    @Override
    public boolean disponivelPara(double total) {
        return total >= 50.0;
    }

    @Override
    public double calcularFrete(double total) {
        return 0.0;
    }
}
