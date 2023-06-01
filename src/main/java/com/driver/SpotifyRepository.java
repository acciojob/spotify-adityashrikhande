package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name, mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {

        boolean flag = false;
        for(int i=0; i<artists.size(); i++){
            if(artists.get(i).getName() == artistName){
                flag = true;
                break;
            }
        }
        if(!flag){
            Artist artist = new Artist(artistName);
            artists.add(artist);
        }
        Album album = new Album(title);
        albums.add(album);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song = new Song(title, length);
        songs.add(song);
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist playlist = new Playlist(title);
        List<Song> lis = new ArrayList<>();
        playlistSongMap.put(playlist, lis);

        for(int i=0; i<songs.size(); i++){
            if(songs.get(i).getLength() == length && !playlistSongMap.get(playlist).contains(songs.get(i))){
                playlistSongMap.get(playlist).add(songs.get(i));
            }
        }
        playlists.add(playlist);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist = new Playlist(title);
        List<Song> lis = new ArrayList<>();
        playlistSongMap.put(playlist, lis);

        for(int i=0; i<songs.size(); i++){
            if(songs.get(i).getTitle() == title && !playlistSongMap.get(playlist).contains(songs.get(i))){
                playlistSongMap.get(playlist).add(songs.get(i));
            }
        }
        playlists.add(playlist);
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        Playlist playlist = new Playlist();
        List<User> lis = new ArrayList<>();
        User user = new User();
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getMobile() == mobile){
                user = users.get(i);
                break;
            }
        }
        for(int i=0; i<playlists.size(); i++){
            if(playlistTitle == playlists.get(i).getTitle()){
                playlist = playlists.get(i);
                break;
            }
        }
        if(!playlistListenerMap.get(playlist).contains(user)){
            playlistListenerMap.get(playlist).add(user);
        }
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

        Song song = new Song();
        for(int i=0; i<songs.size(); i++){
            if(songs.get(i).getTitle() == songTitle){
                boolean flag = false;
                song = songs.get(i);
                for(User us : songLikeMap.get(songs.get(i))){
                    if(us.getMobile() == mobile){
                        flag = true;
                        break;
                    }
                }
                if(!flag)song.setLikes(song.getLikes() + 1);
                if(!flag){
                    String name = "";
                    for(int j=0; j<users.size(); j++){
                        if(users.get(j).getMobile() == mobile){
                            name = users.get(j).getName();
                            break;
                        }
                    }
                    for(int j=0; j<artists.size(); j++){
                        if(artists.get(j).getName() == name && name.length() != 0){
                            artists.get(j).setLikes(artists.get(j).getLikes() + 1);
                            break;
                        }
                    }
                }
            }
        }
        return song;
    }

    public String mostPopularArtist() {
        String name = "";
        int likes = 0;
        for(int i=0; i<artists.size(); i++){
            if(artists.get(i).getLikes() > likes){
                name = artists.get(i).getName();
                likes = artists.get(i).getLikes();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String name = "";
        int likes = 0;
        for(int i=0; i<songs.size(); i++){
            if(songs.get(i).getLikes() > likes){
                name = songs.get(i).getTitle();
                likes = songs.get(i).getLikes();
            }
        }
        return name;
    }
}
