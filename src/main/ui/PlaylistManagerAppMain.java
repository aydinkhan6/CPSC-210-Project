package ui;

import model.MusicLibrary;
import model.Playlist;
import model.Song;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlaylistManagerAppMain {
    private MusicLibrary musicLibrary;
    private Scanner scanner;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: Initializes the application and starts the user interface, loads
    // data if chosen
    public PlaylistManagerAppMain() {
        musicLibrary = new MusicLibrary();
        scanner = new Scanner(System.in);
        jsonWriter = new JsonWriter("JSON_STORE");
        jsonReader = new JsonReader("JSON_STORE");

        promptLoadData();
        runApp();
    }

    // EFFECTS: Runs the main menu loop
    private void runApp() {
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine();
            running = processChoice(choice);
        }
        promptSaveData();
    }

    // EFFECTS: Displays the main menu options
    private void displayMenu() {
        System.out.println("Music Playlist Manager");
        System.out.println("1. Create a playlist");
        System.out.println("2. Select a playlist");
        System.out.println("3. View most played songs");
        System.out.println("4. View all playlists");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    // EFFECTS: Takes user input and processes the choice
    private boolean processChoice(String choice) {
        switch (choice) {
            case "1":
                createPlaylist();
                break;
            case "2":
                selectPlaylist();
                break;
            case "3":
                Playlist playlist1 = musicLibrary.getPlaylists().get(0);
                viewMostPlayedSongs(playlist1);
                break;
            case "4":
                viewAllPlaylists();
                break;
            case "5":
                System.out.println("Exiting");
                return false;
            default:
                System.out.println("Invalid choice. Try again.");

        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Prompts user to load data from file if they choose
    private void promptLoadData() {
        System.out.print("Would you like to load your saved playlists? (yes/no): ");
        String response = scanner.nextLine();

        if (response.equals("yes")) {
            try {
                musicLibrary = jsonReader.read();
                System.out.println("Successfully loaded playlists!");
            } catch (IOException e) {
                System.out.println("Error loading data. Starting new.");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts user to save data before exiting
    private void promptSaveData() {
        System.out.print("Would you like to save your playlists before exiting? (yes/no): ");
        String response = scanner.nextLine();

        if (response.equals("yes")) {
            try {
                jsonWriter.open();
                jsonWriter.write(musicLibrary);
                jsonWriter.close();
                System.out.println("Playlists saved successfully!");
            } catch (IOException e) {
                System.out.println("Error saving playlists.");
            }
        }
    }

    // REQUIRES: name must be non-empty and not already exist in the music library
    // MODIFIES: this
    // EFFECTS: Creates a new playlist with the given name and adds it to the music
    // library
    private void createPlaylist() {
        System.out.print("Enter playlist name: ");
        String name = scanner.nextLine();

        if (name.isEmpty()) {
            System.out.println("Invalid name. Please enter a valid name.");
            return;
        }

        musicLibrary.addPlaylist(new Playlist(name));
        System.out.println("Playlist created: " + name);
    }

    // REQUIRES: At least one playlist must be in the library
    // EFFECTS: Allows the user to choose an existing playlist to manage
    private void selectPlaylist() {
        if (musicLibrary.getPlaylistCount() == 0) {
            System.out.println("No playlists available.");
            return;
        }

        System.out.println("Select a playlist:");
        for (int i = 0; i < musicLibrary.getPlaylistCount(); i++) {
            System.out.println((i + 1) + ". " + musicLibrary.getPlaylists().get(i).getName());
        }
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= musicLibrary.getPlaylistCount()) {
            Playlist selectedPlaylist = musicLibrary.getPlaylists().get(choice - 1);
            managePlaylist(selectedPlaylist);
        } else {
            System.out.println("Invalid selection.");
        }
    }

    // REQUIRES: A playlist must be selected, song must be non-null and not already
    // in the playlist
    // MODIFIES: this
    // EFFECTS: Adds a song to the selected playlist
    @SuppressWarnings("methodlength")
    private void managePlaylist(Playlist playlist) {
        boolean managing = true;
        while (managing) {
            printMenu(playlist);

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addSong(playlist);
                    break;

                case "2":
                    removeSong(playlist);
                    break;

                case "3":
                    playSingleSong(playlist);
                    break;

                case "4":
                    System.out.println("Total songs: " + playlist.getTotalSongs());
                    break;

                case "5":
                    System.out.println("Total duration: " + playlist.getTotalDuration() + " minutes");
                    break;

                case "6":
                    managing = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void removeSong(Playlist playlist) {
        System.out.print("Enter song title to remove: ");
        String titleToRemove = scanner.nextLine();

        Song songToRemove = null;

        for (Song s : playlist.getAllSongs()) {
            if (s.getTitle().equals(titleToRemove)) {
                songToRemove = s;
                break;
            }
        }

        if (songToRemove != null) {
            playlist.removeSong(songToRemove);
            System.out.println("Removed song: " + titleToRemove);
        } else {
            System.out.println("Song not found in playlist.");
        }
    }

    private void addSong(Playlist playlist) {
        System.out.print("Enter song title:");
        String title = scanner.nextLine();
        System.out.print("Enter song's artist Name:");
        String artist = scanner.nextLine();
        System.out.print("Enter song duration in minutes:");
        int duration = scanner.nextInt();
        scanner.nextLine();

        Song song = new Song(title, artist, duration);
        playlist.addSong(song);
        System.out.println("Added song: " + title);
    }

    private void printMenu(Playlist playlist) {
        System.out.println("Managing Playlist: " + playlist.getName());
        System.out.println("1. Add song");
        System.out.println("2. Remove song");
        System.out.println("3. Play song");
        System.out.println("4. View total songs");
        System.out.println("5. View total duration");
        System.out.println("6. Go back");
        System.out.print("Enter choice: ");
    }

    // REQUIRES: The song must exist in a playlist
    // MODIFIES: this
    // EFFECTS: Plays a song and increases its play count
    private void playSingleSong(Playlist playlist) {
        if (playlist.getTotalSongs() == 0) {
            System.out.println("No songs to play.");
            return;
        }

        System.out.print("Enter song title to play: ");
        String title = scanner.nextLine();
        for (Song song : playlist.getAllSongs()) {
            if (song.getTitle().equals(title)) {
                song.play();
                System.out.println("Playing: " + song.getTitle());
                return;
            }
        }
        System.out.println("Song not found.");
    }

    // REQUIRES: At least one song must have been played
    // EFFECTS: Shows the top 3 most played songs
    private void viewMostPlayedSongs(Playlist playlist) {
        List<Song> songs = new ArrayList<>(playlist.getAllSongs());
        if (songs.isEmpty()) {
            System.out.println("No songs in this playlist.");
            return;
        }

        for (int i = 0; i < songs.size() - 1; i++) {
            for (int j = i + 1; j < songs.size(); j++) {
                if (songs.get(i).getPlayCount() < songs.get(j).getPlayCount()) {
                    Song temp = songs.get(i);
                    songs.set(i, songs.get(j));
                    songs.set(j, temp);
                }
            }
        }

        System.out.println("Top 3 Most Played Songs in " + playlist.getName() + ":");
        for (int i = 0; i < songs.size() && i < 3; i++) {
            Song song = songs.get(i);
            System.out.println((i + 1) + ". " + song.getTitle() + " - Played " + song.getPlayCount() + " times");
        }
    }

    // EFFECTS: Displays all playlists in the music library in a neat format
    private void viewAllPlaylists() {
        List<Playlist> playlists = musicLibrary.getPlaylists();

        if (playlists.isEmpty()) {
            System.out.println("No playlists available.");
            return;
        }

        System.out.println("Your Playlists:");
        System.out.println("----------------------------");
        for (Playlist playlist : playlists) {
            System.out.println(playlist.getName() + " | Songs: " + playlist.getTotalSongs()
                    + " | Total Duration: " + playlist.getTotalDuration() + " min");
        }
        System.out.println("----------------------------");
    }

    public static void main(String[] args) {
        new PlaylistManagerAppMain();
    }
}