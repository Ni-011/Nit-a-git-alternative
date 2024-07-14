import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

public class CommitDataTypeAdapter extends TypeAdapter<CommitData> {
    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(new TypeToken<List<FileEntry>>(){}.getType(), new FileEntryListTypeAdapter())
            .create();

    @Override
    public void write(JsonWriter out, CommitData value) throws IOException {
        out.beginObject();
        out.name("timeStamp").value(value.getTimeStamp());
        out.name("message").value(value.getMessage());
        out.name("files");
        gson.toJson(value.getFiles(), new TypeToken<List<FileEntry>>(){}.getType(), out);
        out.name("parentCommit").value(value.getParentCommit());
        out.endObject();
    }

    @Override
    public CommitData read(JsonReader in) throws IOException {
        // Implement deserialization if needed
        return null;
    }
}