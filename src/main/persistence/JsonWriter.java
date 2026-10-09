package persistence;

import model.MusicLibrary;
import org.json.JSONObject;
import java.io.*;

/* Citations: Modelled based on Sample Application, 
 * https://github.com/stleary/JSON-java.git
 */

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String location;

    // EFFECTS: constructs a writer to be able to write to the specified file location
    public JsonWriter(String location) {
        this.location = location;
    }

    // MODIFIES: this
    // EFFECTS: opens the writer and throws FileNotFoundException if the file cannot be opened
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(location));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of music library to file
    public void write(MusicLibrary ml) {
        JSONObject json = ml.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes given a specific JSON string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}