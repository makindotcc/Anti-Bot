package pl.shg.sqlbook.sql;

import pl.shg.sqlbook.Format;
import pl.shg.sqlbook.Column;

/**
 * A <code>VARCHAR</code> SQL object
 * @author TheMolkaPL
 */
public class SQLVarchar extends Column {
    public static final String TYPE = "VARCHAR";
    public static final Format FORMAT = new TextualFormat();
    
    /**
     * Create a new <code>VARCHAR</code> SQL object column
     * @param name (as unique column-ID in this table) of the column.
     * @param chars required storage in bytes amount
     */
    public SQLVarchar(String name, int chars) {
        super(name, TYPE, FORMAT, chars);
    }
}
