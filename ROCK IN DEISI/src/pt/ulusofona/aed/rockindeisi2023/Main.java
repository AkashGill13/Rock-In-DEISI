package pt.ulusofona.aed.rockindeisi2023;

import java.io.*;
import java.util.*;


public class Main {

    //--------------------------------------------loadfiles rapido----------------------------------------------------------
    static ArrayList<SongArtist> artists = new ArrayList<>(); //arraylist para guardar os artistas e seu id
    static LinkedHashMap<String, SongDetails> detailsMap = new LinkedHashMap<>(); //Linked Hashmap para guardar dados de details.txt
    static LinkedHashMap<String, Songs> songsMap = new LinkedHashMap<>(); //Linked hasmap para guardar dados de song.txt
    static LinkedHashMap<Integer, Songs> songByAno = new LinkedHashMap<>(); //Linked hashmap para guardar songs mas key= ano lancamento
    static LinkedHashMap<String, ArrayList<SongArtist>> artistMap = new LinkedHashMap<>(); //Linked hasmap que guarda arrayist de songs key = id
    static LinkedHashMap<String,SongArtist> artistByName = new LinkedHashMap<>(); // Linked Hashmap para guardar artistas key = nome
    static ArrayList<Songs> songs = new ArrayList<>();
    static ArrayList<Object> objects = new ArrayList<>(); // guarda objectos a retornar em GETOBJECTS

    static ArrayList<EstaticFicheiro> dados = new ArrayList<>(); // guarda dados estaticticos de cada ficheiro


    public static boolean loadFiles(File folder) {
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
                    if (detailsMap.containsKey(id)) {
                        // Song with the same ID already exists, mark it as a duplicate
                        detailsLinhasNOK++;
                        if (detailsPrimeiraLinhaNOK == -1) {
                            detailsPrimeiraLinhaNOK = linhaAtualDetails;
                        }
                        continue;
                    }
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
            while ((line = reader.readLine()) != null) {
                linhaAtualSongs++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("@");
                if (parts.length == 3) {
                    String id = parts[0].trim();
                    if (songsMap.containsKey(id)) {
                        songsLinhasNOK++;
                        if (songsPrimeiraLinhaNOK == -1) {
                            songsPrimeiraLinhaNOK = linhaAtualSongs;
                        }
                        continue;
                    }
                    String nome = parts[1].trim();
                    Integer anoLancamento = Integer.valueOf(parts[2].trim());

                    // Check if the song ID exists in detailsMap
                    if (detailsMap.containsKey(id)) {
                        Songs song = new Songs(id, nome, anoLancamento);
                        songsLinhasOK++;
                        songs.add(song);
                        songsMap.put(id, song);
                        songByAno.put(anoLancamento, song);

                    } else {
                        songsLinhasOK++;
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
                ArrayList<String> artistNamesList = parseMultipleArtists(parts[1]);

                if (artistNames.length > 1 && !artistNames[0].contains("\"")) {
                    continue;
                }
                boolean idExists = songsMap.containsKey(id);
                if (!idExists) {
                    continue;
                }
                ArrayList<SongArtist> artistList = new ArrayList<>();

                for (String a : artistNamesList) {

                    a = a.trim();

                    SongArtist artist = new SongArtist(id, a.trim());
                    artists.add(artist);
                    artistList.add(artist);
                    existingArtistNames.add(a);
                    artistByName.put(a,artist);
                }


                artistMap.put(id, artistList);



            }

        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        EstaticFicheiro file1 = new EstaticFicheiro("songs.txt", songsLinhasOK, songsLinhasNOK, songsPrimeiraLinhaNOK);
        dados.add(file1);
        EstaticFicheiro file2 = new EstaticFicheiro("song_details.txt", detailsLinhasOK, detailsLinhasNOK, detailsPrimeiraLinhaNOK);
        dados.add(file2);
        EstaticFicheiro file3 = new EstaticFicheiro("song_artists.txt", artistsLinhasOK, artistsLinhasNOK, artistsPrimeiraLinhaNOK);
        dados.add(file3);

        removeSongsWithoutArtist();
        associarDetalhes();
        contarNumArtistas();
        contarNumMusicas();

//// Associate Songs with SongDetails
//        for (Songs song : songsMap.values()) {
//            SongDetails detail = detailsMap.get(song.id);
//            if (detail != null) {
//                song.details = detail;
//            }
//        }

//        for (Songs song : songsMap.values()) {
//            ArrayList<SongArtist> songArtists = artistMap.get(song.id);
//            if (songArtists != null) {
//                song.numArt.addAll(songArtists);
//            }
//        }


//
//        HashSet<String> uniqueArtistNames = new HashSet<>();
//// Create a new ArrayList to store unique artists
//        ArrayList<SongArtist> uniqueArtists = new ArrayList<>();
//
//// Loop through the artists ArrayList
//        for (SongArtist artist : artists) {
//            // Get the artist's name
//            String artistName = artist.nome;
//
//            // Check if the artist name is already in the uniqueArtistNames set
//            if (uniqueArtistNames.add(artistName)) {
//                // The artist name is unique, add the artist to the uniqueArtists ArrayList
//                uniqueArtists.add(artist);
//            } else {
//                // The artist name already exists, find the corresponding artist in uniqueArtists and increment the number of music
//                for (SongArtist uniqueArtist : uniqueArtists) {
//                    if (uniqueArtist.nome.equals(artistName)) {
//                        uniqueArtist.incrementNmrMusicas();
//                        break;
//                    }
//                }
//            }
//        }
//// Replace the original artists ArrayList with the uniqueArtists ArrayList
//        artists = uniqueArtists;
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
    public static void associarDetalhes(){
        for (Songs song : songsMap.values()) {
            SongDetails detail = detailsMap.get(song.id);
            if (detail != null) {
                song.details = detail;
            }
        }
    }
    public static void contarNumArtistas(){
        for (Songs song : songsMap.values()) {
            ArrayList<SongArtist> songArtists = artistMap.get(song.id);
            if (songArtists != null) {
                song.numArt.addAll(songArtists);
            }
        }
    }
    public static void contarNumMusicas(){

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
                    return new Query(command, queryGetSongsByArtist(arguments));

                case "GET_MOST_DANCEABLE":
                    return new Query(command, queryGetMostDanceable(arguments));

                case "ADD_TAGS", "REMOVE_TAGS":
                    return new Query(command, queryGetTag(arguments));

                case "GET_ARTISTS_FOR_TAG":
                    return new Query(command, quetGetArtistTag(arguments));

                case "GET_TOP_ARTISTS_WITH_SONGS_BETWEEN", "MOST_FREQUENT_WORDS_IN_ARTIST_NAME",
                        "COUNT_SONGS_YEAR", "COUNT_DUPLICATE_SONGS_YEAR", "GET_ARTISTS_ONE_SONG",
                        "GET_UNIQUE_TAGS_IN_BETWEEN_YEARS":
                    return new Query(command, queryTopArtistBetween(arguments));

                case "GET_RISING_STAR":
                    return new Query(command, queryMostFrequentWord(arguments));
            }
        }

        return null;
    }

    private static String[] queryMostFrequentWord(String arguments) {
        int firstSpaceIndex = arguments.indexOf(" ");
        int secondSpaceIndex = arguments.indexOf(" ", firstSpaceIndex + 1);

        String count = arguments.substring(0, firstSpaceIndex);
        String count1 = arguments.substring(firstSpaceIndex + 1, secondSpaceIndex);
        String name = arguments.substring(secondSpaceIndex + 1).trim();

        return new String[]{count, count1, name};
    }


    private static String[] queryTopArtistBetween(String arguments) {
        return arguments.split(" ");
    }

    private static String[] quetGetArtistTag(String arguments) {
        return new String[]{arguments};
    }

    private static String[] queryGetTag(String arguments) {
        return arguments.split(";");
    }


    private static String[] queryGetSongsByArtist(String arguments) {
        int indice = arguments.indexOf(" ");

        String count = arguments.substring(0, indice);
        String name = arguments.substring(indice).trim(); // Adjusted substring indices and added trim()
        return new String[]{count, name};
    }

    private static String[] queryGetMostDanceable(String arguments) {
        return arguments.split(" ");
    }

    //
////------------------ESTA FUNÇÃO ESTA UM POUCA CONFUSA E É INDEPENDENTE--------------------------------------------------
//


//    //--------------------------    FUNÇÃO  QUE MOSTRA RESULTADO  FUNCIONA BEM OS QUERY IMPLEMENTADOS PARA JÁ-----------

    static Map<String, List<String>> artistTags;

    static {
        artistTags = new HashMap<>();
    }


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


    //-------------------------------------------------------------------------------------------------------------------------------------------------------
    public static QueryResult execute(String command) {
        String[] tokens = command.split(" ");
        String name = tokens[0];
        String[] args = new String[tokens.length - 1];
        Query query = new Query(name, args);
//        String[] inputStrings = tokens[1].split(";"); // Split the arguments using semicolon


        switch (query.getName()) {
            case "COUNT_DUPLICATE_SONGS_YEAR":
                int year = Integer.parseInt(tokens[1].trim());
                int resu = countDuplicateSongsYear(year);
                return new QueryResult(String.valueOf(resu), 0);
            case "COUNT_SONGS_YEAR":
                int ano = Integer.parseInt(tokens[1].trim());
                int count = countSongsYear(ano);
                return new QueryResult(String.valueOf(count), 0);
            case "GET_MOST_DANCEABLE":
                int anoInicio = Integer.parseInt(tokens[1]);
                int anoFim = Integer.parseInt(tokens[2]);
                int numDeRes = Integer.parseInt(tokens[3]);
                List<Songs> musicasFiltradas = filtrarMusicas(anoInicio, anoFim);
                List<Songs> musicasOrdenadas = ordenarPorDancabilidade(musicasFiltradas);
                List<Songs> topMusicas = selecionarTopNMusicas(musicasOrdenadas, numDeRes);
                String resultado = formatarResultado(topMusicas);
                return new QueryResult(resultado, 0);
            case "GET_RISING_STARS":
                int ano1 = Integer.parseInt(tokens[1].trim());
                int ano2 = Integer.parseInt(tokens[2].trim());
                String ordenacao = tokens[3].trim();
                String resultGRS = String.valueOf(getRisingStars(ano1, ano2, ordenacao));
                return new QueryResult(resultGRS, 0);
            case "ADD_TAGS":
                String argumentsAT = command.substring(command.indexOf(" ") + 1).trim();
                String[] argpartAT = argumentsAT.split(";");
                addTags(argumentsAT);
                return new QueryResult(getTags(argpartAT[0]), 0);
            case "REMOVE_TAGS":
                String argumentsRT = command.substring(command.indexOf(" ") + 1).trim();
                String[] argpartRT = argumentsRT.split(";");
                removeTags(argumentsRT);
                return new QueryResult(removeTags(argpartRT[0]), 0);

            case "GET_ARTISTS_FOR_TAG":
                String argumentsGAFT = command.substring(command.indexOf(" ") + 1).trim();
                String[] argpartGAFT = argumentsGAFT.split(";");
                getArtistsForTag(argumentsGAFT);
                return new QueryResult(getArtistsForTag(argpartGAFT[0]), 0);

            case "GET_UNIQUE_TAGS":
                return new QueryResult(getUniqueTags(), 0);


            case "MOST_FREQUENT_WORDS_IN_ARTIST_NAME":
                int n = Integer.parseInt(tokens[1].trim());
                int m = Integer.parseInt(tokens[2].trim());
                int l = Integer.parseInt(tokens[3].trim());
                String argumentsMFWIAN = command.substring(command.indexOf(" ") + 1).trim();
                String[] argpartMFWIAN = argumentsMFWIAN.split(";");
                getArtistsForTag(argumentsMFWIAN);
                String resultadoMFWIAN = mostFrequentWordsInArtistName(n, m, l);
                return new QueryResult(resultadoMFWIAN, 0);
        }


        return null;
    }

    //// --------------------------Função Para conta numero de Musica do certo ano FUNÇÃO EM SI ESTA FUNCINAR BEM------------
    public static int countSongsYear(int year) {
        int totalTemas = 0;
        for (Songs song : songsMap.values()) {
            if (song.anoLancamento == year && artistMap.containsKey(song.id)) {

                totalTemas++;
            }

        }
        return totalTemas;
    }


    //
//-----------------------------FUNÇAO PARA CONTAR MUSICAS COM MESMO NOME NO CERTO ANO---------------------------------
//    public static int countDuplicateSongsYear(int ano) {
//        int temasDuplicados = 0;
//        HashSet<String> temas = new HashSet<>();
//
//        for (Songs song : songByAno.containsKey(ano)) {
//            if (song.anoLancamento == ano && detailsMap.containsKey(song.id) && temas.contains(song.nome) &&
//                    artistMap.containsKey(song.id)) {
//                temasDuplicados++;
//            } else if (song.anoLancamento == ano && detailsMap.containsKey(song.id)) {
//                temas.add(song.nome);
//            }
//        }
//
//        return temasDuplicados;
//    }
    public static int countDuplicateSongsYear(int ano) {
        int temasDuplicados = 0;
        HashSet<String> temas = new HashSet<>();

        for (Songs song : songsMap.values()) {
            if (song.anoLancamento == ano && detailsMap.containsKey(song.id) && temas.contains(song.nome) &&
                    artistMap.containsKey(song.id)) {
                temasDuplicados++;
            } else if (song.anoLancamento == ano && detailsMap.containsKey(song.id) && artistMap.containsKey(song.id)) {
                temas.add(song.nome);
            }
        }

        return temasDuplicados;
    }


    //------------------------------FUNÇÃO PARA QUERY  dancabilidade--------------------------------------------------------
    public static List<Songs> filtrarMusicas(int anoInicio, int anoFim) {
        List<Songs> musicasFiltradas = new ArrayList<>();
        for (Map.Entry<String, Songs> entry : songsMap.entrySet()) {
            Songs musica = entry.getValue();
            int ano = musica.anoLancamento;
            if (ano >= anoInicio && ano <= anoFim) {
                musicasFiltradas.add(musica);
            }
        }
        return musicasFiltradas;
    }

    public static List<Songs> ordenarPorDancabilidade(List<Songs> musicas) {
        List<Songs> musicasOrdenadas = new ArrayList<>(musicas);
        Collections.sort(musicasOrdenadas, new Comparator<Songs>() {
            @Override
            public int compare(Songs musica1, Songs musica2) {
                double dancabilidade1 = musica1.details.dancabilidade;
                double dancabilidade2 = musica2.details.dancabilidade;
                return Double.compare(dancabilidade2, dancabilidade1); // Ordena em ordem decrescente
            }
        });
        return musicasOrdenadas;
    }

    public static List<Songs> selecionarTopNMusicas(List<Songs> musicas, int n) {
        return musicas.subList(0, Math.min(n, musicas.size()));
    }

    public static String formatarResultado(List<Songs> musicas) {
        StringBuilder resultado = new StringBuilder();
        for (Songs musica : musicas) {
            resultado.append(musica.nome).append(" : ")
                    .append(musica.anoLancamento).append(" : ")
                    .append(musica.details.dancabilidade).append("\n");
        }
        return resultado.toString();
    }


    //----------------------------------------------------------------------------------------------------------
    public static ArrayList<String> getRisingStars(int ano1, int ano2, String ordenacao) {
        ArrayList<String> artistsList = new ArrayList<>();
        HashMap<String, ArrayList<SongArtist>> artistas = new HashMap<>();
        HashSet<String> artistasNaoIncluidos = new HashSet<>();

        for (Map.Entry<String, Songs> entry : songsMap.entrySet()) {
            Songs song = entry.getValue();
            if (song.anoLancamento >= ano1 && song.anoLancamento <= ano2) {
                if (artistMap.get(song.id) != null) {
                    String[] artistasDoTema = artistMap.get(song.id).toString().split(",");
                    for (String art : artistasDoTema) {
                        ArrayList<SongArtist> temasDoArtista = artistas.get(art);
                        if (temasDoArtista != null) {
                            temasDoArtista.add(new SongArtist(song.id, song.nome));
                        } else {
                            temasDoArtista = new ArrayList<>();
                            temasDoArtista.add(new SongArtist(song.id, song.nome    ));
                            artistas.put(art, temasDoArtista);
                        }
                    }
                }
            } else if (artistas.get(song.id) != null) {
                String[] artistasDoTema = new String[]{artistas.get(song.id).toString()};
                Collections.addAll(artistasNaoIncluidos, artistasDoTema);
            }
        }

        ArrayList<Map.Entry<String, Integer>> ordenar = ordenarRising(artistasNaoIncluidos, artistas, ordenacao);
        int limit = Math.min(15, ordenar.size());  // Limit the result to the top 15 artists
        for (int i = 0; i < limit; i++) {
            artistsList.add(ordenar.get(i).getKey());
        }

        return artistsList;
    }

    public static ArrayList<Map.Entry<String, Integer>> ordenarRising(HashSet<String> artistasNaoIncluidos,
                                                                      HashMap<String, ArrayList<SongArtist>> artistas, String ordenacao) {
        HashMap<String, Integer> popularidade = new HashMap<>();
        ArrayList<Map.Entry<String, Integer>> ordenar = new ArrayList<>();

        for (String art : artistasNaoIncluidos) {
            artistas.remove(art);
        }

        for (Map.Entry<String, ArrayList<SongArtist>> artista : artistas.entrySet()) {
            ArrayList<SongArtist> songs = artista.getValue();
            if (!songs.isEmpty()) {
                double media = 0.0;
                for (SongArtist song : songs) {
                    media += detailsMap.get(song.id) == null ? 0 : detailsMap.get(song.id).popularidade;
                }
                media /= songs.size();
                popularidade.put(artista.getKey(), (int) Math.ceil(media));
            }
        }

        ordenar.addAll(popularidade.entrySet());

        if (ordenacao.equals("ASC")) {
            ordenar.sort(new MapSortCresc());
        } else {
            ordenar.sort(new MapSortDec());
        }

        return ordenar;
    }

//-----------------------------------------------------------------------------------------------------------------------

    static Map<Integer, String[]> songartists;
    static Map<String, Integer> nrTemasArtista;
    static Map<String, ArrayList<String>> tagsArtista;

    static {
        artistTags = new HashMap<>();
        songartists = new HashMap<>();
        nrTemasArtista = new HashMap<>();
        tagsArtista = new HashMap<>();
    }

    public static String mostFrequentWordsInArtistName(int n, int m, int l) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Map.Entry<String, Integer>> list = nocorrencias(m, l);
        list.sort(new MapSortCresc());
        int index = Math.max(list.size() - n, 0);

        for (int i = index; i < list.size(); ++i) {
            Map.Entry<String, Integer> entry = list.get(i);
            String key = entry.getKey();
            sb.append(key).append(" ").append(entry.getValue()).append("\n");
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public static String[] containsE(String artist) {
        String[] artistNames = artist.split("&");
        ArrayList<String> names = new ArrayList<>();
        for (String name : artistNames) {
            String[] nameParts = name.split(" ");
            Collections.addAll(names, nameParts);
        }
        return names.toArray(new String[0]);
    }

    public static HashMap<String, Integer> verificaTamanho(String[] artistNames, int l) {
        HashMap<String, Integer> occurrenceCount = new HashMap<>();
        for (String word : artistNames) {
            word = word.trim();
            if (word.length() >= l) {
                occurrenceCount.put(word, occurrenceCount.getOrDefault(word, 0) + 1);
            }
        }
        return occurrenceCount;
    }

    public static ArrayList<Map.Entry<String, Integer>> nocorrencias(int m, int l) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>();
        HashMap<String, Integer> occurrenceCount = new HashMap<>();

        for (Songs song : songs) {
            if (songartists.containsKey(song.getId())) {
                String[] artists = songartists.get(song.getId());
                for (String artist : artists) {
                    if (nrTemasArtista.containsKey(artist) && nrTemasArtista.get(artist) >= m) {
                        String[] artistNames;
                        if (artist.contains("&")) {
                            artistNames = containsE(artist);
                        } else {
                            artistNames = artist.split(" ");
                        }
                        occurrenceCount = verificaTamanho(artistNames, l);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> entry : occurrenceCount.entrySet()) {
            list.add(entry);
        }

        return list;
    }

    public static HashMap<String, Integer> ocorrencias() {
        HashMap<String, Integer> occurrenceCount = new HashMap<>();
        for (Map.Entry<String, ArrayList<String>> entry : tagsArtista.entrySet()) {
            String tag = entry.getKey();
            ArrayList<String> artists = entry.getValue();
            for (String artist : artists) {
                occurrenceCount.put(tag, occurrenceCount.getOrDefault(tag, 0) + 1);
            }
        }
        return occurrenceCount;
    }

public static ArrayList<String> parseMultipleArtists(String input) {
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
//public static ArrayList<String> parseMultipleArtists1(String input) {
//    ArrayList<String> artists = new ArrayList<>();
//
//    // Remove the characters '[' and ']'
//    String cleanedInput = input.replaceAll("[\\[\\]\"]", "");
//
//    // Split the string into separate elements
//    String[] elements = cleanedInput.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^']*'[^']*')*[^']*$)");
//
//    // Remove the quotes from the elements
//    for (String element : elements) {
//        String cleanedElement = element.trim();
//        // Handle elements enclosed in double quotes
//        if (cleanedElement.startsWith("\"") && cleanedElement.endsWith("\"")) {
//            cleanedElement = cleanedElement.substring(1, cleanedElement.length() - 1);
//        }
//        // Handle elements enclosed in single quotes
//        if (cleanedElement.startsWith("'") && cleanedElement.endsWith("'")) {
//            cleanedElement = cleanedElement.substring(1, cleanedElement.length() - 1);
//        }
//
//        // Remove additional double quotes
//        cleanedElement = cleanedElement.replaceAll("\"", "");
//
//        if (cleanedElement.startsWith("'")) {
//            cleanedElement = cleanedElement.substring(1); // Remove the single quote at the beginning
//        }
//
//        artists.add(cleanedElement);
//    }
//
//    return artists;
//}





    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        loadFiles(new File("files"));
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println(executionTime);
        System.out.println(getObjects(TipoEntidade.TEMA));
//        System.out.println(parseMultipleArtists("['aaa']"));
//        System.out.println(parseMultipleArtists("['aaa', 'bbb']"));
//        System.out.println(parseMultipleArtists("['a,aa', 'bbb']"));
//        System.out.println(parseMultipleArtists("['aaa', 'bb b']"));
//        System.out.println(parseMultipleArtists("[\"\"a'aa\"\", 'bbb']"));
//        System.out.println(parseMultipleArtists("[\"\"a' aa\"\", 'b, bb', 'ccc']"));

    }


}





