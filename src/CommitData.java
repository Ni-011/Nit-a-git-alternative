import com.google.gson.annotations.Expose;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;

public class CommitData {
    @Expose
    private String timeStamp;
    @Expose
    private String message;
    @Expose
    private List<FileEntry> files;
    @Expose
    private String parentCommit;

    public CommitData (String timeStamp, String message, List<FileEntry> files, String parentCommit) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.files = files;
        this.parentCommit = parentCommit;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FileEntry> getFiles() {
        return files;
    }

    public void setFiles(List<FileEntry> files) {
        this.files = files;
    }

    public String getParentCommit() {
        return parentCommit;
    }

    public void setParentCommit(String parentCommit) {
        this.parentCommit = parentCommit;
    }
}
