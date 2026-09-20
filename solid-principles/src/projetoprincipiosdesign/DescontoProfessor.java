package projetoprincipiosdesign;

public class DescontoProfessor implements IDesconto {
    @Override
    public double aplicar(double valor) {
        return valor * 0.85;
    }
}
