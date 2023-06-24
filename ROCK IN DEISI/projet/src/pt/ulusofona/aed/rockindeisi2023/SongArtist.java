package pt.ulusofona.aed.rockindeisi2023;

import java.util.ArrayList;
import java.util.Collection;

public class SongArtist {
    int nmrMusicas = 1;
    String id;
    String nome;




    SongArtist(){

    }
    void incrementNmrMusicas() {
        nmrMusicas++;
    }

    SongArtist(String id, String nome){
        this.id = id;
        this.nome = nome;

    }


    @Override
    public String toString() {

        if (nome.charAt(0)== 'A' || nome.charAt(0)== 'B'|| nome.charAt(0)== 'C' || nome.charAt(0)== 'D' ){
            return  "Artista: [" +nome+ "]";

        }

        return  "Artista: [" +nome+ "]" + " | " + nmrMusicas ;

    }

    public String getId() {
        return id;
    }

    public String getArtistName() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public Object getNome() {
        return nome;
    }


    public String getSongId() {
        return id;
    }
}
