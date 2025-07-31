import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceProcesso extends Remote {
    void receberMensagem(Mensagem msg) throws RemoteException;
    void iniciarCaptura() throws RemoteException;
    String getNome() throws RemoteException;
}