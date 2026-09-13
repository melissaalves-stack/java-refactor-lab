package projetoprincipiosdesign;

public class DescontoFuncionario implements IDesconto {
    @Override
    public double aplicar(double valor) {
        return valor * 0.80;
    }
}
