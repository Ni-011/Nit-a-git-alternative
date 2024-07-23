import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String VERSION = "0.1.0";

    public static void main(String[] args) throws IOException {
        if (args.length == 0){
            printUsage();
            return;
        }

        Nit nit = new Nit();
        String command = args[0];
        switch (command) {
            case "version":
                System.out.println(VERSION);

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
                System.out.println(filePath);
                try {
                    nit.add(filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "commit":
                if (args.length < 2) {
                    System.out.println("Usage: commit <commit message>, Please enter the commit message");
                    return;
                }
                String message = args[1];
                nit.commit(message);
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
    private static void printUsage() {
        System.out.println("Usage: nit <command> [<args>]");
        System.out.println("Available commands:");
        System.out.println("  init                Initialize a new Nit repository");
        System.out.println("  add <file_path>     Add a file to the staging area");
        System.out.println("  commit <message>    Commit staged changes");
        System.out.println("  log                 Show commit log");
        System.out.println("  changes <commit_hash> Show changes in a specific commit");
        System.out.println("  help                Show this help message");
        System.out.println("  version             Show Nit version");
    }
}