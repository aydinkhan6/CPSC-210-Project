package persistence;

import model.MusicLibrary;
import model.Playlist;
import model.Song;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.json.*;

/* Citations: Modelled based on Sample Application, 
 * https://github.com/stleary/JSON-java.git
 */

public class JsonReader {
    private String root;

    // EFFECTS: constructs a reader to read from the given root file
    public JsonReader(String root) {
        this.root = root;
    }

    // EFFECTS: reads music library from file and returns it;
    // throws IOException if an error occurs while reading data
    public MusicLibrary read() throws IOException {
        String jsonData = readFile(root);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMusicLibrary(jsonObject);
    }

    // EFFECTS: reads the source file as string and returns it bacl
    private String readFile(String root) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(root), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses music library from JSON object and returns it
    private MusicLibrary parseMusicLibrary(JSONObject jsonObject) {
        MusicLibrary ml = new MusicLibrary();
        addPlaylists(ml, jsonObject);
        return ml;
    }

    // MODIFIES: ml
    // EFFECTS: examines playlists from JSON and adds them to the music library
    private void addPlaylists(MusicLibrary ml, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("playlists");
        for (Object json : jsonArray) {
            JSONObject nextPlaylist = (JSONObject) json;
            addPlaylist(ml, nextPlaylist);
        }
    }

    // MODIFIES: ml
    // EFFECTS: inspects a single playlist from JSON and adds it to the music
    // library
    private void addPlaylist(MusicLibrary ml, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Playlist playlist = new Playlist(name);
        JSONArray songsArray = jsonObject.getJSONArray("songs");
        for (Object json : songsArray) {
            JSONObject songJson = (JSONObject) json;
            Song song = new Song(
                    songJson.getString("title"),
                    songJson.getString("artist"),
                    songJson.getInt("duration"));
            playlist.addSong(song);
        }
        ml.addPlaylist(playlist);
    }
}
