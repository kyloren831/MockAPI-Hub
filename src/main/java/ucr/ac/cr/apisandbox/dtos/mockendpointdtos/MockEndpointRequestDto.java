package ucr.ac.cr.apisandbox.dtos.mockendpointdtos;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MockEndpointRequestDto {
    public UUID sandboxId;
    private String method;
    private String path;
    private boolean enabled;
    private String description;
}
