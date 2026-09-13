package projetoprincipiosdesign;

// Segregação de Interfaces: a interface original tinha pagar, parcelar e gerarBoleto,
// mas Pix não pode parcelar e cartão não gera boleto. Isso forçava implementações
// falsas ou exceções. Agora cada interface representa uma capacidade real.
public interface IPagamento {
    void processar(double valor);
}
