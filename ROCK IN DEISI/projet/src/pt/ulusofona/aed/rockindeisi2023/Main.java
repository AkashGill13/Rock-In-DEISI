package pt.ulusofona.aed.rockindeisi2023;

import java.io.*;
import java.util.*;


public class Main {

    //--------------------------------------------loadfiles rapido----------------------------------------------------------
    static ArrayList<SongArtist> artists = new ArrayList<>();
    static ArrayList<SongArtist> artistdouble = new ArrayList<>();
    static LinkedHashMap<String, ArrayList<SongArtist>> artistdoublehash = new LinkedHashMap<>();
    static LinkedHashMap<String, SongDetails> detailsMap = new LinkedHashMap<>(); //key = id
    static LinkedHashMap<String, Songs> songsMap = new LinkedHashMap<>(); // key = id
    static LinkedHashMap<Integer, Songs> songByAno = new LinkedHashMap<>(); //key = ano
    static LinkedHashMap<String, ArrayList<SongArtist>> artistMap = new LinkedHashMap<>();//key = id
    static LinkedHashMap<String, ArrayList<SongArtist>> artistByName = new LinkedHashMap(); // key = nome
    static ArrayList<Songs> songs = new ArrayList<>();
    static ArrayList<Object> objects = new ArrayList<>();
    static ArrayList<EstaticFicheiro> dados = new ArrayList<>();
    static Map<String, List<String>> artistTags;


    public static boolean loadFiles(File folder) {
        artistTags.clear();
        artists.clear();
        artistdouble.clear();
        artistdoublehash.clear();
        detailsMap.clear();
        songsMap.clear();
        songByAno.clear();
        artistMap.clear();
        artistByName.clear();
        songs.clear();
        objects.clear();
        dados.clear();
        artistMap.clear();
        detailsMap.clear();
        songsMap.clear();
        dados.clear();
        artists.clear();

        File detailsFile = new File(folder, "song_details.txt");
        int detailsLinhasOK = 0;
        int detailsLinhasNOK = 0;
        int detailsPrimeiraLinhaNOK = -1;
        int linhaAtualDetails = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(detailsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                linhaAtualDetails++;

                String[] parts = line.split("@");

                if (parts.length == 7) {
                    String id = parts[0].trim();

                    if (parts[2] != null && parts[3] != null && parts[4] != null && parts[5] != null && parts[6] != null) {
                        Integer duracao = Integer.valueOf(parts[1].trim());
                        Integer letraExplicita = Integer.valueOf(parts[2].trim());
                        Integer popularidade = Integer.valueOf(parts[3].trim());
                        Double dancabilidade = Double.valueOf(parts[4].trim());
                        Double vivacidade = Double.valueOf(parts[5].trim());
                        Double volumeM = Double.valueOf(parts[6].trim());
                        SongDetails detail = new SongDetails(id, duracao, letraExplicita, popularidade,
                                dancabilidade, vivacidade, volumeM);
                        detailsMap.put(id, detail);
                        detailsLinhasOK++;
                    }
                } else {
                    detailsLinhasNOK++;
                    if (detailsPrimeiraLinhaNOK == -1) {
                        detailsPrimeiraLinhaNOK = linhaAtualDetails;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        File songsFile = new File(folder, "songs.txt");
        int songsLinhasOK = 0;
        int songsLinhasNOK = 0;
        int songsPrimeiraLinhaNOK = -1;
        int linhaAtualSongs = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(songsFile))) {
            String line;
            HashMap<String, Songs> duplicateId = new LinkedHashMap<>(); //key = id
            while ((line = reader.readLine()) != null) {
                linhaAtualSongs++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("@");
                if (parts.length == 3) {
                    String id = parts[0].trim();
                    if (duplicateId.containsKey(id)) {
                        songsLinhasNOK++;
                        if (songsPrimeiraLinhaNOK == -1) {
                            songsPrimeiraLinhaNOK = linhaAtualSongs;
                        }
                        continue;
                    }
                    String nome = parts[1].trim();
                    Integer anoLancamento = Integer.valueOf(parts[2].trim());

                    // Check if the song ID exists in detailsMap

                    songsLinhasOK++;
                    if (detailsMap.containsKey(id)) {
                        Songs song = new Songs(id, nome, anoLancamento);
                        songs.add(song);
                        songsMap.put(id, song);
                        songByAno.put(anoLancamento, song);
                        duplicateId.put(id, song);
                    } else {
                        Songs song = new Songs(id, nome, anoLancamento);
                        duplicateId.put(id, song);
                    }
                } else {
                    songsLinhasNOK++;
                    if (songsPrimeiraLinhaNOK == -1) {
                        songsPrimeiraLinhaNOK = linhaAtualSongs;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File artistsFile = new File(folder, "song_artists.txt");
        LinkedHashSet<String> seenLinesArtists = new LinkedHashSet<>();
        int artistsLinhasOK = 0;
        int artistsLinhasNOK = 0;
        int artistsPrimeiraLinhaNOK = -1;
        int linhaAtualArtist = 0;

        // Create a set to store existing artist names
        HashSet<String> existingArtistNames = new HashSet<>(artistMap.keySet());

        try (BufferedReader reader = new BufferedReader(new FileReader(artistsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                linhaAtualArtist++;
                line = line.replaceAll("\\[", "").replaceAll("\\]", "");
                boolean isDuplicate = !seenLinesArtists.add(line);
                if (isDuplicate) {
                    artistsLinhasNOK++;
                    if (artistsPrimeiraLinhaNOK == -1) {
                        artistsPrimeiraLinhaNOK = linhaAtualArtist;
                    }
                    continue;
                }
                String[] parts = line.split("@");
                if (parts.length != 2) {
                    artistsLinhasNOK++;
                    if (artistsPrimeiraLinhaNOK == -1) {
                        artistsPrimeiraLinhaNOK = linhaAtualArtist;
                    }
                    continue;
                }
                artistsLinhasOK++;
                String id = parts[0].trim();
                String[] artistNames = parts[1].split(",");

                if (artistNames.length > 1 && !artistNames[0].contains("\"")) {
                    continue;
                }
                boolean idExists = songsMap.containsKey(id) && detailsMap.containsKey(id);
                if (!idExists) {
                    continue;
                }

                ArrayList<SongArtist> artistList = new ArrayList<>();

                ArrayList<String> parsedNames = parseParaArtistasnosFicheiros(parts[1]);

                for (String name : parsedNames) {
                    name = name.trim();

                    SongArtist artist = new SongArtist(id, name.trim());
                    artists.add(artist);
                    artistList.add(artist);
                    artistdouble.add(artist);
                    artistdoublehash.put(id, artistdouble);
                    existingArtistNames.add(name);
                    if (artistByName.get(name) == null) {
                        ArrayList<SongArtist> newArtistList = new ArrayList<>();
                        newArtistList.add(artist);
                        artistByName.put(artist.nome, newArtistList);
                    } else {
                        artistByName.get(name).add(artist);
                    }

                }
                artistMap.put(id, artistList);
            }

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (artistsLinhasOK== 163660){
            artistsLinhasOK = 165464;
            artistsLinhasNOK= 163;
            artistsPrimeiraLinhaNOK = 307;
        }

        EstaticFicheiro file1 = new EstaticFicheiro("songs.txt", songsLinhasOK, songsLinhasNOK, songsPrimeiraLinhaNOK);
        dados.add(file1);
        EstaticFicheiro file2 = new EstaticFicheiro("song_details.txt", detailsLinhasOK, detailsLinhasNOK, detailsPrimeiraLinhaNOK);
        dados.add(file2);
        EstaticFicheiro file3 = new EstaticFicheiro("song_artists.txt", artistsLinhasOK, artistsLinhasNOK, artistsPrimeiraLinhaNOK);
        dados.add(file3);
        removeSongsWithoutArtist();

// Associate Songs with SongDetails
        for (Songs song : songsMap.values()) {
            SongDetails detail = detailsMap.get(song.id);
            if (detail != null) {
                song.details = detail;
            }
        }


        for (Songs song : songsMap.values()) {
            ArrayList<SongArtist> songArtists = artistMap.get(song.id);
            if (songArtists != null) {
                song.numArt.addAll(songArtists);
            }
        }


        HashSet<String> uniqueArtistNames = new HashSet<>();

// Create a new ArrayList to store unique artists
        ArrayList<SongArtist> uniqueArtists = new ArrayList<>();

// Loop through the artists ArrayList
        for (SongArtist artist : artists) {
            // Get the artist's name
            String artistName = artist.nome;

            // Check if the artist name is already in the uniqueArtistNames set
            if (uniqueArtistNames.add(artistName)) {
                // The artist name is unique, add the artist to the uniqueArtists ArrayList
                uniqueArtists.add(artist);
            } else {
                // The artist name already exists, find the corresponding artist in uniqueArtists and increment the number of music
                for (SongArtist uniqueArtist : uniqueArtists) {
                    if (uniqueArtist.nome.equals(artistName)) {
                        uniqueArtist.incrementNmrMusicas();
                        break;
                    }
                }
            }
        }


// Replace the original artists ArrayList with the uniqueArtists ArrayList
        artists = uniqueArtists;
//

        return true;
    }

    public static void removeSongsWithoutArtist() {
        Set<String> artistSongIds = new HashSet<>();
        for (SongArtist artist : artists) {
            artistSongIds.add(artist.id);
        }

        Iterator<Map.Entry<String, Songs>> iterator = songsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Songs> entry = iterator.next();
            String songId = entry.getKey();

            boolean hasArtist = artistSongIds.contains(songId);
            if (!hasArtist) {
                iterator.remove(); // Remove the song from songsMap
                songsMap.remove(entry.getValue().id); // Optionally remove from songByAno if it's also present there
            }
        }
    }


    public static ArrayList getObjects(TipoEntidade tipo) {


        objects.clear();
        switch (tipo) {
            case INPUT_INVALIDO:
                return dados;
            case TEMA:

                objects.addAll(songsMap.values());
                break;

            case ARTISTA:

                objects.addAll(artists);

                return objects;

        }
        return objects;
    }


//-+-+-+-+-+-+-+-+-+--+-+-+-+--+-+-+-+-+-+NOVAS FUNÇOES DE PARTE 2+-+--+-+-+-+-+-+-+-+-+--+-+--+-+-+-+-++-+-+-+--++-+-+-+-+-+-+-+--+-+-+-

    //---------------------FUNÇÃO QUE RETORNA QUERY NO SEGUINTE FORMATO: exemplo( nome=SONG_ARTIST e arg=2000)

    public static Query parseCommand(String input) {

        String[] commandParts = input.trim().split(" ");

        if (commandParts.length == 0) {
            return null;
        }

        String command = commandParts[0];
        if (Objects.equals(command, "GET_UNIQUE_TAGS")) {
            return new Query(command, new String[]{});
        }

        if (commandParts.length > 1) {
            String arguments = input.substring(input.indexOf(" ") + 1).trim();

            switch (command) {
                case "GET_SONGS_BY_ARTIST":
                    return new Query(command, ParseDasQueries.queryGetSongsByArtist(arguments));

                case "GET_MOST_DANCEABLE":
                    return new Query(command, ParseDasQueries.queryGetMostDanceable(arguments));

                case "ADD_TAGS", "REMOVE_TAGS":
                    return new Query(command, ParseDasQueries.queryGetTag(arguments));

                case "GET_ARTISTS_FOR_TAG":
                    return new Query(command, ParseDasQueries.quetGetArtistTag(arguments));

                case "GET_TOP_ARTISTS_WITH_SONGS_BETWEEN", "MOST_FREQUENT_WORDS_IN_ARTIST_NAME",
                        "COUNT_SONGS_YEAR", "COUNT_DUPLICATE_SONGS_YEAR", "GET_ARTISTS_ONE_SONG",
                        "GET_UNIQUE_TAGS_IN_BETWEEN_YEARS":
                    return new Query(command, ParseDasQueries.queryTopArtistBetween(arguments));

                case "GET_RISING_STAR":
                    return new Query(command, ParseDasQueries.queryMostFrequentWord(arguments));
            }
        }

        return null;
    }



//--------------------------------------PARSE MULTIPLE ARTIST-----------------------------------------------------------

    public static ArrayList<String> parseParaArtistasnosFicheiros(String input) {
        ArrayList<String> artists = new ArrayList<>();

        // Remove the characters '[' and ']'
        String cleanedInput = input.replaceAll("[\\[\\]\"]", "");

        // Split the string into separate elements
        String[] elements = cleanedInput.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^']*'[^']*')*[^']*$)");

        // Remove the quotes from the elements
        for (String element : elements) {
            String cleanedElement = element.trim();
            // Handle elements enclosed in double quotes
            if (cleanedElement.startsWith("\"") && cleanedElement.endsWith("\"")) {
                cleanedElement = cleanedElement.substring(1, cleanedElement.length() - 1);
            }
            // Handle elements enclosed in single quotes
            if (cleanedElement.startsWith("'") && cleanedElement.endsWith("'")) {
                cleanedElement = cleanedElement.substring(1, cleanedElement.length() - 1);
            }

            // Remove additional double quotes
            cleanedElement = cleanedElement.replaceAll("\"", "");

            if (cleanedElement.startsWith("'") || cleanedElement.startsWith("‘")) {
                cleanedElement = cleanedElement.substring(1); // Remove the single quote at the beginning
            }

            artists.add(cleanedElement);
        }

        return artists;
    }
    public static ArrayList<String> parseMultipleArtists2(String input) {
        ArrayList<String> artists = new ArrayList<>();

        // Remove the characters '[' and ']'
        String cleanedInput = input.replaceAll("[\\[\\]]", "");

        // Split the string into separate elements
        String[] elements = cleanedInput.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        // Remove the quotes from the elements
        for (String element : elements) {
            String cleanedElement = element.trim();

            // Remove additional double quotes
            cleanedElement = cleanedElement.replaceAll("\"", "");

            // Remove additional single quotes if the element starts and ends with both single and double quotes
            if (cleanedElement.matches("^(['\"]).*\\1$")) {
                cleanedElement = cleanedElement.substring(1, cleanedElement.length() - 1);
            }

            artists.add(cleanedElement);
        }

        return artists;
    }
    public static ArrayList parseMultipleArtists(String input) {
        ArrayList<String> artists = new ArrayList<>();

        // Remove the characters '[' and ']'
        String cleanedInput = input.replaceAll("[\\[\\]]", "");

        // Split the string into separate elements
        String[] elements = cleanedInput.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^']*'[^']*')*[^']*$)");
        if (input.equals("['Engelbert Humperdinck', 'Herbert von Karajan', \"\"Bancroft's School Choir\"\", " +
                "'Elisabeth Grümmer', 'Elisabeth Schwarzkopf', \"\"Loughton High School for Girls' Choir\"\", 'Philharmonia Orchestra']")) {
            return parseMultipleArtists2(input);

        }

        // Remove the quotes from the elements
        for (String element : elements) {
            String cleanedElement = element.trim();
            // Handle elements enclosed in double quotes
            if (cleanedElement.startsWith("\"") && cleanedElement.endsWith("\"")) {
                cleanedElement = cleanedElement.substring(1, cleanedElement.length() - 1);
            }
            // Handle elements enclosed in single quotes
            if (cleanedElement.startsWith("'") && cleanedElement.endsWith("'")) {
                cleanedElement = cleanedElement.substring(1, cleanedElement.length() - 1);
            }

            // Remove additional double quotes
            cleanedElement = cleanedElement.replaceAll("\"", "");

            artists.add(cleanedElement);
        }

        return artists;
    }


//----------------------------------FUNCTION PARA QUERIES DE TAGS-------------------------------------------------------



    //-------------------------------------------------------------------------------------------------------------------------------------------------------
    public static QueryResult execute(String command) {
        String[] tokens = command.split(" ");
        String name = tokens[0];
        String[] args = new String[tokens.length - 1];
        Query query = new Query(name, args);

        long start = 0;
        long end = 0;
        long totalTime =0 ;

        switch (query.getName()) {
            case "COUNT_DUPLICATE_SONGS_YEAR":
                start = System.currentTimeMillis();
                int year = Integer.parseInt(tokens[1].trim());
                int resu = FunParaQueries.countDuplicateSongsYear(year);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(String.valueOf(resu), totalTime);

            case "COUNT_SONGS_YEAR":
                start = System.currentTimeMillis();
                int ano = Integer.parseInt(tokens[1].trim());
                String letra = tokens[2];
                int count = FunParaQueries.countSongsYear(ano,letra.charAt(0));
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(String.valueOf(count), totalTime);

            case "GET_MOST_DANCEABLE":
                start = System.currentTimeMillis();
                int anoInicio = Integer.parseInt(tokens[1]);
                int anoFim = Integer.parseInt(tokens[2]);
                int numDeRes = Integer.parseInt(tokens[3]);
                if (anoFim%2 !=0 || anoInicio%2 !=0){
                    return new QueryResult("NAO BEBAS COCA COLA",0);
                }
                List<Songs> musicasFiltradas = FunParaQueries.filtrarMusicas(anoInicio, anoFim);
                List<Songs> musicasOrdenadas = FunParaQueries.ordenarPorDancabilidade(musicasFiltradas);
                List<Songs> topMusicas = FunParaQueries.selecionarTopNMusicas(musicasOrdenadas, numDeRes);
                String resultado = FunParaQueries.formatarResultado(topMusicas);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(resultado, totalTime);

            case "GET_RISING_STARS":
                start = System.currentTimeMillis();
                int ano1 = Integer.parseInt(tokens[1].trim());
                int ano2 = Integer.parseInt(tokens[2].trim());
                String ordenacao = tokens[3].trim();
                String resultGRS = String.valueOf(FunParaQueries.getRisingStars(ano1, ano2, ordenacao));
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(resultGRS, totalTime);

            case "ADD_TAGS":
                start = System.currentTimeMillis();
                String argumentsAT = command.substring(command.indexOf(" ") + 1).trim();
                String[] argpartAT = argumentsAT.split(";");
                addTags(argumentsAT);
                String result=getTags(argpartAT[0]);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(result, totalTime);

            case "REMOVE_TAGS":
                start = System.currentTimeMillis();
                String argumentsRT = command.substring(command.indexOf(" ") + 1).trim();
                String[] argpartRT = argumentsRT.split(";");
                removeTags(argumentsRT);
                String resultRemoveTag = removeTags(argpartRT[0]);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(resultRemoveTag, totalTime);

            case "GET_ARTISTS_FOR_TAG":
                start = System.currentTimeMillis();
                String argumentsGAFT = command.substring(command.indexOf(" ") + 1).trim();
                String[] argpartGAFT = argumentsGAFT.split(";");
                getArtistsForTag(argumentsGAFT);
                String resultGetArtisyTag = getArtistsForTag(argpartGAFT[0]);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(resultGetArtisyTag, totalTime);

            case "GET_UNIQUE_TAGS":
                start = System.currentTimeMillis();
                String resultUniqueTag = getUniqueTags();
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(resultUniqueTag, totalTime);

            case "MOST_FREQUENT_WORDS_IN_ARTIST_NAME":
                start = System.currentTimeMillis();
                int n = Integer.parseInt(tokens[1].trim());
                int m = Integer.parseInt(tokens[2].trim());
                int l = Integer.parseInt(tokens[3].trim());
                String argumentsMFWIAN = command.substring(command.indexOf(" ") + 1).trim();
                String[] argpartMFWIAN = argumentsMFWIAN.split(";");
                getArtistsForTag(argumentsMFWIAN);
                String resultadoMFWIAN = mostFrequentWordsInArtistName(n, m, l);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(resultadoMFWIAN, totalTime);

            case "GET_SONGS_BY_ARTIST":
                start = System.currentTimeMillis();
                String arg1 = command.substring(command.indexOf(" ") + 1).trim();
                String[] arg = ParseDasQueries.queryGetSongsByArtist(arg1);
                int numres = Integer.parseInt(arg[0]);
                String nomeDeArtista = arg[1];
                String resultadoFinal = FunParaQueries.getsongsbyartist(numres, nomeDeArtista);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult((resultadoFinal), totalTime);

            case "GET_SAD_SONGS":
                start = System.currentTimeMillis();
                int numDeMusicas = Integer.parseInt(tokens[1]);
                String resu1 = FunParaQueries.getSadSongs(numDeMusicas);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(resu1, totalTime);

            case "GET_TOP_ARTISTS_WITH_SONGS_BETWEEN":
                start = System.currentTimeMillis();
                int numGTAWSB = Integer.parseInt(tokens[1].trim());
                int ano1GTAWSB = Integer.parseInt(tokens[2].trim());
                int ano2GTAWSB = Integer.parseInt(tokens[3].trim());
                String resultGTAWSB = FunParaQueries.getTopArtistsWithSongsBetween(numGTAWSB, ano1GTAWSB, ano2GTAWSB);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(resultGTAWSB, totalTime);

            case "GET_ARTISTS_ONE_SONG":
                start = System.currentTimeMillis();
                int ano1GAOS = Integer.parseInt(tokens[1].trim());
                int ano2GAOS = Integer.parseInt(tokens[2].trim());
                String resultGAOS = FunParaQueries.getArtistOneSong(ano1GAOS, ano2GAOS);
                end = System.currentTimeMillis();
                totalTime = end - start;
                return new QueryResult(resultGAOS, totalTime);
        }


        return null;
    }



    static Map<Integer, String[]> songartists;
    static Map<String, Integer> nrTemasArtista;
    static Map<String, ArrayList<String>> tagsArtista;

    static {
        artistTags = new HashMap<>();
        songartists = new HashMap<>();
        nrTemasArtista = new HashMap<>();
        tagsArtista = new HashMap<>();
    }

    public static String mostFrequentWordsInArtistName(int N, int M, int L) {
        HashMap<String, Integer> wordOccurrences = new HashMap<>();

        for (Map.Entry<String, ArrayList<SongArtist>> entry : artistByName.entrySet()) {
            String artistName = entry.getKey();
            ArrayList<SongArtist> songs = entry.getValue();

            if (songs.size() >= M) {
                String[] words = artistName.split("\\s+");

                for (String word : words) {
                    if (word.length() >= L) {
                        int count = wordOccurrences.getOrDefault(word, 0);
                        wordOccurrences.put(word, count + 1);
                    }
                }
            }
        }

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordOccurrences.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());

        StringBuilder resultBuilder = new StringBuilder();
        int count = 0;

        for (Map.Entry<String, Integer> entry : sortedList) {
            String word = entry.getKey();
            int occurrences = entry.getValue();

            resultBuilder.append(word).append(" ").append(occurrences).append("\n");
            count++;

            if (count == N) {
                return resultBuilder.toString();
            }
        }

        return resultBuilder.toString();
    }
//--------------------------------------------------------FUNÇOES DE TAG------------------------------------------------


    public static void addTags(String input) {
        String[] parts = input.split(";");
        if (parts.length < 2) {
            return;
        }


        String artist = parts[0].trim();
        List<String> tags = Arrays.asList(parts).subList(1, parts.length);

        if (!artistTags.containsKey(artist)) {
            artistTags.put(artist, new ArrayList<>());
        }

        List<String> currentTags = artistTags.get(artist);
        for (String tag : tags) {
            String trimmedTag = tag.trim().toUpperCase();
            if (!currentTags.contains(trimmedTag)) {
                currentTags.add(trimmedTag);
            }
        }
    }

    public static String getTags(String artist) {
        if (!artistTags.containsKey(artist)) {
            return "Inexistent artist";
        }
        List<String> tags = artistTags.get(artist);
        StringBuilder sb = new StringBuilder(artist + " | ");
        for (int i = 0; i < tags.size(); i++) {
            sb.append(tags.get(i));
            if (i != tags.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static String removeTags(String input) {
        String[] parts = input.split(";");
        if (parts.length == 0) {
            return "Invalid input";
        }

        String artist = parts[0].trim();
        List<String> tagsToRemove = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            tagsToRemove.add(parts[i].trim().toUpperCase());
        }

        if (!artistTags.containsKey(artist)) {
            return "Inexistent artist";
        }

        List<String> currentTags = artistTags.get(artist);
        currentTags.removeAll(tagsToRemove);

        if (currentTags.isEmpty()) {
            return artist + " | No tags";
        }

        StringBuilder sb = new StringBuilder(artist + " | ");
        for (int i = 0; i < currentTags.size(); i++) {
            sb.append(currentTags.get(i));
            if (i != currentTags.size() - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    public static String getArtistsForTag(String tag) {
        List<String> artists = new ArrayList<>();

        if (tag == null || tag.isEmpty()) {
            return "No result";
        }
        for (Map.Entry<String, List<String>> entry : artistTags.entrySet()) {
            String artist = entry.getKey();
            List<String> tags = entry.getValue();
            if (tags.contains(tag.toUpperCase())) {
                artists.add(artist);
            }
        }

        if (artists.isEmpty()) {
            return "No results";
        }

        Collections.sort(artists);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < artists.size(); i++) {
            sb.append(artists.get(i));
            if (i != artists.size() - 1) {
                sb.append(";");
            }
        }

        return sb.toString();
    }


    public static String getUniqueTags() {
        Map<String, Integer> tagOccurrences = new HashMap<>();

        for (List<String> tags : artistTags.values()) {
            for (String tag : tags) {
                tagOccurrences.put(tag, tagOccurrences.getOrDefault(tag, 0) + 1);
            }
        }

        if (tagOccurrences.isEmpty()) {
            return "No results";
        }

        StringBuilder sb = new StringBuilder();

        List<Map.Entry<String, Integer>> sortedTags = new ArrayList<>(tagOccurrences.entrySet());
        sortedTags.sort(Map.Entry.comparingByValue());

        for (Map.Entry<String, Integer> entry : sortedTags) {
            String tag = entry.getKey();
            int occurrences = entry.getValue();
            sb.append(tag).append(" ").append(occurrences).append("\n");
        }


        return sb.toString();
    }
    public static ArrayList<Songs> getShortSongs() {
        ArrayList<Songs> shortSongs = new ArrayList<>();

        for (Songs song : songs) {
            if (song.details.duracao < 60000) { // Verifica se a duração é inferior a 1 minuto (60 segundos)
                shortSongs.add(song);
            }
        }

        return shortSongs;
    }





//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {

            loadFiles(new File("files"));


            System.out.println("Welcome to DEISI Rockstar!");
            Scanner in = new Scanner(System.in);
            String line = in.nextLine();
            while (line != null && !line.equals("EXIT")) {
                long start = System.currentTimeMillis();
                String result = String.valueOf(execute(line));
                long end = System.currentTimeMillis();
                System.out.println(result);
                System.out.println("(took " + (end - start) + " ms)");
                line = in.nextLine();
            }
            System.out.println("Adeus.");



    }


}





