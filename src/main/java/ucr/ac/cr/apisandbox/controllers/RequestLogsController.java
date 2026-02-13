package ucr.ac.cr.apisandbox.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ucr.ac.cr.apisandbox.dtos.requestlogsdtos.RequestLogListDto;
import ucr.ac.cr.apisandbox.dtos.requestlogsdtos.RequestLogResponseDto;
import ucr.ac.cr.apisandbox.services.RequestLogServices;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sandboxes/{sandboxId}")
@RequiredArgsConstructor
public class RequestLogsController {
    private final RequestLogServices requestLogServices;

    @GetMapping("/logs")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestLogListDto> findAllBySandbox(@PathVariable UUID sandboxId,
                                                    @RequestParam Integer limit) {
        return requestLogServices.findAllBySandbox(sandboxId, limit);
    }
    @GetMapping("/endpoint/{id}/logs")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestLogListDto>findBySandboxAndEndpoint(@PathVariable UUID sandboxId,
                                                           @PathVariable UUID id,
                                                           @RequestParam Integer limit) {
        return requestLogServices.findBySandboxAndEndpoint(sandboxId,id, limit);
    }
    @GetMapping("/logs/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RequestLogResponseDto findById(@PathVariable UUID sandboxId,
                                          @PathVariable UUID id){
        return requestLogServices.findById(sandboxId, id);
    }
}
