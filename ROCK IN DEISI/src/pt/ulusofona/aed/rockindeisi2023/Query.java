package pt.ulusofona.aed.rockindeisi2023;

import java.util.Arrays;
import java.util.Objects;

public class Query {
    String name;
    String[] args;

    Query(String name, String[] args) {
        this.name = name;
        this.args = args;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Query other = (Query) obj;
        return Objects.equals(name, other.name);
    }



    public String getName() {
            return name;
        }

        public String[] getArgs() {
            return args;
        }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Query Name=").append(name.trim()).append("\n");
        sb.append("Query Arguments=").append(Arrays.toString(args)).append("\n");
        return sb.toString();
    }

}



