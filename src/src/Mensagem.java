import java.io.Serializable;

public class Mensagem implements Serializable {
    public final String origem;
    public final int timestamp;
    public final String conteudo;

    public Mensagem(String origem, int timestamp, String conteudo) {
        this.origem = origem;
        this.timestamp = timestamp;
        this.conteudo = conteudo;
    }

    public String toString() {
        return "[" + timestamp + "] de " + origem + ": " + conteudo;
    }
}
