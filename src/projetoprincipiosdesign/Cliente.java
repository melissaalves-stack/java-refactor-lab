package projetoprincipiosdesign;

public class Cliente {
    private String nome;
    private Endereco endereco;

    public Cliente(String nome, Endereco endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    // Lei de Demeter: quem conhece a estrutura interna do endereço é o próprio Cliente.
    // Em vez de navegar pedido.getCliente().getEndereco().getCidade().getNome() de fora,
    // a lógica fica encapsulada aqui.
    public String getCidadeEntrega() {
        return endereco.getCidade().getNome();
    }
}
