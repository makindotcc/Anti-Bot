package pl.shg.sqlbook;

import java.sql.PreparedStatement;

/**
 * Thread that executes all of the commands to or from SQL server
 * @author TheMolkaPL
 */
public class SQLThread implements Runnable {
    private final Connection connection;
    private final String command;
    private final SQLFuture future;
    
    public SQLThread(Connection connection, String command, SQLFuture future) {
        this.connection = connection;
        this.command = command.trim();
        this.future = future;
    }
    
    @Override
    public synchronized void run() {
        try {
            PreparedStatement statement = this.getConnection().getConnection().prepareStatement(this.getCommand());
            if (this.getCommand().toUpperCase().startsWith("SELECT")) {
                this.getFuture().success(statement.executeQuery());
            } else {
                statement.execute();
                this.getFuture().success(null);
            }
        } catch (Throwable ex) {
            System.err.println("Error in: " + this.getCommand());
            this.getFuture().exception(ex);
        }
    }
    
    private Connection getConnection() {
        return this.connection;
    }
    
    private String getCommand() {
        return this.command;
    }
    
    private SQLFuture getFuture() {
        return this.future;
    }
}
