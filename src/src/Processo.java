import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;

public class Processo extends UnicastRemoteObject implements InterfaceProcesso {
    private final String nome;
    private int clock = 0;
    private final List<InterfaceProcesso> vizinhos = new ArrayList<>();
    private final List<Mensagem> mensagensEmTransito = new ArrayList<>();
    private boolean capturando = false;
    private Estado estadoLocal;

    public Processo(String nome) throws RemoteException {
        this.nome = nome;
    }

    public void adicionarVizinho(InterfaceProcesso vizinho) {
        this.vizinhos.add(vizinho);
    }

    public synchronized void eventoInterno() {
        clock++;
        Registro.log(nome, "Evento interno", clock);
    }

    public synchronized void enviarMensagem(String conteudo) throws RemoteException {
        clock++;
        Mensagem msg = new Mensagem(nome, clock, conteudo);
        for (InterfaceProcesso vizinho : vizinhos) {
            vizinho.receberMensagem(msg);
        }
        Registro.log(nome, "Enviou mensagem: " + conteudo, clock);
    }

    @Override
    public synchronized void receberMensagem(Mensagem msg) throws RemoteException {
        clock = Math.max(clock, msg.timestamp) + 1;
        Registro.log(nome, "Recebeu " + msg.toString(), clock);
        if (capturando && estadoLocal != null) {
            mensagensEmTransito.add(msg);
        }
    }

    @Override
    public synchronized void iniciarCaptura() throws RemoteException {
        if (capturando) return; // Impede iniciar captura mais de uma vez

        capturando = true;
        estadoLocal = new Estado(clock, new ArrayList<>(mensagensEmTransito));
        Registro.log(nome, "Captura iniciada!", clock);

        for (InterfaceProcesso vizinho : vizinhos) {
            try {
                vizinho.iniciarCaptura();
            } catch (Exception e) {
                Registro.log(nome, "Erro ao iniciar captura em " + vizinho.getNome(), clock);
            }
        }
    }

    @Override
    public String getNome() {
        return nome;
    }

    public void simularEventos() {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(new Random().nextInt(3000) + 1000);
                    int tipo = new Random().nextInt(3);
                    if (tipo == 0) eventoInterno();
                    else enviarMensagem("Ping");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}