package senac.tsi.Music_Playlist.exceptions;

public class BusinessException extends ApplicationException {
    private final String resource;
    private final String field;
    private final Object value;

    public BusinessException(String message,String resource,String field,Object value) {

        super(message);

        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    public String getResource() { return resource; }
    public String getField() { return field; }
    public Object getValue() { return value; }
}
