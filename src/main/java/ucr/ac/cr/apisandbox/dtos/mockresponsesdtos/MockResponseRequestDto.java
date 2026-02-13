package ucr.ac.cr.apisandbox.dtos.mockresponsesdtos;

import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

@Getter
@Builder
public class MockResponseRequestDto {
    private UUID endpointId;
    private int statusCode;
    private JsonNode body;
    private String contentType;
    private int delayMs;
}
