package pt.ulusofona.aed.rockindeisi2023;


import java.util.Comparator;
import java.util.Map;


class ComparadorDeDancabilidade implements Comparator<SongDetails> {
    public int compare(SongDetails o1, SongDetails o2) {
        if (o1.dancabilidade > o2.dancabilidade) {
            return -1;
        } else if (o1.dancabilidade < o2.dancabilidade) {
            return +1;
        } else {
            return 0;
        }
    }


}


class OrdenacaoAlfabetica implements Comparator<String> {
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
}


class MapSortCresc implements Comparator<Map.Entry<String, Integer>> {
    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {

        return (o1.getValue()).compareTo(o2.getValue());
    }
}


class MapSortDec implements Comparator<Map.Entry<String, Integer>> {
    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {

        return (o2.getValue()).compareTo(o1.getValue());
    }
}


class MapSortDecOriginal implements Comparator<Map.Entry<String, Double>> {
    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {

        return (o2.getValue()).compareTo(o1.getValue());
    }
}


public class Comparadores {


}