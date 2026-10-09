package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class SongTest {
    private Song song;

    @BeforeEach
    public void setUp() {
        song = new Song("Nangs", "Tame Impala", 2);
    }

    @Test
    public void testPlay() {
        setUp();
        assertEquals(0, song.getPlayCount()); 
        song.play();
        assertEquals(1, song.getPlayCount()); 
        song.play();
        assertEquals(2, song.getPlayCount()); 
    }

    @Test
    public void testFullSong() {
        setUp();
        assertEquals("Nangs - Tame Impala (2 mins)", song.getFullSong());
    }
}
