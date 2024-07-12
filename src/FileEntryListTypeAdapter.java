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
            object.addProperty("path", entry.getFilePath());
            object.addProperty("hash", entry.getFileHash());
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
                    String path = object.has("path") ? object.get("path").getAsString() : "";
                    String hash = object.has("hash") ? object.get("hash").getAsString() : "";
                    fileEntries.add(new FileEntry(path, hash));
                }
            }
        }
        return fileEntries;
    }
}