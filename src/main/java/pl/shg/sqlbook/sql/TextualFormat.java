package pl.shg.sqlbook.sql;

import pl.shg.sqlbook.Format;

/**
 * Textual variables object format
 * @author TheMolkaPL
 */
public class TextualFormat implements Format {
    @Override
    public String getFormat() {
        return "'{0}'";
    }
}
