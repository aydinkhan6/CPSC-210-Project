package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.Test;

import model.MusicLibrary;
import model.Playlist;
import model.Song;

public class JsonWriterTest extends WritableTest {

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nonExistentFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // should pass
        }
    }

    @Test
    public void testReaderEmptyMusicLibrary() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyLibrary.json");
        try {
            MusicLibrary ml = reader.read();
            assertEquals(0, ml.getPlaylistCount());
        } catch (IOException e) {
            fail("Unable to read from the file");
        }
    }

    @Test
    public void testReaderGeneralMusicLibrary() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralLibrary.json");
        try {
            MusicLibrary ml = reader.read();
            assertEquals(2, ml.getPlaylistCount());
            validateTestLibrary(ml);
        } catch (IOException e) {
            fail("Unable to read from the file");
        }
    }

    // EFFECTS: Creates a testing music library with pre chosen playlists and songs
    protected MusicLibrary createTestLibrary() {
        MusicLibrary ml = new MusicLibrary();

        Playlist p1 = new Playlist("Throwbacks!");
        Playlist p2 = new Playlist("Summer Songs");

        p1.addSong(new Song("Since U Been Gone", "Kelly Clarkson", 3));
        p2.addSong(new Song("Darling, I", "Tyler the Creator", 4));
        p2.addSong(new Song("misses", "Dominic Fike", 2));

        ml.addPlaylist(p1);
        ml.addPlaylist(p2);
        return ml;
    }

    // EFFECTS: Validates that the filled library matches the expected test data
    protected void validateTestLibrary(MusicLibrary ml) {
        Playlist p1 = ml.getPlaylists().get(0);
        Playlist p2 = ml.getPlaylists().get(1);

        checkPlaylist("Throwbacks!", 1, p1);
        checkPlaylist("Summer Songs", 2, p2);

        checkSong("Since U Been Gone", "Kelly Clarkson", 3, 0, p1.getAllSongs().get(0));
        checkSong("Darling, I", "Tyler the Creator", 4, 0, p2.getAllSongs().get(0));
        checkSong("misses", "Dominic Fike", 2, 0, p2.getAllSongs().get(1));
    }

}
