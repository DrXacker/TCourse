package com.example.TCourse.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse extends Throwable {
    private static final String DATE_TIME_PATTERN = "dd-MM-yyyy hh:mm:ss";
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }
}
