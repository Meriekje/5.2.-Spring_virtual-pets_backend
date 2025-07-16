package virtualpets.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
  private String error;
  private String message;
  private int status;
  private LocalDateTime timestamp;
  private String path;
  private Map<String, String> fieldErrors;

  public ErrorResponse(String error, String message, int status, LocalDateTime timestamp, String path) {
    this.error = error;
    this.message = message;
    this.status = status;
    this.timestamp = timestamp;
    this.path = path;
    this.fieldErrors = null;
  }
}
