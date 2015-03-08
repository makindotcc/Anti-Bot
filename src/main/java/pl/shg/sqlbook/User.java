package pl.shg.sqlbook;

/**
 * User class
 * @author TheMolkaPL
 */
public class User {
    private final String login;
    private String password = null;
    
    /**
     * New user with login and password
     * @param login to the database, can not be <code>null</code>
     * @throws IllegalArgumentException when <code>login</code> is <code>null</code>
     */
    public User(String login) {
        if (login == null) {
            throw new IllegalArgumentException("login can not be null");
        }
        this.login = login;
    }
    
    /**
     * New user with login and password
     * @param login to the database, can not be <code>null</code>
     * @param password to the database
     * @throws IllegalArgumentException when <code>login</code> is <code>null</code>
     */
    public User(String login, String password) {
        if (login == null) {
            throw new IllegalArgumentException("login can not be null");
        }
        this.login = login;
        this.password = password;
    }
    
    /**
     * Get a login to the database
     * @return login as a #String object, 
     * never <code>null</code>
     * @see #getPassword() get a password to the database (if exists)
     */
    public String getLogin() {
        return this.login;
    }
    
    /**
     * Get a password to the database (if exists)
     * @return password as a #String object, 
     * <code>null</code> is the password is null
     * @see #getLogin() get a login to the database
     */
    public String getPassword() {
        return this.password;
    }
}
