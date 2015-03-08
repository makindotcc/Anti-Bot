package pl.shg.sqlbook;

import java.sql.SQLException;

/**
 * Class that represent SQL database connection
 * @author TheMolkaPL
 */
public abstract class Connection {
    private final String driver;
    private java.sql.Connection connection;
    
    /**
     * Create a new connection
     * @param driver SQL driver, can not be <code>null</code>
     * @throws IllegalArgumentException if the <code>driver</code> is <code>null</code>
     * @see #createConnection() try to create a connection
     */
    public Connection(String driver) {
        if (driver != null) {
            this.driver = driver;
        } else {
            throw new IllegalArgumentException("driver can not be null");
        }
    }
    
    /**
     * Create a test connection to the specifited SQL database
     * @throws SQLException when can not connect to the database
     * @see #setConnection(java.sql.Connection) Set Java SQL API connection
     * @see #createTable(net.mg.sqlbook.Table) create a new table in the database
     */
    public abstract void createConnection() throws SQLException;
    
    /**
     * Create a new table in your database
     * @param table Table object to create
     * @return object from the argument, 
     * <code>null</code> if the <code>null</code> is specifited
     * @throws SQLException when can not create the table
     */
    public abstract Table createTable(Table table) throws SQLException;
    
    /**
     * Get a driver to the database
     * @return database's driver, never <code>null</code>
     */
    public String getDriver() {
        return this.driver;
    }
    
    /**
     * Get Java SQL API connection to the database
     * @return Java SQL API connection,
     * <code>null</code> if the connection is <code>null</code>
     * @see #setConnection(java.sql.Connection) set Java SQL API connection
     */
    public java.sql.Connection getConnection() {
        return this.connection;
    }
    
    /**
     * Set Java SQL API object connection
     * @param connection Java SQL API object connection
     * @see #getConnection() get Java SQL API connection
     */
    public void setConnection(java.sql.Connection connection) {
        this.connection = connection;
    }
}
