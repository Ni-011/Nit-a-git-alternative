import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileLineDiffAlgo {
    public static void lineDiff (String file1, String file2) {
        List<String> file1Lines = Arrays.asList(file1.split("\n"));
        List<String> file2Lines = Arrays.asList(file1.split("\n"));

        // ANSI escape codes for colors
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RED = "\u001B[31m";

        int file1LineCount = file1Lines.size();
        int file2LineCount = file2Lines.size();

        if (file1LineCount > file2LineCount) {
            System.out.println("Lines added: " + (file1LineCount - file2LineCount));
        } else if (file2LineCount > file1LineCount) {
            System.out.println("Lines removed: " + (file2LineCount - file1LineCount));
        } else {
            System.out.println("No changes");
        }

        // compare each line
        List<String> addedLines = new ArrayList<>();
        List<String> removedLines = new ArrayList<>();

        for (String line : file1Lines) {
            if (!file2Lines.contains(line)) {
                addedLines.add(line);
            }
        }

        for (String line : file2Lines) {
            if (!file1Lines.contains(line)) {
                removedLines.add(line);
            }
        }

        // Print added lines
        if (!addedLines.isEmpty()) {
            System.out.println("Added:");
            for (String line : addedLines) {
                System.out.println(ANSI_GREEN + "+ " + line);
            }
            System.out.println();
        }

        // Print removed lines
        if (!removedLines.isEmpty()) {
            System.out.println("Removed:");
            for (String line : removedLines) {
                System.out.println(ANSI_RED + "- " + line);
            }
            System.out.println();
        }

        // If no changes, print a message
        if (addedLines.isEmpty() && removedLines.isEmpty()) {
            System.out.println("No changes between the commits.");
        }
    }
}
