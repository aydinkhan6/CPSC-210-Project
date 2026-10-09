package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class PlaylistTest {
    private Playlist playlist;
    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    public void setUp() {
        playlist = new Playlist("Late Night");
        song1 = new Song("The Morning", "The Weeknd", 5);
        song2 = new Song("Self Control", "Frank Ocean", 4);
        song3 = new Song("Clocks", "Coldplay", 5);
    }

    @Test
    public void testAddSong() {
        setUp();
        assertEquals(0, playlist.getTotalSongs()); 
        playlist.addSong(song1);
        assertEquals(1, playlist.getTotalSongs()); 
        playlist.addSong(song2);
        assertEquals(2, playlist.getTotalSongs()); 
    }

    @Test
    public void testAddDuplicateSong() {
        setUp();
        playlist.addSong(song1);
        playlist.addSong(song1);  
        assertEquals(1, playlist.getTotalSongs());
    }

    @Test
    public void testRemoveSong() {
        setUp();
        addSongs();
        assertEquals(2, playlist.getTotalSongs());
        playlist.removeSong(song1);
        assertEquals(1, playlist.getTotalSongs());
        playlist.removeSong(song2);
        assertEquals(0, playlist.getTotalSongs()); 
    }

    @Test
    public void testRemoveNonExistentSong() {
    setUp();
    addSongs();
    assertEquals(2, playlist.getTotalSongs());
    Song fakeSong = new Song("Fake Song", "Unknown Artist", 3);
    playlist.removeSong(fakeSong); 
    assertEquals(2, playlist.getTotalSongs()); 
    }

    @Test
    public void testRemoveFromEmptyPlaylist() {
        setUp();
        playlist.removeSong(song1); 
        assertEquals(0, playlist.getTotalSongs());
    }



    @Test
    public void testGetTotalDuration() {
        setUp();
        addSongs();
        assertEquals(9, playlist.getTotalDuration()); 

        playlist.addSong(song3);
        assertEquals(14, playlist.getTotalDuration()); 
    }

    @Test
    public void testPlayAll() {
        setUp();
        addSongs();
        assertEquals(0, song1.getPlayCount());
        assertEquals(0, song2.getPlayCount());

        playlist.playAll(); 
        assertEquals(1, song1.getPlayCount());
        assertEquals(1, song2.getPlayCount());
    }

    @Test
    public void testPlayAllEmptyPlaylist() {
        setUp();
        playlist.playAll(); 
        assertEquals(0, playlist.getTotalSongs());
    }


    private void addSongs() {
        playlist.addSong(song1);
        playlist.addSong(song2);
    }

}
