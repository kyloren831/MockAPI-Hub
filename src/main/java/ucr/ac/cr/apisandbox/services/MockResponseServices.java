package ucr.ac.cr.apisandbox.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucr.ac.cr.apisandbox.dtos.mockresponsesdtos.MockResponseListDto;
import ucr.ac.cr.apisandbox.dtos.mockresponsesdtos.MockResponseRequestDto;
import ucr.ac.cr.apisandbox.dtos.mockresponsesdtos.MockResponseRespDto;
import ucr.ac.cr.apisandbox.exceptions.ConflictException;
import ucr.ac.cr.apisandbox.exceptions.ResourceNotFoundException;
import ucr.ac.cr.apisandbox.models.MockEndpoint;
import ucr.ac.cr.apisandbox.models.MockResponse;
import ucr.ac.cr.apisandbox.repositories.IMockResponseRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MockResponseServices {
    private final IMockResponseRepository repository;
    private final MockEndpointServices mockEndpointServices;


    public List<MockResponseListDto> findAllBySandboxAndEndpoint(UUID sandboxId,
                                                      UUID endpointId) {
        //Trae el mockendpoint para hacer la verificacion sandbox y endpoint
        validateSandboxOwnerShip(sandboxId, endpointId);
        return repository.findByEndpoint_Id(endpointId)
                .stream()
                .map(m-> MockResponseListDto.builder()
                        .id(m.getId())
                        .statusCode(m.getStatusCode())
                        .contentType(m.getContentType())
                        .bodyJson(m.getBodyJson())
                        .delayMs(m.getDelayMs())
                        .enabled(m.isEnabled())
                        .build())
                .toList();
    }

    public MockResponseRespDto getMockResponseById(UUID sandboxId,
                                                   UUID endpointId,
                                                   UUID mockResponseId) {
        validateSandboxOwnerShip(sandboxId, endpointId);
        MockResponse entity = getMockResponse(mockResponseId);
        return MockResponseRespDto.builder()
                .id(entity.getId())
                .endpointId(endpointId)
                .body(entity.getBodyJson())
                .contentType(entity.getContentType())
                .delayMs(entity.getDelayMs())
                .enabled(entity.isEnabled())
                .statusCode(entity.getStatusCode())
                .build();
    }

    @Transactional
    public MockResponseRespDto addMockResponse(UUID sandboxId,
                                               UUID endpointId,
                                               MockResponseRequestDto mockResponseRequestDto) {
        validateSandboxOwnerShip(sandboxId, endpointId);
        MockEndpoint mockEndpoint = mockEndpointServices.getMockEndpoint(endpointId);
        MockResponse created = repository.save(MockResponse.builder()
                .endpoint(mockEndpoint)
                .statusCode(mockResponseRequestDto.getStatusCode())
                .contentType(mockResponseRequestDto.getContentType())
                .bodyJson(mockResponseRequestDto.getBody())
                .delayMs(mockResponseRequestDto.getDelayMs())
                .build());
        return toResponseDto(created);
    }

    @Transactional
    public MockResponseRespDto updateMockResponse(UUID sandboxId,
                                                          UUID endpointId,
                                                          UUID mockResId,
                                                          MockResponseRequestDto mockResponseRequestDto) {
        //Validations
        validateSandboxOwnerShip(sandboxId, endpointId);
        validateEndpointOwnerShip(endpointId, mockResId);


        MockResponse entity = getMockResponse(mockResId);
        MockEndpoint mockEndpoint = mockEndpointServices.getMockEndpoint(endpointId);

        entity.setBodyJson(mockResponseRequestDto.getBody());
        entity.setDelayMs(mockResponseRequestDto.getDelayMs());
        entity.setEndpoint(mockEndpoint);
        entity.setStatusCode(mockResponseRequestDto.getStatusCode());
        entity.setContentType(mockResponseRequestDto.getContentType());

        return toResponseDto(repository.save(entity));
    }

    @Transactional
    public void deleteMockResponse(UUID sandboxId,
                                                          UUID endpointId,
                                                          UUID mockResId){
        validateSandboxOwnerShip(sandboxId, endpointId);
        validateEndpointOwnerShip(endpointId, mockResId);
        repository.deleteById(mockResId);
    }

    @Transactional
    public void stateSwap(UUID sandboxId,
                          UUID endpointId,
                          UUID mockResId){
        validateSandboxOwnerShip(sandboxId, endpointId);
        validateEndpointOwnerShip(endpointId, mockResId);

        MockResponse enabled = getActiveMockResponse(endpointId);
        if(enabled != null){
            enabled.setEnabled(false);
            repository.save(enabled);
        }
        MockResponse newEnabled = getMockResponse(mockResId);
        newEnabled.setEnabled(true);
        repository.save(newEnabled);
    }

    public MockResponse getActiveMockResponse(UUID endpointId){
        var responses = repository.findByEndpoint_Id(endpointId);
        if(responses.isEmpty()){
            return null;
        }
        return repository.findByEnabledAndEndpoint_Id(true,endpointId)
                .orElse(null);
    }



    /**
     * Utilities
     */
    private MockResponse getMockResponse(UUID mockResponseId) {
        return repository.findById(mockResponseId)
                .orElseThrow(()->new ResourceNotFoundException(
                        "MockResponse with id: " + mockResponseId +" not found"
                ));
    }

    private void validateEndpointOwnerShip(UUID endpointId,
                                           UUID mockResId) {
        MockEndpoint mockEndpoint = mockEndpointServices.getMockEndpoint(endpointId);
        if (mockEndpoint.getId().equals(mockResId)) {
            throw new ConflictException("Endpoint id " + endpointId + " does not belong to this response");
        }
    }

    private void validateSandboxOwnerShip(UUID sandboxId,
                                          UUID endpointId) {
        MockEndpoint mockEndpoint = mockEndpointServices.getMockEndpoint(endpointId);
        mockEndpointServices.validateSandboxOwnerShip(sandboxId,mockEndpoint);
    }

    private MockResponseRespDto toResponseDto(MockResponse mockResponse) {
        return MockResponseRespDto.builder()
                .id(mockResponse.getId())
                .statusCode(mockResponse.getStatusCode())
                .contentType(mockResponse.getContentType())
                .body(mockResponse.getBodyJson())
                .delayMs(mockResponse.getDelayMs())
                .endpointId(mockResponse.getEndpoint().getId())
                .updatedAt(mockResponse.getUpdatedAt())
                .createdAt(mockResponse.getCreatedAt())
                .build();
    }
}
