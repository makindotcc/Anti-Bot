package pl.shg.sqlbook.sql;

import pl.shg.sqlbook.Format;

/**
 * Numeric variables object format
 * @author TheMolkaPL
 */
public class NumericFormat implements Format {
    @Override
    public String getFormat() {
        return "{0}";
    }
}
