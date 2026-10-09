package model;

import org.json.JSONObject;

import persistence.Writable;

//REPRESENTS A SONG WITH A TITLE, ARTIST, AND PLAYCOUNT
public class Song implements Writable {
    private String artist;
    private String title;
    private int playCount;
    private int duration;

    // REQUIRES: title and artist to be non-empty strings, minutes >= 0
    // MODIFIES: this
    // EFFECTS: Creates a song with its given title and artist, and initializes the
    // starting playCount to 0
    public Song(String title, String artist, int minutes) {
        this.title = title;
        this.artist = artist;
        this.duration = minutes;
        this.playCount = 0;
    }

    // REQUIRES: The song instance must exist and be not null
    // MODIFIES: this
    // EFFECTS: increments the playCount up by 1 (playing a song)
    public void play() {
        playCount++;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public int getPlayCount() {
        return playCount;
    }

    public String getFullSong() {
        return title + " - " + artist + " (" + duration + " mins)";
    }

    // EFFECTS: returns song as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("artist", artist);
        json.put("duration", duration);
        json.put("playCount", playCount);
        return json;
    }
}
