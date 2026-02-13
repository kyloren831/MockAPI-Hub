package ucr.ac.cr.apisandbox.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ucr.ac.cr.apisandbox.dtos.sandboxdtos.SandboxListDto;
import ucr.ac.cr.apisandbox.dtos.sandboxdtos.SandboxRequestDto;
import ucr.ac.cr.apisandbox.dtos.sandboxdtos.SandboxResponseDto;
import ucr.ac.cr.apisandbox.services.SandboxServices;

import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sandboxes")
@RequiredArgsConstructor
public class SandboxController {
    private final SandboxServices sandboxServices;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private SandboxResponseDto addSandbox(@RequestBody SandboxRequestDto sandboxRequestDto){
        return sandboxServices.addSandbox(sandboxRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private List<SandboxListDto> findAllSandbox(){
        return sandboxServices.findAll();
    }
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    private SandboxResponseDto findSandboxById(@PathVariable UUID id){
        return sandboxServices.findById(id);
    }
    @GetMapping("/slug/{slug}")
    @ResponseStatus(HttpStatus.OK)
    private SandboxResponseDto findSandboxBySlug(@PathVariable String slug){
        return sandboxServices.findBySlug(slug);
    }
    @PutMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    private SandboxResponseDto updateSandbox(@PathVariable UUID id,@RequestBody SandboxRequestDto sandboxRequestDto){
        return sandboxServices.updateSandbox(id, sandboxRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteSandbox(@PathVariable UUID id){
        sandboxServices.deleteSandbox(id);
    }

}
