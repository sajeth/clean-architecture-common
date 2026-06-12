package io.github.sajeth.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProblemDetailErrorResponse extends ProblemDetail {

    private String errorCode;
    private Instant timestamp;
    private List<String> validationErrors;
    private Map<String, String> fieldErrors;
    private String debugInfo;
    private List<ExceptionDetail> exceptions;

    public static ProblemDetailErrorResponse of(int status, String title, String detail, String typeSlug) {
        ProblemDetailErrorResponse response = new ProblemDetailErrorResponse();
        response.setStatus(status);
        response.setTitle(title);
        response.setDetail(detail);
        response.setType(URI.create("https://errors.example.com/" + typeSlug));
        response.setTimestamp(Instant.now());
        return response;
    }
}
