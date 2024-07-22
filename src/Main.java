import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0){
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

            case "add":
                if (args.length < 2) {
                    System.out.println("Usage: add <file_path>, please enter the file name you want to add");
                    return;
                }
                String filePath = args[1];
                try {
                    nit.add(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "log":
                nit.log();
                break;

            case "changes":
                if (args.length < 2) {
                    System.out.println("Usage: changes <commit Hash>, please enter the commit hash you wanna see changes of");
                    return;
                }
                String commitHash = args[1];
                try {
                    nit.displayCommitChanges(commitHash);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            default:
                System.out.println("unknown command " + command);
        }
    }
}