package network;

public class StaticMethods {
    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.println("\033c");
            }
            System.out.println("Hint: type 'help' for a list of commands you can do ;)");
        } catch (final Exception e) {
            System.out.println("Warning: failed to clear console");
        }
    }
}
