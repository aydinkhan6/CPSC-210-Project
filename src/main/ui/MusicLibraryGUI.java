package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class MusicLibraryGUI {
    private static final String JSON_STORE = "JSON_STORE";
    private MusicLibrary library;
    private JFrame frame;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private DefaultListModel<String> playlistModel;
    private JList<String> playlistJList;

    // EFFECTS: Initializes GUI and loads playlists if the user chooses to
    public MusicLibraryGUI() {
        library = new MusicLibrary();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        setupUI();
        ImageIcon picture = new ImageIcon("src/music.png");

        int option = JOptionPane.showConfirmDialog(
                frame,
                "Would you like to load your saved playlists?",
                "Load Playlists",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                picture);

        if (option == JOptionPane.YES_OPTION) {
            try {
                library = jsonReader.read();

                for (Playlist p : library.getPlaylists()) {
                    playlistModel.addElement(p.getName());
                }

                JOptionPane.showMessageDialog(frame, "Playlists loaded successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to load playlists");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: sets up the main application window and its components
    private void setupUI() {
        frame = new JFrame("Music Library Manager");
        frame.setSize(800, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        playlistModel = new DefaultListModel<>();
        playlistJList = new JList<>(playlistModel);

        JButton addPlaylistButton = setupAddPlaylistButton();
        JButton addSongButton = setupAddSongToPlaylist();
        JButton removePlaylistButton = setupRemovePlaylistButton();
        JButton showLongestPlaylistButton = setupShowLongestPlaylistButton();
        JButton quitButton = setupQuitApplicationButton();

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addPlaylistButton);
        buttonPanel.add(addSongButton);
        buttonPanel.add(removePlaylistButton);
        buttonPanel.add(showLongestPlaylistButton);
        buttonPanel.add(quitButton);

        frame.add(new JScrollPane(playlistJList), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // EFFECTS: returns a button that prompts the user to save the application and
    // exits the application with an event log
    private JButton setupQuitApplicationButton() {
        JButton quitButton = new JButton("Quit Application");

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImageIcon picture = new ImageIcon("src/music.png");

                int option = JOptionPane.showConfirmDialog(
                        frame,
                        "Would you like to save your playlists before quitting?",
                        "Save Playlists",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        picture);
                if (option == JOptionPane.YES_OPTION) {
                    saveBeforeExit();
                }

                System.out.println("---- EVENT LOG ----");
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.toString());
                    System.out.println("-------------------");
                }

                System.exit(0);
            }
        });
        return quitButton;
    }

    // EFFECTS: returns a button that displays the playlist with the most songs
    private JButton setupShowLongestPlaylistButton() {
        JButton showLongestPlaylistButton = new JButton("Show Longest Playlist");

        showLongestPlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (library.getPlaylistCount() == 0) {
                    JOptionPane.showMessageDialog(frame, "No playlists available.");
                    return;
                }

                Playlist longest = null;
                int maxSongs = 0;

                for (Playlist p : library.getPlaylists()) {
                    if (p.getTotalSongs() > maxSongs) {
                        longest = p;
                        maxSongs = p.getTotalSongs();
                    }
                }

                if (longest != null) {
                    JOptionPane.showMessageDialog(frame,
                            "Longest Playlist: " + longest.getName()
                                    + " Total Songs: " + longest.getTotalSongs());
                }
            }
        });
        return showLongestPlaylistButton;
    }

    // EFFECTS: returns a button that removes a selected playlist from the music
    // library
    private JButton setupRemovePlaylistButton() {
        JButton removePlaylistButton = new JButton("Remove Playlist");
        removePlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (library.getPlaylistCount() == 0) {
                    JOptionPane.showMessageDialog(frame, "No playlists available");
                    return;
                }

                String selectedPlaylistName = showRemovePlaylistDropdownMenu();

                if (selectedPlaylistName != null) {
                    handleRemovePlaylistByName(selectedPlaylistName);
                }
            }
        });
        return removePlaylistButton;
    }

    // EFFECTS; returns a button that adds a song to a playlist that is chosen
    private JButton setupAddSongToPlaylist() {
        JButton addSongButton = new JButton("Add Song to Playlist");

        addSongButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (library.getPlaylistCount() == 0) {
                    JOptionPane.showMessageDialog(frame, "No playlists available");
                    return;
                }

                String selectedPlaylistName = showPlaylistDropdownMenu();

                if (selectedPlaylistName != null) {
                    Playlist selected = null;
                    for (Playlist p : library.getPlaylists()) {
                        if (p.getName().equals(selectedPlaylistName)) {
                            selected = p;
                            break;
                        }
                    }

                    if (selected != null) {
                        handleAddSongToPlaylist(selected);
                    }
                }
            }

        });
        return addSongButton;
    }

    // EFFECTS: returns a button that adds a playlist to a music library
    private JButton setupAddPlaylistButton() {
        JButton addPlaylistButton = new JButton("Add Playlist");

        addPlaylistButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(frame, "Enter playlist name:");

                if (name != null && !name.isEmpty()) {
                    Playlist newPlaylist = new Playlist(name);
                    library.addPlaylist(newPlaylist);
                    playlistModel.addElement(newPlaylist.getName());
                }
            }
        });
        return addPlaylistButton;
    }

    // REQUIRES: selected is a valid playlist
    // MODIFIES: this
    // EFFECTS: prompts the user to enter details of a song and adds it to a
    // playlist
    private void handleAddSongToPlaylist(Playlist selected) {
        String title = JOptionPane.showInputDialog(frame, "Enter song title:");
        String artist = JOptionPane.showInputDialog(frame, "Enter artist name:");
        String durationStr = JOptionPane.showInputDialog(frame, "Enter duration (minutes):");

        try {
            int duration = Integer.parseInt(durationStr);
            Song newSong = new Song(title, artist, duration);
            selected.addSong(newSong);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid duration. Please enter a number.");
        }
    }

    private String showPlaylistDropdownMenu() {
        ArrayList<String> playlistNameList = new ArrayList<>();
        for (Playlist p : library.getPlaylists()) {
            playlistNameList.add(p.getName());
        }

        String[] playlistNames = playlistNameList.toArray(new String[0]);

        String selectedPlaylistName = (String) JOptionPane.showInputDialog(
                frame,
                "Select a playlist:",
                "Choose Playlist",
                JOptionPane.PLAIN_MESSAGE,
                null,
                playlistNames,
                playlistNames[0]);
        return selectedPlaylistName;
    }

    // EFFECTS: returns the name of a selected playlist from a dropdown menu
    private String showRemovePlaylistDropdownMenu() {
        ArrayList<String> playlistNameList = new ArrayList<>();
        for (Playlist p : library.getPlaylists()) {
            playlistNameList.add(p.getName());
        }

        String[] playlistNames = playlistNameList.toArray(new String[0]);

        String selectedPlaylistName = (String) JOptionPane.showInputDialog(
                frame,
                "Select a playlist to remove:",
                "Remove Playlist",
                JOptionPane.PLAIN_MESSAGE,
                null,
                playlistNames,
                playlistNames[0]);
        return selectedPlaylistName;
    }

    // EFFECTS: saves the music library
    private void saveBeforeExit() {
        try {
            jsonWriter.open();
            jsonWriter.write(library);
            jsonWriter.close();
            JOptionPane.showMessageDialog(frame, "Playlists saved successfully!");
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(frame, "Error saving playlists");
        }
    }

    // REQUIRES: selectedPlaylistName is the name of an existing playlist in the
    // music library
    // MODIFIES: this
    // EFFECTS: Removes the playlist from the music library and updates the GUI
    private void handleRemovePlaylistByName(String selectedPlaylistName) {
        Playlist selected = null;
        for (Playlist p : library.getPlaylists()) {
            if (p.getName().equals(selectedPlaylistName)) {
                selected = p;
                break;
            }
        }

        if (selected != null) {
            library.removePlaylist(selected);
            playlistModel.removeElement(selectedPlaylistName);
            JOptionPane.showMessageDialog(frame, "Removed playlist: " + selectedPlaylistName);
        }
    }

    public static void main(String[] args) {
        new MusicLibraryGUI();
    }

}
