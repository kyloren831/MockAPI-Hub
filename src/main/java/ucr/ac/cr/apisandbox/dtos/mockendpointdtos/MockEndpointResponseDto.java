package ucr.ac.cr.apisandbox.dtos.mockendpointdtos;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class MockEndpointResponseDto {
    private UUID id;
    private UUID sandboxId;
    private String method;
    private String path;
    private boolean enabled;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
