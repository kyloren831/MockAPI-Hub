package ucr.ac.cr.apisandbox.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ucr.ac.cr.apisandbox.dtos.mockendpointdtos.MockEndpointListDto;
import ucr.ac.cr.apisandbox.dtos.mockendpointdtos.MockEndpointRequestDto;
import ucr.ac.cr.apisandbox.dtos.mockendpointdtos.MockEndpointResponseDto;
import ucr.ac.cr.apisandbox.exceptions.ConflictException;
import ucr.ac.cr.apisandbox.exceptions.ResourceNotFoundException;
import ucr.ac.cr.apisandbox.models.MockEndpoint;
import ucr.ac.cr.apisandbox.models.Sandbox;
import ucr.ac.cr.apisandbox.repositories.IMockEndpointRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MockEndpointServices {
    private final IMockEndpointRepository repository;
    private final SandboxServices sandboxServices;

    /**
     * Lista todos los mock endpoints mapeados al dto
     * */
    public List<MockEndpointListDto> findAll(UUID sandboxId) {
        return repository.findAllBySandbox_Id(sandboxId)
                .stream()
                .map(m->MockEndpointListDto.builder()
                        .id(m.getId())
                        .method(m.getMethod())
                        .path(m.getPath())
                        .enabled(m.isEnabled())
                        .build()
                ).toList();
    }

    public MockEndpointResponseDto findById(UUID sandboxId, UUID id) {
        MockEndpoint entity = getMockEndpoint(id);
        validateSandboxOwnerShip(sandboxId,entity);
        return toResponseDto(entity);
    }

    public MockEndpointResponseDto findByMethodAndPath(UUID sandboxId,String method, String path) {
        var entity =  resolveEndpoint(sandboxId,method,path);
        return toResponseDto(entity);
    }

    public MockEndpoint resolveEndpoint(UUID sandboxId,
                                        String method,
                                        String path) {
        var entity =  repository.findByPathAndMethod(path,method)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "MockEndpoint with method " + method +" and path " + path + " not found"
                ));
        validateSandboxOwnerShip(sandboxId,entity);
        return entity;
    }


    @Transactional
    public MockEndpointResponseDto addMockEndpoint(UUID sandboxId, MockEndpointRequestDto mockEndpointRequestDto) {


        if (!sandboxId.equals(mockEndpointRequestDto.getSandboxId())) {
            throw new ConflictException("Sandbox id " + sandboxId + " does not belong to this endpoint");
        }

        Sandbox sandbox = sandboxServices.getSandbox(sandboxId);
        MockEndpoint entity = repository.save(MockEndpoint.builder()
                    .sandbox(sandbox)
                    .method(mockEndpointRequestDto.getMethod())
                    .path(mockEndpointRequestDto.getPath())
                    .enabled(mockEndpointRequestDto.isEnabled())
                    .description(mockEndpointRequestDto.getDescription())
                .build());
        return toResponseDto(entity);
    }

    @Transactional
    public MockEndpointResponseDto updateMockEndpoint(UUID sandboxId,UUID id, MockEndpointRequestDto mockEndpointRequestDto) {
        MockEndpoint entity = getMockEndpoint(id);
        validateSandboxOwnerShip(sandboxId,entity);
        entity.setMethod(mockEndpointRequestDto.getMethod());
        entity.setPath(mockEndpointRequestDto.getPath());
        entity.setEnabled(mockEndpointRequestDto.isEnabled());
        entity.setDescription(mockEndpointRequestDto.getDescription());
        entity.setSandbox(sandboxServices.getSandbox(sandboxId));
        entity.setId(id);
        repository.save(entity);
        return toResponseDto(entity);
    }
    @Transactional
    public void deleteMockEndpoint(UUID sandboxId, UUID id) {
        var entity = getMockEndpoint(id);
        validateSandboxOwnerShip(sandboxId,entity);
        repository.deleteById(entity.getId());
    }
    @Transactional
    public void stateSwap(UUID sandboxId, UUID id){
        var entity = getMockEndpoint(id);
        validateSandboxOwnerShip(sandboxId,entity);
        entity.setEnabled(!entity.isEnabled());
        repository.save(entity);
    }

    //Traer mockendpoint por id
    public MockEndpoint getMockEndpoint(UUID id) {
        return repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "MockEndpoint with id " + id +" not found"
                ));
    }

    // Convierte entidad -> DTO de salida.
    private MockEndpointResponseDto toResponseDto(MockEndpoint mockEndpoint) {
        return MockEndpointResponseDto.builder()
                .id(mockEndpoint.getId())
                .sandboxId(mockEndpoint.getSandbox().getId())
                .method(mockEndpoint.getMethod())
                .path(mockEndpoint.getPath())
                .enabled(mockEndpoint.isEnabled())
                .description(mockEndpoint.getDescription())
                .createdAt(mockEndpoint.getCreatedAt())
                .updatedAt(mockEndpoint.getUpdatedAt())
                .build();
    }

    public void validateSandboxOwnerShip(UUID sandboxId, MockEndpoint mockEndpoint) {
        Sandbox sandbox = sandboxServices.getSandbox(sandboxId);
        if (!sandbox.getId().equals(mockEndpoint.getSandbox().getId())) {
            throw new ConflictException("Sandbox id " + sandboxId + " does not belong to this endpoint");
        }
    }
}
