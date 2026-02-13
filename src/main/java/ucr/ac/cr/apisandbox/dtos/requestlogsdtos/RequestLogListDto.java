package ucr.ac.cr.apisandbox.dtos.requestlogsdtos;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class RequestLogListDto {
    private UUID id;
    private UUID sandboxId;
    private UUID endpointId;
    private String method;
    private String path;
    private Instant timestamp;
    private int responseTimeMs;
}
