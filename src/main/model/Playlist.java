package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

//Represents a playlist with its own name and list of songs within it
public class Playlist implements Writable {
    private String name;
    private List<Song> songs;

    // REQUIRES: the String "name" must be non-empty and non-null
    // MODIFIES: this
    // EFFECTS: Creates an empty playlist with the given name set
    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<Song>();
    }

    // REQUIRES: song must be non-null and must not be already in the playlist
    // MODIFIES: this
    // EFFECTS: adds the given song to the playlist with event logs
    public void addSong(Song song) {
        if (song != null && !songs.contains(song)) {
            songs.add(song);
            EventLog.getInstance().logEvent(
                    new Event("Song added: " + song.getFullSong() + " to playlist: " + name));
        }
    }

    // REQUIRES: song must be in the playlist already to remove the song from the
    // playlist
    // MODIFIES: this
    // EFFECTS: removes the specified song from the playlist with event logs
    public void removeSong(Song song) {
        songs.remove(song);
        EventLog.getInstance().logEvent(
                new Event("Song removed: " + song.getFullSong() + " from playlist: " + name));
    }

    // REQUIRES: playlist contains at least one song
    // MODIFIES: this
    // EFFECTS: Plays all songs in the playlist, incrementing their play counts.
    public void playAll() {
        for (Song song : songs) {
            song.play();
        }
    }

    // REQUIRES: playlist must exist
    // MODIFIES: this
    // EFFECTS: returns the total number of songs in a playlist
    public int getTotalSongs() {
        return songs.size();
    }

    // REQUIRES: playlist must exist
    // MODIFIES: this
    // EFFECTS: returns the total duration of songs in a playlist
    public int getTotalDuration() {
        int totalDuration = 0;
        for (Song song : songs) {
            totalDuration += song.getDuration();
        }
        return totalDuration;
    }

    public List<Song> getAllSongs() {
        return songs;
    }

    public String getName() {
        return name;
    }

    // EFFECTS: returns playlist as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        JSONArray songArray = new JSONArray();
        for (Song song : songs) {
            songArray.put(song.toJson());
        }
        json.put("songs", songArray);
        return json;
    }
}
