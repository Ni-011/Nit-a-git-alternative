import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        /*if (args.length == 0){
            System.out.println("Usage: Nit <command>");
            return;
        }

        String command = args[0];
        Nit nit = new Nit();

        switch (command) {
            case "init":
                try {
                    nit.init();
                } catch (IOException e) {
                    System.out.println("Error initialising repository: " + e.getMessage());
                }
                break;

            default:
                System.out.println("unknown command " + command);
        }*/

        Nit nit = new Nit();
//        try {
//            nit.init();
//        } catch (IOException e) {
//            System.out.println("The file already exists");
//        }
//
//        try {
//            nit.add("\\\\wsl$\\Ubuntu\\home\\ni\\projects\\Nit_JavaVersion\\src\\test.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        nit.commit("new commmit");
//        nit.log();
        try {
            nit.displayCommitChanges("b6a61fce3c6585daec81ad933aef046c3f3007e4bf8316b87707b2a8ba5a6e3a");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}