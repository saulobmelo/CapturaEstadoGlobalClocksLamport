public class Registro {
    public static synchronized void log(String processo, String evento, int clock) {
        System.out.println("[" + processo + "] " + evento + " | Clock: " + clock);
    }
}