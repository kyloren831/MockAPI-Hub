package ucr.ac.cr.apisandbox.dtos.requestlogsdtos;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class RequestLogResponseDto {
    private UUID id;
    private UUID sandboxId;
    private UUID endpointId;
    private Instant timestamp;
    private String method;
    private String path;
    private String queryString;
    private String requestHeaders;
    private String requestBody;
    private int responseStatus;
    private int responseTimeMs;
}
