package pt.ulusofona.aed.rockindeisi2023;

public class SongDetails {
    String id;
    int duracao;
    int letraExplicita;
    int popularidade;
    double dancabilidade, vivacidade, volumeM;

    SongDetails(String id, int duracao, int letraExplicita, int popularidade, double dancabilidade,
                double vivacidade, double volumeM) {
        this.id = id;
        this.duracao = duracao;
        this.letraExplicita = letraExplicita;
        this.popularidade = popularidade;
        this.dancabilidade = dancabilidade;
        this.vivacidade = vivacidade;
        this.volumeM = volumeM;
    }

    SongDetails() {

    }


    @Override
    public String toString() {
        return "<" + id + ">" + "|" + "<" + duracao + ">" + "|" + "<" + letraExplicita + ">" + "|" + "<" + popularidade + ">" +
                "|" + "<" + dancabilidade + ">" + "|" + "<" + vivacidade + ">" + "|" + "<" + volumeM + ">";
    }

    public void duracao(int i) {
    }

    public void setDuracao(int i) {
    }

    public void setPopularidade(int i) {
    }


    public String getId() {
        return this.id = id;
    }
}


