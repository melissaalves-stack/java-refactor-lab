package projetoprincipiosdesign;

// Cartão suporta pagamento e parcelamento, mas não gera boleto.
// Agora implementa apenas o que realmente faz sentido para ele.
public class PagamentoCartao implements IPagamento, IParcelavel {
    @Override
    public void processar(double valor) {
        System.out.printf("Pagamento no cartão: R$ %.2f%n", valor);
    }

    @Override
    public void parcelar(double valor, int parcelas) {
        System.out.printf("Cartão parcelado em %dx de R$ %.2f%n", parcelas, valor / parcelas);
    }
}
