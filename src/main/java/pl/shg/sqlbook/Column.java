package pl.shg.sqlbook;

/**
 * Column in the #Table
 * @author TheMolkaPL
 */
public class Column {
    public static final int NO_CHARS = -1;
    private final String name;
    private final String type;
    private Format format;
    private int chars = NO_CHARS;
    
    protected Column(String name, String type, Format format) {
        this.name = name;
        this.type = type;
        this.format = format;
    }
    
    protected Column(String name, String type, Format format, int chars) {
        this.name = name;
        this.type = type;
        this.format = format;
        this.chars = chars;
    }
    
    public int getChars() {
        return this.chars;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public Format getFormat() {
        return this.format;
    }
    
    public boolean hasChars() {
        return this.chars != NO_CHARS;
    }
    
    public void setChars(int chars) {
        this.chars = chars;
    }
    
    public void setFormat(Format format) {
        this.format = format;
    }
    
    @Override
    public String toString() {
        return "Column{name=" + this.getName()
                + "type=" + this.getType()
                + "format=" + this.getFormat()
                + "chars=" + this.getChars() + "}";
    }
}
