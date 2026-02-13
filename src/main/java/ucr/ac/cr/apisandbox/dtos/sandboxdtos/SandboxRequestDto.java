package ucr.ac.cr.apisandbox.dtos.sandboxdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
public class SandboxRequestDto {
    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Size(max = 120)
    private String slug;
}
