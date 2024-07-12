import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Nit {
    private final Path ParentDirPath;
    private final Path ObjectsDirPath;
    private final Path HeadFilePath;
    private final Path IndexPath;
    private final Gson gson;

    // get all the paths for directories and paths
    public Nit (String ParentDirPath) {
        this.ParentDirPath = Paths.get(ParentDirPath, ".Nit");
        this.ObjectsDirPath = this.ParentDirPath.resolve("Objects");
        this.HeadFilePath = this.ParentDirPath.resolve("Head");
        this.IndexPath= this.ParentDirPath.resolve("Index");
        Type fileEntryListType = new TypeToken<List<FileEntry>>(){}.getType();
        this.gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(fileEntryListType, new FileEntryListTypeAdapter())
                .create();
    }

    // overloading constructor to run constructor with default param
    public Nit () {
        this(".");
    }

    // make all the dir and files from path
    public void init () throws IOException {
        try {
            Files.createDirectories(ParentDirPath);
        } catch (FileAlreadyExistsException e) {
            System.out.println("The .Nit directory already exists" + e);
        }

        try {
            Files.createDirectories(ObjectsDirPath);
        } catch (FileAlreadyExistsException e) {
            System.out.println("The Objects directory already exists" + e);
        }

        try {
            Files.createFile(HeadFilePath);
            Files.writeString(IndexPath, "[]");
        } catch (FileAlreadyExistsException e) {
            System.out.println("The Files already exists" + e);
        }
    }

    // make hash of input using sha 256
    public String makeHash (String data) {
        try {
            // get the algo
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // convert input data into bytes and hash them into a hashed byte array
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            // final string to be returned
            StringBuilder hexString = new StringBuilder();
            // for every byte in hash convert into hexstring then add hexstring into stringbuilder
            for (byte b: hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // add the files
    public void add (String fileToAdd) throws IOException {
        Path filePath = Paths.get(fileToAdd);
        try {
            // read the file and hash its content
            String fileData = Files.readString(filePath, StandardCharsets.UTF_8);
            String fileHash = this.makeHash(fileData);
            // Gte the first 2 characters of hash and make sub folder in Objects with that name
            String objectSubFolderName = this.makeHash(fileData).substring(0, 2);
            Path objectsSubDirPath = this.ObjectsDirPath.resolve(objectSubFolderName);
            Files.createDirectories(objectsSubDirPath);
            // Add file inside that sub folder
            Path fileToAddPathInObjectsSubDir = objectsSubDirPath.resolve(fileHash);
            Files.createFile(fileToAddPathInObjectsSubDir);
            // updating the staging area in index file
            this.StagingArea(fileToAddPathInObjectsSubDir, fileHash);
            System.out.println("The file was successfully added");
        } catch (IOException e) {
            throw new RuntimeException("Could not Read File" + e);
        }
    }

    public void StagingArea (Path addedFilePath, String addedFileHash) {
        try {
            // reading the string inside index file and convert to List<String>
            String jsonInFile = Files.readString(this.IndexPath);
            List<FileEntry> StagingArray = gson.fromJson(jsonInFile, new TypeToken<List<FileEntry>>(){}.getType());
            // update the stagingArea with info about file and convert stagingarea back to json and add it back to StringFile
            StagingArray.add(new FileEntry(addedFilePath.toString(), addedFileHash));
            String newJson = gson.toJson(StagingArray);
            Files.writeString(this.IndexPath, newJson);
            System.out.println("Staging area updated successfully");
        } catch (IOException e) {
            System.out.println("Cannot read staging area Index File " + e);
        }
    }

    public String getCurrentHeadState () {
        try {
            return Files.readString(this.HeadFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Could not read file " + e);
            return null;
        }
    }

    public void commit (String message) {
        try {
            // read the data in index file (staging area)
            String indexDataJson = Files.readString(this.IndexPath, StandardCharsets.UTF_8);
            List<FileEntry> stagingArray = gson.fromJson(indexDataJson, new TypeToken<List<FileEntry>>(){}.getType());
            // get the previous parent commit from head
            String lastCommit = this.getCurrentHeadState();
            // commit data
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date currentTime = new Date();
            CommitData commitData = new CommitData(formatter.format(currentTime), message, stagingArray, lastCommit);
            // convert commit data into string then hash it
            String commitDataJson = gson.toJson(commitData);
            String commitDataHash = this.makeHash(commitDataJson);
            // create the commits folder inside objects with commit data in it
            Path commitsDirPath = this.ObjectsDirPath.resolve("commits");
            Files.createDirectories(commitsDirPath);
            Path commitFilePath = commitsDirPath.resolve(commitDataHash);
            Files.writeString(commitFilePath, commitDataJson);
            // update Head file with new commit's hash and clear staging area
            Files.writeString(this.HeadFilePath, commitDataHash);
            Files.writeString(this.IndexPath, "[]");
            System.out.println("Successfully committed, message: " + message);
            System.out.println("Working Directory Clean");
        } catch (IOException e) {
            System.out.println("Could not read file " + e);
        }
    }

    public void log () {
        String currentCommitHash = this.getCurrentHeadState();
        while (currentCommitHash != null && !currentCommitHash.isEmpty()) {
            Path currentCommitDataFilePath = this.ObjectsDirPath.resolve("commits").resolve(currentCommitHash);
            try {
                String currentCommitDataString = Files.readString(currentCommitDataFilePath, StandardCharsets.UTF_8);
                CommitData currentCommitData = gson.fromJson(currentCommitDataString, new TypeToken<CommitData>(){}.getType());

                // display info of the current commit
                System.out.println("commit " + currentCommitHash);
                System.out.println("Date: " + currentCommitData.getTimeStamp());
                System.out.println();
                System.out.println(currentCommitData.getMessage());
                System.out.println();
                // change the currentcommit to parent commit
                currentCommitHash = currentCommitData.getParentCommit();
            } catch (IOException e) {
                System.out.println("cannot read commitDataFile " + e);
            }
        }
    }

    public void displayCommitChanges(String commitHash) throws IOException {
        CommitData commitData = gson.fromJson(getCommitDataJson(commitHash), new TypeToken<CommitData>(){}.getType());
        System.out.println("Changes in the Latest commit: ");

        for (FileEntry file : commitData.getFiles()) {
            System.out.println("File: " + file.getFilePath());
            String fileData = this.getFileData(file.getFileHash());
            System.out.println();

            if (commitData.getParentCommit() != null) {
                String parentCommitDataJson = this.getCommitDataJson(commitData.getParentCommit());
                CommitData parentCommitData = gson.fromJson(parentCommitDataJson, new TypeToken<CommitData>(){}.getType());
                String parentFileData = this.getParentFileDataJson(parentCommitData, Paths.get(file.getFilePath()));
                FileLineDiffAlgo.lineDiff(fileData, parentFileData);
            }
        }
    }

    public String getParentFileDataJson (CommitData parentCommitData, Path filePath) {
        // find if a file in parentcommitData matches filepath and gets its file content from parent commit and return content
        for (FileEntry file : parentCommitData.getFiles()) {
            if (Paths.get(file.getFilePath()) == filePath) {
                return this.getFileData(file.getFileHash());
            }
        }
        return null;
    }

    public String getCommitDataJson (String commitHash) {
        Path commitPath = this.ObjectsDirPath.resolve("commits").resolve(commitHash);
        try {
            return Files.readString(commitPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not read the file " + e);
        }
    }

    public String getFileData (String fileHash) {
        Path fileHashPath = this.ObjectsDirPath.resolve(fileHash.substring(0,2)).resolve(fileHash);
        try {
            return Files.readString(fileHashPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file " + e);
        }
    }
}
