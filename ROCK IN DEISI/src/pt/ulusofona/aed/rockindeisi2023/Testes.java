//package pt.ulusofona.aed.rockindeisi2023;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import static pt.ulusofona.aed.rockindeisi2023.Main.parseCommand;
//
//
//public class Testes {
//
//
//    @Test
//    public void ano_de_lancamento_menor_1995() {
//        Songs song = new Songs("1", "Song Name", 1990);
//        String expected = "1 | Song Name | 1990";
//        String actual = song.toString();
//        assertEquals(expected, actual);
//    }
//
//
//    @Test
//    public void ano_de_lancamento_menor_2000() {
//        Songs song = new Songs("1", "Song Name",1998);
//        song.details = new SongDetails();
//        song.details.duracao = 250000;
//        song.details.popularidade = 85;
//        String expected = "1 | Song Name | 1998 | 4:10 | 85";
//        String result = song.toString();
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void ano_de_lancamento_maior2000() {
//        Songs song = new Songs("1", "Song Name", 2005);
//        song.details = new SongDetails();
//        song.details.duracao = 180000;
//        song.details.popularidade = 5;
//        String expected = "1 | Song Name | 2005 | 3:00 | 5 | 2";
//        String actual = song.toString();
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void testToString() {
//        SongArtist artist = new SongArtist("1", "kash");
//        String expected = "Artista: [kash] | 1";
//        String actual = artist.toString();
//        assertEquals(expected, actual);
//    }
//    @Test
//    void testToStringArtistaletraA() {
//        SongArtist artist = new SongArtist("1", "Akash");
//        String expected = "Artista: [Akash]";
//        String actual = artist.toString();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void loadFiles_not_found() {
//        boolean result = Main.loadFiles(new File("skjh"));
//        assertEquals(false, result);
//    }
//
//
//    @Test
//    public void testParseCommand_GetSongsByArtist() {
//        String input = "GET_SONGS_BY_ARTIST 5 John Doe";
//        Query expectedQuery = new Query("GET_SONGS_BY_ARTIST", new String[]{"5", "John Doe"});
//        Query actualQuery = parseCommand(input);
//        Assertions.assertEquals(expectedQuery, actualQuery);
//    }
//
//    @Test
//    public void testParseCommand_GetMostDanceable() {
//        String input = "GET_MOST_DANCEABLE 10";
//        Query expectedQuery = new Query("GET_MOST_DANCEABLE", new String[]{"10"});
//        Query actualQuery = parseCommand(input);
//        Assertions.assertEquals(expectedQuery, actualQuery);
//    }
//
//
//
//    @Test
//    public void testParseCommand_InvalidCommand() {
//        String input = "INVALID_COMMAND";
//        Query expectedQuery = null;
//        Query actualQuery = parseCommand(input);
//        Assertions.assertEquals(expectedQuery, actualQuery);
//    }
//
//    @Test
//    public void testParseCommand_EmptyInput() {
//        String input = "";
//        Query expectedQuery = null;
//        Query actualQuery = parseCommand(input);
//        Assertions.assertEquals(expectedQuery, actualQuery);
//    }
//
//
//
//
//
//}