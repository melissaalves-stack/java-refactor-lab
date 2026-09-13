package projetoprincipiosdesign;

public class Entrega implements IEntrega {
    @Override
    public boolean disponivelPara(double total) {
        return true;
    }

    @Override
    public double calcularFrete(double total) {
        return 15.0;
    }
}
