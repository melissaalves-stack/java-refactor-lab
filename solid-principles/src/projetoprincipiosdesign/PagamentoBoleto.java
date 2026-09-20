package projetoprincipiosdesign;

// Boleto processa pagamento e pode gerar a linha digitável, mas não parcela.
public class PagamentoBoleto implements IPagamento, IGeradorBoleto {
    @Override
    public void processar(double valor) {
        System.out.printf("Boleto registrado: R$ %.2f%n", valor);
    }

    @Override
    public void gerarBoleto(double valor) {
        System.out.printf("Linha digitável gerada para R$ %.2f%n", valor);
    }
}
