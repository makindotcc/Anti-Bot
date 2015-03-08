package pl.shg.sqlbook.sql;

import pl.shg.sqlbook.Format;
import pl.shg.sqlbook.Column;

/**
 * An <code>INTEGER</code> SQL object
 * @author TheMolkaPL
 */
public class SQLInteger extends Column {
    public static final String TYPE = "INTEGER";
    public static final Format FORMAT = new NumericFormat();
    
    /**
     * Create an new <code>INTEGER</code> SQL object column
     * @param name (as unique column-ID in this table) of the column.
     */
    public SQLInteger(String name) {
        super(name, TYPE, FORMAT, Column.NO_CHARS);
    }
}
