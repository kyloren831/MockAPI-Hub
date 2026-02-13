package ucr.ac.cr.apisandbox.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ucr.ac.cr.apisandbox.dtos.mockresponsesdtos.MockResponseListDto;
import ucr.ac.cr.apisandbox.dtos.mockresponsesdtos.MockResponseRequestDto;
import ucr.ac.cr.apisandbox.dtos.mockresponsesdtos.MockResponseRespDto;
import ucr.ac.cr.apisandbox.services.MockResponseServices;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sandboxes/id/{sandboxId}/mock-endpoints/id/{endpointId}/mock-responses")
@RequiredArgsConstructor
public class MockResponseController {
    private final MockResponseServices mockResponseServices;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MockResponseListDto> findAll(@PathVariable UUID sandboxId,
                                                 @PathVariable UUID endpointId) {
        return mockResponseServices.findAllBySandboxAndEndpoint(sandboxId,endpointId);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MockResponseRespDto getMockResponseById(@PathVariable UUID sandboxId,
                                                   @PathVariable UUID endpointId,
                                                   @PathVariable UUID id) {
        return mockResponseServices.getMockResponseById(sandboxId,endpointId,id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MockResponseRespDto addMockResponse(@PathVariable UUID sandboxId,
                                               @PathVariable UUID endpointId,
                                               @RequestBody MockResponseRequestDto mockResponseRequestDto) {
        return mockResponseServices.addMockResponse(sandboxId,endpointId,mockResponseRequestDto);
    }

    @PutMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MockResponseRespDto updateMockResponse(@PathVariable UUID sandboxId,
                                                  @PathVariable UUID endpointId,
                                                  @PathVariable UUID id,
                                                  @RequestBody MockResponseRequestDto mockResponseRequestDto){
        return mockResponseServices.updateMockResponse(sandboxId,endpointId,id,mockResponseRequestDto);
    }
    @PatchMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stateSwap(@PathVariable UUID sandboxId,
                          @PathVariable UUID endpointId,
                          @PathVariable UUID id){
        mockResponseServices.stateSwap(sandboxId,endpointId,id);
    }
    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMockResponse(@PathVariable UUID sandboxId,
                                   @PathVariable UUID endpointId,
                                   @PathVariable UUID id){
        mockResponseServices.deleteMockResponse(sandboxId,endpointId,id);
    }

}
