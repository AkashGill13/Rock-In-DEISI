package pt.ulusofona.aed.rockindeisi2023;

public class ParseDasQueries {

     static String[] queryMostFrequentWord(String arguments) {
        int firstSpaceIndex = arguments.indexOf(" ");
        int secondSpaceIndex = arguments.indexOf(" ", firstSpaceIndex + 1);

        String count = arguments.substring(0, firstSpaceIndex);
        String count1 = arguments.substring(firstSpaceIndex + 1, secondSpaceIndex);
        String name = arguments.substring(secondSpaceIndex + 1).trim();

        return new String[]{count, count1, name};
    }


     static String[] queryTopArtistBetween(String arguments) {
        return arguments.split(" ");
    }

     static String[] quetGetArtistTag(String arguments) {
        return new String[]{arguments};
    }

     static String[] queryGetTag(String arguments) {
        return arguments.split(";");
    }


     static String[] queryGetSongsByArtist(String arguments) {
        int indice = arguments.indexOf(" ");

        String count = arguments.substring(0, indice);
        String name = arguments.substring(indice).trim(); // Adjusted substring indices and added trim()
        return new String[]{count, name};
    }

     static String[] queryGetMostDanceable(String arguments) {
        return arguments.split(" ");
    }


}
