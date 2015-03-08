package pl.shg.sqlbook.sql.db;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.shg.sqlbook.Column;
import pl.shg.sqlbook.Connection;
import pl.shg.sqlbook.Table;

/**
 * SQLite (flat file) connection
 * @author TheMolkaPL
 */
public class SQLiteConnection extends Connection {
    private final File file;
    
    public SQLiteConnection(File file) {
        super("org.sqlite.JDBC");
        this.file = file;
    }
    
    @Override
    public synchronized void createConnection() throws SQLException {
        try {
            Class.forName(this.getDriver()).newInstance();
            String url = "jdbc:sqlite:" + this.getFile().getPath();
            this.setConnection(DriverManager.getConnection(url));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public synchronized Table createTable(Table table) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("id INTEGER AUTO_INCREMENT");
        for (Column column : table.getColumns()) {
            builder.append(", ").append(column.getName()).append(" ").append(column.getType());
            if (column.hasChars()) {
                builder.append("(").append(column.getChars()).append(")");
            }
        }
        
        table.execute("CREATE TABLE IF NOT EXISTS {0}({1}, PRIMARY KEY({2}));",
                null, new Object[] {table.getName(), builder.toString(), "id"});
        return table;
    }
    
    public File getFile() {
        return this.file;
    }
}
