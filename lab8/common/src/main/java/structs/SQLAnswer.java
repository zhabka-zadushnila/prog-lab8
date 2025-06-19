package structs;

import java.io.Serializable;
import java.sql.ResultSet;

public class SQLAnswer implements Serializable {
    private static final long serialVersionUID = 1L;
    boolean expectedBehabiour;
    String description;
    ResultSet output;
    boolean hasResults;

    public SQLAnswer(ResultSet output, String description, boolean expectedBehabiour) {
        this.output = output;
        this.description = description;
        this.expectedBehabiour = expectedBehabiour;
        if (output == null) {
            this.hasResults = false;
        } else {
            this.hasResults = true;
        }
    }

    public SQLAnswer(String description, boolean expectedBehabiour) {
        this(null, description, expectedBehabiour);
    }

    public String getDescription() {
        return description;
    }

    public ResultSet getOutput() {
        return output;
    }


    public boolean isExpectedBehabiour() {
        return expectedBehabiour;
    }

    public boolean HasResults() {
        return hasResults;
    }

}
