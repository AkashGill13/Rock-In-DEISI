package pt.ulusofona.aed.rockindeisi2023;

public class QueryResult {
     String result;
     long time;

     QueryResult(String result, long time) {
        this.result = result;
        this.time = time;
    }

    public QueryResult(Query query, String[] strings) {
    }

    @Override
    public String toString() {
        return  result + "\n" + "time=" + time ;
    }
    String getResult() {
        return result;
    }


     long getTime() {
        return time;
    }
}

