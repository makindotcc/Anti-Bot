package pl.shg.sqlbook.sql;

import pl.shg.sqlbook.Column;
import pl.shg.sqlbook.Format;

/**
 * A <code>CHAR</code> SQL object
 * @author Aleksander
 */
public class SQLChar extends Column {
    public static final String TYPE = "CHAR";
    public static final Format FORMAT = new TextualFormat();
    
    /**
     * Create a new <code>CHAR</code> SQL object column
     * @param name (as unique column-ID in this table) of the column.
     * @param chars required storage in bytes amount
     */
    public SQLChar(String name, int chars) {
        super(name, TYPE, FORMAT, chars);
    }
}
