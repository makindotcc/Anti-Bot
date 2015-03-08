package pl.shg.sqlbook;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL table management class
 * @author TheMolkaPL
 */
public class Table {
    private final Connection connection;
    private final String name;
    private final List<Column> columns = new ArrayList<>();
    
    /**
     * Create a new table with a specifited <code>name</code>
     * This method supports SQL injection protection for the name argument
     * See {@link #escapeString(java.lang.String)} for more information
     * @param connection to be send new table to, 
     * can not be <code>null</code>
     * @param name of the table, 
     * can not be <code>null</code>
     * @throws IllegalArgumentException when <code>connection</code>
     * or <code>name</code> is <code>null</code>
     */
    public Table(Connection connection, String name) {
        if (connection == null) {
            throw new IllegalArgumentException("connection can not be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name can not be null");
        }
        this.connection = connection;
        this.name = name;
    }
    
    /**
     * Add column to this table
     * @param column to be added, 
     * can not be <code>null</code>
     * @return instance of this class
     * @throws IllegalArgumentException if the
     * <code>column</code> is <code>null</code>
     * @see #getColumns() get a list of colums
     */
    public Table addColumn(Column column) {
        this.columns.add(column);
        return this;
    }
    
    /**
     * Escape given {@link java.lang.String} in the query argument from the SQL injection
     * WARNING: This method don't protect all of the SQL injection ways.
     * @param query string to be "cleared" from the SQL injection, 
     * can be <code>null</code>.
     * @return new "cleared" SQL string from the SQL injection, 
     * <code>null</code> when <code>null</code> given.
     */
    public String escapeString(String query) {
        if (query == null) {
            return null;
        }
        
        StringBuilder builder = new StringBuilder();
        boolean ignoreNext = false;
        for (int i = 0; i < query.toCharArray().length; i++) {
            boolean remove = false;
            Character c = query.charAt(i);
            if (!ignoreNext) {
                if (c.equals('\'') || c.equals('`')) { // Place \ before ' and `.
                    builder.append('\\');
                } else if (c.equals('\\')) { // Ignore \
                    continue;
                }
            }
            ignoreNext = false;
            
            if (!remove) {
                builder.append(c.toString());
            }
        }
        
        return builder.toString().trim();
    }
    
    /**
     * Execute SQL query command to the database
     * WARNING: Your query is not protect from the SQL injection.
     * Use {@link #escapeString(java.lang.String)} to every data.
     * @param query command to be send to database
     * @param future after-command methods implementation.
     * If <code>null</code> and command was not executed
     * correctly stack trace will be printed on the console
     */
    public void execute(String query, SQLFuture future) {
        if (future == null) {
            future = new SQLFuture() {
                @Override
                public void success(ResultSet result) {}
                
                @Override
                public void exception(Throwable exception) {
                    exception.printStackTrace();
                }
            };
        }
        Thread thread = new Thread(new SQLThread(this.connection, query, future));
        thread.start();
    }
    
    /**
     * Execute SQL query command to the database
     * This method supports SQL injection protection for every data given in params
     * See {@link #escapeString(java.lang.String)} for more information
     * @param query command to be send to database
     * @param future after-command methods implementation.
     * If <code>null</code> and command was not executed
     * @param params object to be replaced in the <code>{0}</code> char, 
     * can be <code>null</code>
     */
    public void execute(String query, SQLFuture future, Object[] params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                String regex = "{" + i + "}";
                if (query.contains(regex)) {
                    String obj = this.escapeString(params[i].toString());
                    query = query.replace(regex, "'" + obj + "'");
                }
            }
        }
        this.execute(query, future);
    }
    
    /**
     * Insert a new value to this table, must be all object specifited in the list
     * This method supports SQL injection protection for every data given in params
     * See {@link #escapeString(java.lang.String)} for more information
     * @param values objects list (columns)
     */
    public void insert(Object[] values) {
        StringBuilder builder = new StringBuilder();
        for (int i = values.length; i > 0; i--) {
            builder.append(this.escapeString(values[i].toString()));
            if (i != 0) {
                builder.append(", ");
            }
        }
        this.execute("INSERT INTO '" + this.getName() + "' VALUES(NULL, " + builder.toString() + ");", null);
    }
    
    /**
     * Get the name of this table
     * @return name of this table, 
     * never <code>null</code>
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Get a list of colums in this table
     * @return colums as a list
     * @throws IndexOutOfBoundsException if no columns was specifited
     * @see #addColumn(net.mg.sqlbook.Column) add a new column
     */
    public List<Column> getColumns() throws NullPointerException {
        if (this.columns.isEmpty()) {
            throw new IndexOutOfBoundsException("colums can not be empty");
        }
        return this.columns;
    }
}
