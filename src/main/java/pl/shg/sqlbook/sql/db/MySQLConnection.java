package pl.shg.sqlbook.sql.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.shg.sqlbook.Connection;
import pl.shg.sqlbook.Table;
import pl.shg.sqlbook.User;
import pl.shg.sqlbook.Column;

/**
 * MySQL server connection
 * @author TheMolkaPL
 */
public class MySQLConnection extends Connection {
    public static final int DEAFAULT_PORT = 3306;
    private final String address;
    private final int port;
    private final String database;
    private final User user;
    
    public MySQLConnection(String address, int port, String database, User user) {
        super("com.mysql.jdbc.Driver");
        this.address = address;
        this.port = port;
        this.database = database;
        this.user = user;
    }
    
    @Override
    public synchronized void createConnection() throws SQLException {
        try {
            Class.forName(this.getDriver()).newInstance();
            String url = "jdbc:mysql://" + this.getAddress() + ":" + this.getPort() + "/" + this.getDatabase();
            String login = this.getUser().getLogin();
            String password = this.getUser().getPassword();
            this.setConnection(DriverManager.getConnection(url, login, password));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public String getAddress() {
        return this.address;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public String getDatabase() {
        return this.database;
    }
    
    public User getUser() {
        return this.user;
    }
}
