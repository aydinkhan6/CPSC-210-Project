package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

//Represents a music library containing multiple playlists
public class MusicLibrary implements Writable {
    private List<Playlist> playlists;

    // MODIFIES: this
    // EFFECTS: creates an empty music library with no playlists
    public MusicLibrary() {
        this.playlists = new ArrayList<>();
    }

    // REQUIRES: playlist is non-null and cannot already be in the library
    // MODIFIES: this
    // EFFECTS: adds given playlist to the music library, if its already in the
    // playlist do nothing. Includes event logs 
    public void addPlaylist(Playlist playlist) {
        if (!playlists.contains(playlist)) {
            playlists.add(playlist);
            EventLog.getInstance().logEvent(new Event("Playlist added: " + playlist.getName()));
        }
    }

    // REQUIRES:In order to remove a playlist, a playlist must exist in the library
    // MODIFIES: this
    // EFFECTS: removes given playlist from the music library, includes event logs 
    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
        EventLog.getInstance().logEvent(new Event("Playlist removed: " + playlist.getName()));
    }

    // MODIFIES: this
    // EFFECTS: Returns the number of playlists in the library
    public int getPlaylistCount() {
        return playlists.size();
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    // EFFECTS: returns music library as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("playlists", playlistsToJson());
        return json;
    }

    // EFFECTS: returns playlists in this music library as a JSON array
    private JSONArray playlistsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Playlist playlist : playlists) {
            jsonArray.put(playlist.toJson());
        }
        return jsonArray;
    }
}
