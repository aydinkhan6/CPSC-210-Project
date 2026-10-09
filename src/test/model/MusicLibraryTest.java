package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class MusicLibraryTest {
    private MusicLibrary library;
    private Playlist playlist1;
    private Playlist playlist2;


    @BeforeEach
    public void setUp() {
        library = new MusicLibrary();
        playlist1 = new Playlist("Gym Music");
        playlist2 = new Playlist("Chill Music");
    }

    @Test
    public void testAddPlaylist() {
        setUp();
        assertEquals(0, library.getPlaylistCount()); 
        library.addPlaylist(playlist1);
        assertEquals(1, library.getPlaylistCount()); 
        library.addPlaylist(playlist2);
        assertEquals(2, library.getPlaylistCount()); 
        
    }

    @Test
    public void testRemovePlaylist() {
        setUp();
        library.addPlaylist(playlist1);
        library.addPlaylist(playlist2);
        assertEquals(2, library.getPlaylistCount());
        library.removePlaylist(playlist1);
        assertEquals(1, library.getPlaylistCount());
        library.removePlaylist(playlist2);
        assertEquals(0, library.getPlaylistCount()); 
    }

    @Test
    public void testRemoveNonExistentPlaylist() {
        setUp();
        assertEquals(0, library.getPlaylistCount()); 
        library.removePlaylist(playlist1); 
        assertEquals(0, library.getPlaylistCount()); 
        library.removePlaylist(null);
        assertEquals(0, library.getPlaylistCount()); 
        }
    
    @Test
    public void testAddDuplicatePlaylist() {
        setUp();
        library.addPlaylist(playlist1);
        assertEquals(1, library.getPlaylistCount()); 
        
        library.addPlaylist(playlist1); 
        assertEquals(1, library.getPlaylistCount()); 
        
        library.addPlaylist(playlist2);
        assertEquals(2, library.getPlaylistCount()); 
        }
}
