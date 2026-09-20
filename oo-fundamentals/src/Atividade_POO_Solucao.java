import java.util.List;
import java.util.ArrayList;

// ===================== PARTE 3 — Interface =====================
interface IConsole {
    void ligar();
    double calcularPreco();
    String getNome();
}

// ===================== PARTE 3 — Composição =====================
class DadosConsole {
    private String nome;
    private double precoBase;

    public DadosConsole(String nome, double precoBase) {
        this.nome = nome;
        this.precoBase = precoBase;
    }

    public String getNome()      { return nome; }
    public double getPrecoBase() { return precoBase; }
}

// ===================== PARTE 3 — Nintendo =====================
class Nintendo implements IConsole {
    private DadosConsole dados;

    public Nintendo(String nome, double precoBase) {
        this.dados = new DadosConsole(nome, precoBase);
    }

    @Override
    public void ligar() {
        System.out.println("Nintendo ligado.");
    }

    @Override
    public double calcularPreco() {
        return dados.getPrecoBase() * 1.10; // 10%
    }

    @Override
    public String getNome() { return dados.getNome(); }
}

// ===================== PARTE 3 — Playstation =====================
class Playstation implements IConsole {
    protected DadosConsole dados; // protected: subclasse precisa acessar

    public Playstation(String nome, double precoBase) {
        this.dados = new DadosConsole(nome, precoBase);
    }

    @Override
    public void ligar() {
        System.out.println("Playstation ligado.");
    }

    @Override
    public double calcularPreco() {
        return dados.getPrecoBase() * 1.20; // 20%
    }

    @Override
    public String getNome() { return dados.getNome(); }
}

// ===================== PARTE 4 — Herança =====================
// PlaystationPortatil ESPECIALIZA Playstation sem quebrar nenhum contrato.
// Ao contrário do jogarDisco() visto no material SOLID que lançava
// UnsupportedOperationException (violação de LSP), aqui sobrescrevemos
// ligar() e calcularPreco() apenas para MUDAR como funcionam —
// a subclasse ainda cumpre tudo que IConsole promete.
class PlaystationPortatil extends Playstation {

    public PlaystationPortatil(String nome, double precoBase) {
        super(nome, precoBase);
    }

    @Override
    public void ligar() {
        System.out.println("Playstation Portátil ligado.");
    }

    @Override
    public double calcularPreco() {
        return dados.getPrecoBase() * 1.15; // 15% próprio
    }
}

// ===================== PARTE 5 — Xbox (extensão sem alterar Loja) =====================
// OCP na prática: Loja não precisou mudar uma linha para suportar Xbox.
class Xbox implements IConsole {
    private DadosConsole dados;

    public Xbox(String nome, double precoBase) {
        this.dados = new DadosConsole(nome, precoBase);
    }

    @Override
    public void ligar() {
        System.out.println("Xbox ligado.");
    }

    @Override
    public double calcularPreco() {
        return dados.getPrecoBase() * 1.18; // 18%
    }

    @Override
    public String getNome() { return dados.getNome(); }
}

// ===================== PARTE 5 — Loja com Polimorfismo =====================
class Loja {

    // Recebe IConsole — sem if/else, sem instanceof
    public void venderConsole(IConsole console) {
        console.ligar();
        System.out.println(console.getNome() + " -> Preço final: R$ " + console.calcularPreco());
    }

    public void venderVarios(List<IConsole> consoles) {
        for (IConsole console : consoles) {
            venderConsole(console);
        }
    }

    public double calcularFaturamentoTotal(List<IConsole> consoles) {
        double total = 0;
        for (IConsole console : consoles) {
            total += console.calcularPreco();
        }
        return total;
    }
}

// ===================== MAIN =====================
public class Atividade_POO_Solucao {

    public static void main(String[] args) {

        // Parte 2: construtor garante que todo objeto nasce em estado válido
        Nintendo nintendo             = new Nintendo("Nintendo Switch", 2000);
        Playstation playstation       = new Playstation("Playstation 5", 3000);
        PlaystationPortatil portatil  = new PlaystationPortatil("Playstation Portátil", 2500);

        List<IConsole> consoles = new ArrayList<>();
        consoles.add(nintendo);
        consoles.add(playstation);
        consoles.add(portatil);

        Loja loja = new Loja();

        System.out.println("=== Rodada 1 (sem Xbox) ===");
        loja.venderVarios(consoles);
        System.out.printf("Faturamento total: R$ %.2f%n%n", loja.calcularFaturamentoTotal(consoles));

        // Parte 5.4/5.5: Xbox entra SEM alterar nenhuma linha de Loja → OCP
        consoles.add(new Xbox("Xbox Series X", 3500));

        System.out.println("=== Rodada 2 (com Xbox) ===");
        loja.venderVarios(consoles);
        System.out.printf("Faturamento total: R$ %.2f%n", loja.calcularFaturamentoTotal(consoles));
    }
}
