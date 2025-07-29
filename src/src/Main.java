import java.net.InetSocketAddress;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        InetSocketAddress p1 = new InetSocketAddress("localhost", 5001);
        InetSocketAddress p2 = new InetSocketAddress("localhost", 5002);
        InetSocketAddress p3 = new InetSocketAddress("localhost", 5003);

        Processo proc1 = new Processo("P1", 5001, List.of(p2, p3));
        Processo proc2 = new Processo("P2", 5002, List.of(p1, p3));
        Processo proc3 = new Processo("P3", 5003, List.of(p1, p2));

        new Thread(proc1::iniciar).start();
        new Thread(proc2::iniciar).start();
        new Thread(proc3::iniciar).start();
    }
}