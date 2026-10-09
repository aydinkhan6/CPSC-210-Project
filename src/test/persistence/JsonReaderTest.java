package persistence;

import model.MusicLibrary;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonWriterTest {
    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // Expected: Pass test
        }
    }

    @Test
    public void testReaderEmptyMusicLibrary() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyLibrary.json");
        MusicLibrary ml = new MusicLibrary();
        try {
            ml = reader.read();
            assertEquals(0, ml.getPlaylistCount());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
        assertEquals(0, ml.getPlaylistCount());
    }

    @Test
    public void testReaderGeneralMusicLibrary() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralLibrary.json");
        try {
            MusicLibrary ml = reader.read();
            assertEquals(2, ml.getPlaylistCount());
            validateTestLibrary(ml);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}