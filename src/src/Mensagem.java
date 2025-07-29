import java.io.Serializable;

public class Mensagem implements Serializable {
    public enum Tipo { INTERNO, ENVIO, RECEBIMENTO, MARCADOR }

    private final Tipo tipo;
    private final String origem;
    private final String destino;
    private final int timestamp;
    private final String conteudo;

    public Mensagem(Tipo tipo, String origem, String destino, int timestamp, String conteudo) {
        this.tipo = tipo;
        this.origem = origem;
        this.destino = destino;
        this.timestamp = timestamp;
        this.conteudo = conteudo;
    }

    public Tipo getTipo() { return tipo; }
    public String getOrigem() { return origem; }
    public String getDestino() { return destino; }
    public int getTimestamp() { return timestamp; }
    public String getConteudo() { return conteudo; }
}