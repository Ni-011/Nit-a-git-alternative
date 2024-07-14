import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileEntryListTypeAdapter implements JsonSerializer<List<FileEntry>>, JsonDeserializer<List<FileEntry>> {
    @Override
    public JsonElement serialize(List<FileEntry> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        for (FileEntry entry : src) {
            JsonObject object = new JsonObject();
            object.addProperty("filePath", entry.getFilePath());
            object.addProperty("fileHash", entry.getFileHash());
            array.add(object);
        }
        return array;
    }

    @Override
    public List<FileEntry> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<FileEntry> fileEntries = new ArrayList<>();
        if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            for (JsonElement element : array) {
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    String path = object.has("filePath") ? object.get("filePath").getAsString() : "";
                    String hash = object.has("fileHash") ? object.get("fileHash").getAsString() : "";
                    fileEntries.add(new FileEntry(path, hash));
                }
            }
        }
        return fileEntries;
    }
}