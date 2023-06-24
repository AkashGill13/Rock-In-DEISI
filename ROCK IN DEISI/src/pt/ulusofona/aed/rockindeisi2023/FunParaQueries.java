package pt.ulusofona.aed.rockindeisi2023;

import java.util.*;

public class FunParaQueries {

    public static int countSongsYear(int year, char ignoreLetter) {
        int totalTemas = 0;
        for (Songs song : Main.songsMap.values()) {
            char firstChar = Character.toLowerCase(song.nome.charAt(0));
            char ignoreChar = Character.toLowerCase(ignoreLetter);

            if (firstChar != ignoreChar) {
                if (song.anoLancamento == year && Main.artistMap.containsKey(song.id)) {
                    totalTemas++;
                }
            }
        }
        return totalTemas;
    }

    public static int countDuplicateSongsYear(int ano) {
        int temasDuplicados = 0;
        HashSet<String> temas = new HashSet<>();

        for (Songs song : Main.songsMap.values()) {
            if (song.anoLancamento == ano && Main.detailsMap.containsKey(song.id) && temas.contains(song.nome) &&
                    Main.artistMap.containsKey(song.id)) {
                temasDuplicados++;
            } else if (song.anoLancamento == ano && Main.detailsMap.containsKey(song.id) && Main.artistMap.containsKey(song.id)) {
                temas.add(song.nome);
            }
        }

        return temasDuplicados;
    }


    public static String getsongsbyartist(int num, String nomeArtista) {
        StringBuilder result = new StringBuilder();
        int counter = 0;

        if (Main.artistByName.get(nomeArtista) != null) {
            ArrayList<SongArtist> abc = Main.artistByName.get(nomeArtista);
            for (Map.Entry<String, Songs> music : Main.songsMap.entrySet()) {
                for (SongArtist art : abc) {
                    if (music.getValue().id.equals(art.id)) {
                        result.append(music.getValue().nome).append(" : ").append(music.getValue().anoLancamento).append("\n");
                        counter++;

                    }
                    if (counter == num) {
                        break;
                    }
                }
                if (counter == num) {
                    break;
                }
            }
        }

        if (result.length() == 0) {
            result = new StringBuilder("No songs");
        }

        return result.toString();
    }


    public static String getArtistOneSong(int ano1, int ano2) {
        if (ano1 >= ano2) {
            return "Invalid period";
        }
        // Conta o número de musicas lançadas por cada artista no intervalo de tempo dado
        HashMap<String, Integer> artistSongCount = new HashMap<>();
        LinkedHashMap<String, String> artistNameById = new LinkedHashMap<>();
        LinkedHashMap<String, String> songNameById = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> releaseYearById = new LinkedHashMap<>();
        StringBuilder resultBuilder = new StringBuilder();

        for (Map.Entry<String, Songs> entry : Main.songsMap.entrySet()) {
            String artistId = entry.getKey();
            Songs song = entry.getValue();
            int songYear = song.anoLancamento;

            if (songYear >= ano1 && songYear <= ano2) {
                int count = artistSongCount.getOrDefault(artistId, 0) + 1;
                artistSongCount.put(artistId, count);
                artistNameById.put(artistId, Main.artistMap.get(artistId).get(0).getArtistName());
                songNameById.put(artistId, song.nome);
                releaseYearById.put(artistId, songYear);
            }
        }

        // Filtra os artistas com apenas uma música e acessa os nomes e os detalhes da música.
        HashSet<String> artistsWithOneSong = new HashSet<>();
        List<String> lastCharSpecial = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : artistSongCount.entrySet()) {
            String artistId = entry.getKey();
            int songCount = entry.getValue();

            if (songCount == 1) {
                String artistName = artistNameById.get(artistId);
                if (artistName != null && !artistsWithOneSong.contains(artistName)) {
                    artistsWithOneSong.add(artistName);
                    String songName = songNameById.get(artistId);
                    int releaseYear = releaseYearById.get(artistId);
                    if (artistName.charAt(0) == '$') {
                        lastCharSpecial.add(artistName + " | " + songName + " | " + releaseYear);
                    } else {
                        String line = artistName + " | " + songName + " | " + releaseYear;
                        resultBuilder.append(line).append("\n");
                    }
                }
            }
        }

        // Ordena os nomes por ordem alfabética (ignorando o caracter especial).
        List<String> resultLines = new ArrayList<>(Arrays.asList(resultBuilder.toString().split("\n")));
        resultLines.sort(null);

        // Adiciona os artistas com o caracter especial ao final da lista.
        resultLines.addAll(lastCharSpecial);

        System.out.println(resultLines.size());
        // Junta as linhas com o delimitador "\n" e retorna o resultado.
        return String.join("\n", resultLines);
    }



    public static List<Songs> filtrarMusicas(int anoInicio, int anoFim) {
        List<Songs> musicasFiltradas = new ArrayList<>();
        for (Map.Entry<String, Songs> entry : Main.songsMap.entrySet()) {
            Songs musica = entry.getValue();
            int ano = musica.anoLancamento;
            if (ano >= anoInicio && ano <= anoFim) {  // so adicionar musicas entre anos pedidos
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

    public static String getSadSongs(int count) {
        StringBuilder leastDanceableSongs = new StringBuilder();

        List<SongDetails> sortedDetails = new ArrayList<>(Main.detailsMap.values());
        sortedDetails.sort(Comparator.comparingDouble(details -> details.dancabilidade));

        int songCount = 0;
        for (SongDetails details : sortedDetails) {
            String songId = details.getId();

            if (Main.songsMap.containsKey(songId)) {
                Songs song = Main.songsMap.get(songId);
                for (SongArtist artist : Main.artistMap.get(songId)) {
                    leastDanceableSongs.append(songCount + 1 + " - ").append("Song: '").append(song.nome).append("' by ").append(artist.nome).append("\n");

                    songCount++;

                    if (songCount >= count) {
                        return leastDanceableSongs.toString();
                    }
                }
            }
        }

        return leastDanceableSongs.toString();
    }

    public static ArrayList<String> getRisingStars(int ano1, int ano2, String ordenacao) {
        ArrayList<String> artistsList = new ArrayList<>();
        HashMap<String, ArrayList<SongArtist>> artistas = new HashMap<>();
        HashSet<String> artistasNaoIncluidos = new HashSet<>();

        for (Map.Entry<String, Songs> entry : Main.songsMap.entrySet()) {
            Songs song = entry.getValue();
            if (song.anoLancamento >= ano1 && song.anoLancamento <= ano2) {
                if (Main.artistMap.get(song.id) != null) {
                    String[] artistasDoTema = Main.artistMap.get(song.id).toString().split(",");
                    for (String art : artistasDoTema) {
                        ArrayList<SongArtist> temasDoArtista = artistas.get(art);
                        if (temasDoArtista != null) {
                            temasDoArtista.add(new SongArtist(song.id, song.nome));
                        } else {
                            temasDoArtista = new ArrayList<>();
                            temasDoArtista.add(new SongArtist(song.id, song.nome));
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
                    media += Main.detailsMap.get(song.id) == null ? 0 : Main.detailsMap.get(song.id).popularidade;
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


    public static String getTopArtistsWithSongsBetween(int num, int A, int B) {
        TreeMap<Integer, ArrayList<String>> artistSongCount = new TreeMap<>(Collections.reverseOrder());

        for (Map.Entry<String, ArrayList<SongArtist>> entry : Main.artistByName.entrySet()) {
            String artistName = entry.getKey();
            ArrayList<SongArtist> songs = entry.getValue();

            if (songs.size() >= A && songs.size() <= B) {
                int songCount = songs.size();
                ArrayList<String> artists = artistSongCount.getOrDefault(songCount, new ArrayList<>());
                artists.add(artistName);
                artistSongCount.put(songCount, artists);
            }
        }

        StringBuilder resultBuilder = new StringBuilder();
        int count = 0;

        for (Map.Entry<Integer, ArrayList<String>> entry : artistSongCount.entrySet()) {
            int songCount = entry.getKey();
            ArrayList<String> artists = entry.getValue();

            for (String artistName : artists) {
                resultBuilder.append(artistName).append(" ").append(songCount).append("\n");
                count++;

                if (count == num) {
                    return resultBuilder.toString();
                }
            }
        }

        if (count == 0) {
            return "No results";
        }

        return resultBuilder.toString();
    }





}
