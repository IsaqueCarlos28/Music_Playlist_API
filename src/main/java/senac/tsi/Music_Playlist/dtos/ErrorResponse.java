package senac.tsi.Music_Playlist.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponse{
        LocalDateTime timestamp;
        int status;
        String error;
        String message;
        String resource;
        String field;
        Object value;
        Map<String, String> errors;
}
