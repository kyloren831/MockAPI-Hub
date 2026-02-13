package ucr.ac.cr.apisandbox.dtos.mockresponsesdtos;

import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class MockResponseRespDto {
    private UUID id;
    private UUID endpointId;
    private JsonNode body;
    private String contentType;
    private int delayMs;
    private int statusCode;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
}
