import java.io.*;
import java.net.*;
import java.util.*;

public class Processo {
    private final String id;
    private final int porta;
    private final List<InetSocketAddress> vizinhos;
    private final LamportClock clock;
    private boolean emSnapshot = false;
    private int estadoLocal = 0;
    private final Map<InetSocketAddress, Boolean> canalRegistrado = new HashMap<>();
    private final List<String> mensagensEmTransito = new ArrayList<>();
    private final String logPath;
    private volatile boolean executando = true;

    public Processo(String id, int porta, List<InetSocketAddress> vizinhos) {
        this.id = id;
        this.porta = porta;
        this.vizinhos = vizinhos;
        this.clock = new LamportClock();
        this.logPath = "log_" + id + ".txt";
        for (InetSocketAddress vizinho : vizinhos) {
            canalRegistrado.put(vizinho, false);
        }
    }

    public void iniciar() {
        Thread servidor = new Thread(this::receber);
        Thread eventos = new Thread(this::gerarEventos);

        servidor.start();
        eventos.start();

        // Forçar snapshot no P1 após 15 segundos
        if (id.equals("P1")) {
            new Thread(() -> {
                try {
                    Thread.sleep(15000);
                    if (!emSnapshot) {
                        emSnapshot = true;
                        registrarLog("[" + id + "] Iniciando snapshot forçado pelo sistema.");
                        System.out.println("[" + id + "] Iniciando snapshot forçado pelo sistema.");
                        registrarLog("[" + id + "] Estado local: " + estadoLocal);
                        enviarMarcadores();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Encerrar o processo automaticamente após 1 minuto
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                executando = false;
                registrarLog("[" + id + "] Encerrando processo automaticamente.");
                System.out.println("[" + id + "] Encerrando processo automaticamente.");
                try {
                    servidor.join(1000);
                    eventos.join(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        }, 60000); // 60 segundos
    }

    private void registrarLog(String msg) {
        try (PrintWriter log = new PrintWriter(new FileWriter(logPath, true))) {
            log.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receber() {
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            while (executando) {
                Socket socket = serverSocket.accept();
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                Mensagem mensagem = (Mensagem) entrada.readObject();

                // Identificar o vizinho real pela porta na string de origem
                String origemStr = mensagem.getOrigem();
                InetSocketAddress origem = null;
                for (InetSocketAddress vizinho : vizinhos) {
                    if (origemStr.contains(String.valueOf(vizinho.getPort()))) {
                        origem = vizinho;
                        break;
                    }
                }

                if (mensagem.getTipo() == Mensagem.Tipo.MARCADOR) {
                    if (!emSnapshot) {
                        emSnapshot = true;
                        System.out.println("[" + id + "] Iniciando captura de estado.");
                        registrarLog("[" + id + "] Estado local: " + estadoLocal);
                        enviarMarcadores();
                    }
                    if (origem != null) canalRegistrado.put(origem, true);
                    registrarLog("[" + id + "] Marcador recebido de " + mensagem.getOrigem());
                } else {
                    clock.update(mensagem.getTimestamp());
                    String logMsg = "[" + id + "] Recebido de " + mensagem.getOrigem() + " [" + clock.getTime() + "] -> " + mensagem.getConteudo();
                    System.out.println(logMsg);
                    registrarLog(logMsg);

                    if (emSnapshot && origem != null && !canalRegistrado.getOrDefault(origem, true)) {
                        mensagensEmTransito.add(mensagem.getConteudo());
                        registrarLog("[" + id + "] (Em trânsito de " + origem + ") " + mensagem.getConteudo());
                    }
                }

                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            if (executando) e.printStackTrace();
        }
    }

    private void gerarEventos() {
        Random random = new Random();
        while (executando) {
            try {
                Thread.sleep(3000 + random.nextInt(2000));
                int tipo = random.nextInt(2);

                if (tipo == 0) { // evento interno
                    clock.increment();
                    estadoLocal++;
                    String logMsg = "[" + id + "] Evento interno. Clock: " + clock.getTime();
                    System.out.println(logMsg);
                    registrarLog(logMsg);
                } else { // envio de mensagem
                    InetSocketAddress destino = vizinhos.get(random.nextInt(vizinhos.size()));
                    enviarMensagem(destino, "Mensagem de " + id);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviarMensagem(InetSocketAddress destino, String conteudo) {
        try (Socket socket = new Socket(destino.getAddress(), destino.getPort())) {
            clock.increment();
            Mensagem msg = new Mensagem(Mensagem.Tipo.ENVIO, id, destino.toString(), clock.getTime(), conteudo);
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            saida.writeObject(msg);
            String logMsg = "[" + id + "] Enviado para " + destino + " [" + clock.getTime() + "] -> " + conteudo;
            System.out.println(logMsg);
            registrarLog(logMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarMarcadores() {
        for (InetSocketAddress destino : vizinhos) {
            try (Socket socket = new Socket(destino.getAddress(), destino.getPort())) {
                clock.increment();
                Mensagem marcador = new Mensagem(Mensagem.Tipo.MARCADOR, id, destino.toString(), clock.getTime(), "SNAPSHOT");
                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                saida.writeObject(marcador);
                String logMsg = "[" + id + "] Marcador enviado para " + destino;
                System.out.println(logMsg);
                registrarLog(logMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}