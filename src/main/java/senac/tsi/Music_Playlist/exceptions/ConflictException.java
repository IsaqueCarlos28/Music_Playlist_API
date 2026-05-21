package senac.tsi.Music_Playlist.exceptions;

public class ConflictException extends RuntimeException {
    private final String resource;
    private final String field;
    private final Object value;

    public ConflictException(String message,String resource,String field,Object value) {
        super(message);
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    public String getResource() { return resource; }
    public String getField() { return field; }
    public Object getValue() { return value; }
}
