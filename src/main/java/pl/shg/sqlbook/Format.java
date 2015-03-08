package pl.shg.sqlbook;

/**
 * SQL variables format
 * @author TheMolkaPL
 */
public interface Format {
    /**
     * Get full format of the variable.
     * Character <code>{0}</code> will be replaced to value of the variable
     * @return format of the variable, 
     * never <code>null</code>
     */
    String getFormat();
}
