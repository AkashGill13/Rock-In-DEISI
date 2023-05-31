package pt.ulusofona.aed.rockindeisi2023;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Songs {



    String id, nome;
    int nmrArtistas = 0;
    int anoLancamento;
    ArrayList<SongArtist> numArt = new ArrayList<>();

    List<String> artistIds; // Field to store the artist IDs

    SongDetails details;


    Songs() {

    }

    public Songs(String nome, int anoLancamento, Double dancabilidade) {
    }

    public Songs(String id, String nome, int anoLancamento, String[] artistArray) {
    }

    void incrementNmrArtistas() {
        nmrArtistas++;
    }


    Songs(String id, String nome, int anoLancamento) {
        this.id = id;
        this.nome = nome;
        this.anoLancamento = anoLancamento;
        this.artistIds = new ArrayList<>();
    }
    Songs(int numart1){
        this.nmrArtistas = numart1;
    }


    @Override
    public String toString() {



        if (anoLancamento >= 1995) {

//       CALCULO DE MILISEGUNDOS PARA MINUTOS : SEGUNDOS

        Duration duration = Duration.ofMillis(details.duracao);
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        String dur = minutes + ":" + seconds;
        String[] parts = dur.split(":");
        int min = Integer.parseInt(parts[0]);
        int sec = Integer.parseInt(parts[1]);
        String formattedTime = String.format("%d:%02d", min, sec);


        if (anoLancamento >= 1995 && anoLancamento < 2000) {
            return id + " | " + nome + " | " + anoLancamento + " | " + formattedTime + " | " + details.popularidade;

        }
        if (anoLancamento >= 2000) {
            if (numArt.size()==0){
                return  id + " | " + nome + " | " + anoLancamento + " | " + formattedTime + " | " + details.popularidade + " | " + 1;

            }
            return  id + " | " + nome + " | " + anoLancamento + " | " + formattedTime + " | " + details.popularidade + " | " +numArt.size();

            }

        }

        return   id + " | " + nome + " | " + anoLancamento;

    }

    public Object getId() {
        return this.id=id;
    }



    public Object getDetails() {
        return details;
    }

    public void setDetails(SongDetails detail) {
    }
    public void addArtistId(String artistId) {
        artistIds.add(artistId);
    }
    public List<String> getArtistIds() {
        return new ArrayList<>(artistIds);
    }


    public String getSongName() {
        return nome;
    }

     List<Songs> songs;

    public Songs(List<Songs> songs) {
        this.songs = songs;
    }

    public List<Songs> getSongs() {
        return songs;
    }

}

