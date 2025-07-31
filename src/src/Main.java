import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            Processo p1 = new Processo("P1");
            Processo p2 = new Processo("P2");
            Processo p3 = new Processo("P3");

            registry.rebind("P1", p1);
            registry.rebind("P2", p2);
            registry.rebind("P3", p3);

            p1.adicionarVizinho(p2);
            p1.adicionarVizinho(p3);

            p2.adicionarVizinho(p1);
            p2.adicionarVizinho(p3);

            p3.adicionarVizinho(p1);
            p3.adicionarVizinho(p2);

            p1.simularEventos();
            p2.simularEventos();
            p3.simularEventos();

            // Inicia captura de estado após 10 segundos
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    p1.iniciarCaptura();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Encerra execução após 30 segundos
            new Thread(() -> {
                try {
                    Thread.sleep(30000);
                    System.out.println("Encerrando execução após 30 segundos...");
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}