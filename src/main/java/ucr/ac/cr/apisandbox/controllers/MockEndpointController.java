package ucr.ac.cr.apisandbox.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ucr.ac.cr.apisandbox.dtos.mockendpointdtos.MockEndpointListDto;
import ucr.ac.cr.apisandbox.dtos.mockendpointdtos.MockEndpointRequestDto;
import ucr.ac.cr.apisandbox.dtos.mockendpointdtos.MockEndpointResponseDto;
import ucr.ac.cr.apisandbox.models.MockEndpoint;
import ucr.ac.cr.apisandbox.services.MockEndpointServices;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sandboxes/id/{sandboxId}/mock-endpoints")
@RequiredArgsConstructor
public class MockEndpointController {
    private final MockEndpointServices mockEndpointServices;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MockEndpointListDto> getMockEndpoints(@PathVariable UUID sandboxId){
        return mockEndpointServices.findAll(sandboxId);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MockEndpointResponseDto getMockEndpointById(@PathVariable UUID sandboxId,@PathVariable UUID id){
        return mockEndpointServices.findById(sandboxId,id);
    }
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public MockEndpointResponseDto getMockEndpointById(@PathVariable UUID sandboxId,
                                                       @RequestParam String method,
                                                       @RequestParam String path){
        return mockEndpointServices.findByMethodAndPath(sandboxId,method,path);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MockEndpointResponseDto createMockEndpoint(@PathVariable UUID sandboxId,
                                                      @RequestBody MockEndpointRequestDto mockEndpointRequestDto){
        return mockEndpointServices.addMockEndpoint(sandboxId,mockEndpointRequestDto);
    }

    @PutMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MockEndpointResponseDto updateMockEndpoint(@PathVariable UUID sandboxId,
                                                      @PathVariable UUID id,
                                                      @RequestBody MockEndpointRequestDto mockEndpointRequestDto){
        return mockEndpointServices.updateMockEndpoint(sandboxId,id,mockEndpointRequestDto);
    }

    @PatchMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stateSwap(@PathVariable UUID sandboxId,
                          @PathVariable UUID id){
        mockEndpointServices.stateSwap(sandboxId,id);
    }

    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMockEndpoint(@PathVariable UUID sandboxId,
                                   @PathVariable UUID id){
        mockEndpointServices.deleteMockEndpoint(sandboxId,id);
    }


}
