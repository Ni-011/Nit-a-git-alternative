import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        this.gson = new GsonBuilder().setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();;
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
}
