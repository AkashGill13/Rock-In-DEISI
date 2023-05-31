package pt.ulusofona.aed.rockindeisi2023;

public class EstaticFicheiro {
    String nomeArquivo;
    int linhasOK;
    int linhasNOK;
    int primeiraLinhaNOK;
    EstaticFicheiro(){

    }

    public EstaticFicheiro(String nomeArquivo, int linhasOK, int linhasNOK, int primeiraLinhaNOK) {
        this.nomeArquivo = nomeArquivo;
        this.linhasOK = linhasOK;
        this.linhasNOK = linhasNOK;
        this.primeiraLinhaNOK = primeiraLinhaNOK;
    }

    @Override
    public String toString() {

       return nomeArquivo + " | " + linhasOK + " | " + linhasNOK + " | " + primeiraLinhaNOK;

    }

}