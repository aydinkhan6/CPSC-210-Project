package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import model.Playlist;
import model.Song;

public class WritableTest {
    protected static void checkSong(String title, String artist, int duration, int playCount, Song song) {
        assertEquals(title, song.getTitle());
        assertEquals(artist, song.getArtist());
        assertEquals(duration, song.getDuration());
        assertEquals(playCount, song.getPlayCount());
    }

    protected void checkPlaylist(String name, int totalSongs, Playlist playlist) {
        assertEquals(name, playlist.getName());
        assertEquals(totalSongs, playlist.getTotalSongs());
    }
}
