package ucr.ac.cr.apisandbox.dtos.mockresponsesdtos;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MockResponseListDto {
    private UUID id;
    private int statusCode;
    private JsonNode bodyJson;
    private String contentType;
    private boolean enabled;
    private int delayMs;
}
