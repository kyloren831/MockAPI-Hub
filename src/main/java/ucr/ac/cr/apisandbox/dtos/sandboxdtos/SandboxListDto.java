package ucr.ac.cr.apisandbox.dtos.sandboxdtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
public class SandboxListDto {
    private UUID id;
    private String name;
    private String slug;
}