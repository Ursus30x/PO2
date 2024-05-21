package projekt2;

public class Logger {
    private static String log = "";

    public static void addLog(String entry){ log += entry + "\n"; }
    public static String getLog(){ return log; }
    public static void clearLog(){ log = ""; }
}
