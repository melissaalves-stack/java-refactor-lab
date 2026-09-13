package projetoprincipiosdesign;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// Responsabilidade Única: toda a lógica de persistência ficou aqui,
// fora do PedidoService. Se um dia mudar de arquivo para banco de dados,
// só essa classe precisa ser alterada.
public class PedidoRepositoryImpl implements IPedidoRepository {
    @Override
    public void salvar(Pedido pedido, double total) {
        System.out.println("Salvando pedido em arquivo...");
        String linha = pedido.getCliente().getNome() + ";" + total + System.lineSeparator();

        try {
            Files.writeString(
                Path.of("pedidos.txt"),
                linha,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o pedido em arquivo.", e);
        }
    }
}
