package network;

import model.Color;

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
            System.out.println("\n" + Color.YELLOW_LIGHT_BG + Color.GREY_DARK_FG + "Hint:" + Color.RESET + " type '" + Color.RESOURCE_STD + "help" + Color.RESET + "' for a list of commands you can do ;)" + "\n");
        } catch (final Exception e) {
            System.out.println("Warning: failed to clear console");
        }
    }
}
