//package pt.ulusofona.aed.rockindeisi2023;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.management.StringValueExp;
//import java.io.File;
//
//import static org.junit.jupiter.api.Assertions.*;
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
//        String expected = "1 | Song Name | 2005 | 3:00 | 5 | 1";
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
//
//    @Test
//    public void parseMultipleArtistsTeste1() {
//        assertArrayEquals(new String[]{"aaa"},
//                Main.parseMultipleArtists("['aaa']").toArray());
//    }
//    @Test
//    public void parseMultipleArtistsTest2() {
//        assertArrayEquals(new String[]{"aaa", "bbb"},
//                Main.parseMultipleArtists("['aaa', 'bbb']").toArray());
//    }
//    @Test
//    public void parseMultipleArtistsTest3() {
//        assertArrayEquals(new String[]{"a,aa", "bbb"},
//                Main.parseMultipleArtists("['a,aa', 'bbb']").toArray());
//    }
//    @Test
//    public void parseMultipleArtistsTest4() {
//        assertArrayEquals(new String[]{"aaa", "bb b"},
//                Main.parseMultipleArtists("['aaa', 'bb b']").toArray());
//    }
//    @Test
//    public void parseMultipleArtistsTest5() {
//        assertArrayEquals(new String[]{"a'aa", "bbb"},
//                Main.parseMultipleArtists("[\"\"a'aa\"\", 'bbb']").toArray());
//    }
//    @Test
//    public void parseMultipleArtistsTest6() {
//
//        assertArrayEquals(new String[]{"a' aa", "b, bb", "ccc"},
//                Main.parseMultipleArtists("[\"\"a' aa\"\", 'b, bb', 'ccc']").toArray());
//    }
//    @Test
//    public void parseMultipleArtistsTest7() {
//        assertArrayEquals(new String[]{"Engelbert Humperdinck", "Herbert von Karajan",
//                        "Bancroft's School Choir", "Elisabeth Grümmer", "Elisabeth Schwarzkopf", "Loughton High School for Girls' Choir", "Philharmonia Orchestra"},
//                Main.parseMultipleArtists("['Engelbert Humperdinck', 'Herbert von Karajan', \"\"Bancroft's School Choir\"\", 'Elisabeth Grümmer', 'Elisabeth Schwarzkopf', \"\"Loughton High School for Girls' Choir\"\", 'Philharmonia Orchestra']").toArray());
//    }
//    @Test
//    public void parseMultipleArtistsTest8() {
//
//        assertArrayEquals(new String[]{"Lin-Manuel Miranda", "'In The Heights' Original Broadway Company"},
//                Main.parseMultipleArtists("['Lin-Manuel Miranda', \"\"'In The Heights' Original Broadway Company\"\"]").toArray());
//}
//@Test
//    public void executeTest1() {
//        String actualResult = String.valueOf(Main.parseCommand("COUNT_SONGS_YEAR 2022"));
//        String realResult = String.valueOf(Main.parseCommand("COUNT_SONGS_YEAR 2022"));
//        assertEquals(realResult, actualResult);
//    }
//
//    @Test
//    public void executeTest2() {
//        String actualResult = String.valueOf(Main.parseCommand("GET_UNIQUE_TAGS"));
//        assertEquals(String.valueOf(Main.parseCommand("GET_UNIQUE_TAGS")), actualResult);
//    }
//
//    @Test
//    public void executeTest3() {
//        String actualResult = String.valueOf(Main.execute("GET_SONGS_BY_ARTIST 10 qwertyuytred"));
//        assertEquals(("No songs"), actualResult);
//    }
//
//    @Test
//    public void executeTest4() {
//        Object actualResult = Main.execute("Teste de comando não existente");
//        assertNull(actualResult);
//    }
//
//
//    @Test
//    public void countSongYearTest() {
//        String actualResult = String.valueOf(FunParaQueries.countSongsYear(2029));
//        assertEquals("0", actualResult);
//    }
//
//    @Test
//    public void duplicatesongYearTest() {
//        String actualResult = String.valueOf(FunParaQueries.countDuplicateSongsYear(2029));
//        System.out.println(actualResult);
//        assertEquals("0", actualResult);
//    }
//
//
//}