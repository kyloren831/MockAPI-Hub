package ucr.ac.cr.apisandbox.dtos.mockendpointdtos;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class MockEndpointListDto {
    private UUID id;

    private String method;   // GET | POST | PUT | PATCH | DELETE

    private String path;

    private boolean enabled;
}
