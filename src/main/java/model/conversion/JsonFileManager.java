package model.conversion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileManager {

    public static JSONObject readJsonObject(String filePath) throws IOException, JSONException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONObject(json);
    }

    public static void writeJsonObject(String filePath, JSONObject jsonObject) throws IOException {
        Files.write(Paths.get(filePath), jsonObject.toString().getBytes());
    }
}
