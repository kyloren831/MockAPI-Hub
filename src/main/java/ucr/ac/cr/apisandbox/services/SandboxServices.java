package ucr.ac.cr.apisandbox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucr.ac.cr.apisandbox.dtos.sandboxdtos.SandboxListDto;
import ucr.ac.cr.apisandbox.dtos.sandboxdtos.SandboxRequestDto;
import ucr.ac.cr.apisandbox.dtos.sandboxdtos.SandboxResponseDto;
import ucr.ac.cr.apisandbox.exceptions.ConflictException;
import ucr.ac.cr.apisandbox.exceptions.ResourceNotFoundException;
import ucr.ac.cr.apisandbox.models.Sandbox;
import ucr.ac.cr.apisandbox.repositories.ISandboxRepository;

import java.util.*;


@Service
public class SandboxServices{
    @Autowired
    private ISandboxRepository sandboxRepository;

    public List<SandboxListDto> findAll(){
       return sandboxRepository.findAll()
               .stream()
               .map(sandbox -> SandboxListDto.builder()
                       .id(sandbox.getId())
                       .slug(sandbox.getSlug())
                       .name(sandbox.getName())
                       .build())
               .toList();
    }

    public SandboxResponseDto  findById(UUID id){

        // Buscamos la entidad por el id y si no existe retorna 404
        Sandbox entity = getSandbox(id);
        // Retornamos la entidad mapeada al DTO
        return toResponseDto(entity);
    }

    public SandboxResponseDto  findBySlug(String slug){
        // Buscamos la entidad por el slug y si no existe retorna 404
        Sandbox entity = getBySlug(slug);
        // Retornamos la entidad mapeada al DTO
        return toResponseDto(entity);
    }

    @Transactional
    public SandboxResponseDto addSandbox(SandboxRequestDto sandboxRequestDto){
        if(sandboxRepository.existsBySlug(sandboxRequestDto.getSlug())){
            throw new ConflictException("You are using the slug ("+sandboxRequestDto.getSlug()+") in other sandbox");
        }
        Sandbox sandbox = Sandbox.builder()
                .slug(sandboxRequestDto.getSlug().toLowerCase().replace("-"," "))
                .name(sandboxRequestDto.getName())
                .build();
        var savedSandbox = sandboxRepository.save(sandbox);
        return toResponseDto(savedSandbox);
    }
    @Transactional
    public SandboxResponseDto updateSandbox(UUID id, SandboxRequestDto sandboxRequestDto){
        Sandbox entity = getSandbox(id);
        entity.setName(sandboxRequestDto.getName());
        entity.setSlug(sandboxRequestDto.getSlug());
        sandboxRepository.save(entity);
        return toResponseDto(entity);
    }
    @Transactional
    public void deleteSandbox(UUID id){
        Sandbox entity = getSandbox(id);
        sandboxRepository.deleteById(entity.getId());
    }

    // Traer Sandbox por id
    public Sandbox getSandbox(UUID id){
        return  sandboxRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Sandbox with id " + id + " not found"
                ));
    }

    // Convierte entidad -> DTO de salida.
    private SandboxResponseDto toResponseDto(Sandbox sandbox) {
        return SandboxResponseDto.builder()
                .id(sandbox.getId())
                .name(sandbox.getName())
                .slug(sandbox.getSlug())
                .createdAt(sandbox.getCreatedAt())
                .updatedAt(sandbox.getUpdatedAt())
                .build();
    }

    public Sandbox getBySlug(String slug){
        return sandboxRepository.findBySlug(slug)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Sandbox with slug " + slug + " not found"
                ));
    }
}
