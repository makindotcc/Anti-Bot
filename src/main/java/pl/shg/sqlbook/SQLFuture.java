/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.shg.sqlbook;

import java.sql.ResultSet;

/**
 * This interface contains the result methods that are
 * executed when an command was executed successfully, or not.
 * @author TheMolkaPL
 */
public interface SQLFuture {
    /**
     * Called when a command was executed successfully
     * @param result ResultSet object with the result of command, 
     * <code>null</code> if the command returns nothing.
     */
    void success(ResultSet result);
    
    /**
     * Called when a command was not executed corretly
     * @param exception An exception that has been thrown, 
     * never <code>null</code>.
     */
    void exception(Throwable exception);
}
