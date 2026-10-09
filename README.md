# Music Playlist Creator and Song Statistics

A Java application for creating and managing music playlists, built for UBC course CPSC 210.

## Features
- Create, name, and remove playlists
- Add and remove songs in a playlist
- Play songs to track play counts and see your most played
- View total songs and total length (in minutes) of a playlist
- Find your longest playlist
- Save and load your library between sessions (JSON)

## How to use
- Run the main GUI. On startup you'll be asked whether to load a previously saved library.
- Use "Add Playlist" and "Add Song to Playlist" to build your library, and "Remove Playlist" to delete one.
- Use "Show Longest Playlist" to see the playlist with the most songs.
- Click "Quit Application" and choose whether to save your library.

## Design
Core classes (MusicLibrary, Playlist, Song) implement a shared Writable interface for JSON saving and loading. Future improvements: a shared base class for JsonReader and JsonWriter, and an interface for playlist management.