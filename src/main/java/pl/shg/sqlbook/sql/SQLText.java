package pl.shg.sqlbook.sql;

import pl.shg.sqlbook.Column;
import pl.shg.sqlbook.Format;

/**
 * A <code>TEXT</code> SQL object
 * @author TheMolkaPL
 */
public class SQLText extends Column {
    public static final String TYPE = "TEXT";
    public static final Format FORMAT = new TextualFormat();
    
    /**
     * Create an new <code>TEXT</code> SQL object column
     * @param name (as unique column-ID in this table) of the column.
     */
    public SQLText(String name) {
        super(name, TYPE, FORMAT);
    }
}
