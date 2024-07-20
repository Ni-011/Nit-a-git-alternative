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
//        nit.commit("latest commmit");
//        nit.log();
        try {
            nit.displayCommitChanges("15084d11b7408cfced57d2152cf2b75df004258065c07184964334b6bd9d80d9");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}