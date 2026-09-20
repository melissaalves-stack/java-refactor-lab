package projetoprincipiosdesign;

// Pix só processa pagamento. Não parcela, não gera boleto.
// Com a interface segregada, não precisa mais lançar UnsupportedOperationException.
public class PagamentoPix implements IPagamento {
    @Override
    public void processar(double valor) {
        System.out.printf("PIX pago: R$ %.2f%n", valor);
    }
}
