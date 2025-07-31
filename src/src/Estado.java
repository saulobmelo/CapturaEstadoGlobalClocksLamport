import java.io.Serializable;
import java.util.List;

public class Estado implements Serializable {
    public int clock;
    public List<Mensagem> mensagensEmTransito;

    public Estado(int clock, List<Mensagem> mensagens) {
        this.clock = clock;
        this.mensagensEmTransito = mensagens;
    }
}