import com.google.gson.annotations.Expose;

import java.nio.file.Path;

public class FileEntry {
    @Expose
    private String filePath;
    @Expose
    private String fileHash;

    public FileEntry (String filePath, String fileHash) {
        this.filePath = filePath;
        this.fileHash = fileHash;
    }

    public String getFilePath () {
        return this.filePath;
    }

    public void setFilePath (String filePath) {
        this.filePath = filePath;
    }

    public String getFileHash () {
        return this.fileHash;
    }

    public void setFileHash (String fileHash) {
        this.fileHash = fileHash;
    }
}
