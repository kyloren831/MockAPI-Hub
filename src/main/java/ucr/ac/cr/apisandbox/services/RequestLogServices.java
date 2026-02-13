package ucr.ac.cr.apisandbox.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucr.ac.cr.apisandbox.dtos.requestlogsdtos.RequestLogListDto;
import ucr.ac.cr.apisandbox.dtos.requestlogsdtos.RequestLogResponseDto;
import ucr.ac.cr.apisandbox.exceptions.ConflictException;
import ucr.ac.cr.apisandbox.exceptions.ResourceNotFoundException;
import ucr.ac.cr.apisandbox.models.MockEndpoint;
import ucr.ac.cr.apisandbox.models.RequestLog;
import ucr.ac.cr.apisandbox.models.Sandbox;
import ucr.ac.cr.apisandbox.repositories.IRequestLogRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestLogServices {
    private final IRequestLogRepository repository;
    private final SandboxServices sandboxServices;
    private final MockEndpointServices mockEndpointServices;
    /**
     * Trae la lista de logs de un sandbox en especifico
     * @param sandboxId
     * @param limit
     * @return List<RequestLogListDto>
     */
    public List<RequestLogListDto> findAllBySandbox(UUID sandboxId,
                                                 Integer limit) {
        /**
         * Si el usuario no indica un
         * limite se traen 50, si no el maximo es 500 y el minimo es 1
         */
        int safeLimit = limit == null ? 50 : Math.min(Math.max(limit,1), 500);

        Pageable pageable = PageRequest.of(0,
                safeLimit,
                Sort.by("createdAt").descending());


        return repository.findBySandbox_Id(sandboxId,pageable).stream()
                .map(rl->RequestLogListDto.builder()
                        .id(rl.getId())
                        .sandboxId(rl.getSandbox().getId())
                        .endpointId(rl.getEndpoint().getId())
                        .method(rl.getMethod())
                        .path(rl.getPath())
                        .timestamp(rl.getTimestamp())
                        .responseTimeMs(rl.getResponseTimeMs())
                        .build())
                .toList();
    }

    /**
     * Trae la lista de logs de un endpoint especifico
     * @param sandboxId
     * @param endpointId
     * @param limit
     * @return
     */
    public List<RequestLogListDto> findBySandboxAndEndpoint(UUID sandboxId,
                                                            UUID endpointId,
                                                            Integer limit) {

        /**
         * Validations
         */
        MockEndpoint endpoint = mockEndpointServices.getMockEndpoint(endpointId);
        mockEndpointServices.validateSandboxOwnerShip(sandboxId, endpoint);
        /**
         * Si el usuario no indica un
         * limite se traen 50, si no el maximo es 500 y el minimo es 1
         */
        int safeLimit = limit == null ? 50 : Math.min(Math.max(limit,1), 500);
        Pageable pageable = PageRequest.of(0,
                safeLimit,
                Sort.by("timestamp").descending());

        return repository.findByEndpoint_IdAndSandbox_Id(endpointId,sandboxId,pageable).stream()
                .map(rl->RequestLogListDto.builder()
                        .id(rl.getId())
                        .sandboxId(rl.getSandbox().getId())
                        .endpointId(rl.getEndpoint().getId())
                        .method(rl.getMethod())
                        .path(rl.getPath())
                        .timestamp(rl.getTimestamp())
                        .responseTimeMs(rl.getResponseTimeMs())
                        .build())
                .toList();
    }

    /**
     * Trae un log de un sandbox en especifico
     * @param sandboxId
     * @param id
     * @return RequestLogListDto
     */
    public RequestLogResponseDto findById(UUID sandboxId,
                                          UUID id) {
        /**
         * Validations
         */
        RequestLog requestLog = getRequestLog(id);
        validateSandboxOwner(sandboxId, requestLog);
        //--------------------------------------------------------------------
        return RequestLogResponseDto.builder()
                .id(requestLog.getId())
                .sandboxId(requestLog.getSandbox().getId())
                .endpointId(requestLog.getEndpoint().getId())
                .timestamp(requestLog.getTimestamp())
                .method(requestLog.getMethod())
                .path(requestLog.getPath())
                .queryString(requestLog.getQueryString())
                .requestHeaders(requestLog.getRequestHeaders())
                .requestBody(requestLog.getRequestBody())
                .responseStatus(requestLog.getResponseStatus())
                .responseTimeMs(requestLog.getResponseTimeMs())
                .build();
    }

    @Transactional
    public void save(Sandbox sandbox,
                     MockEndpoint endpoint,
                     HttpServletRequest request,
                     int responseStatus,
                     long startedAtMillis) {

        // Calculamos cuánto tardó el procesamiento del request
        int responseTimeMs = (int) (System.currentTimeMillis() - startedAtMillis);

        // Creamos request
        RequestLog newRequestLog = new RequestLog();

        // Guardamos relaciones para poder filtrar logs rápido por sandbox/endpoint
        newRequestLog.setSandbox(sandbox);
        newRequestLog.setEndpoint(endpoint);


        newRequestLog.setMethod(request.getMethod());
        newRequestLog.setPath(request.getRequestURI());
        newRequestLog.setQueryString(request.getQueryString());
        newRequestLog.setRequestHeaders(extractHeaders(request));
        newRequestLog.setResponseStatus(responseStatus);
        newRequestLog.setResponseTimeMs(responseTimeMs);

        // Guardamos en BD
        repository.save(newRequestLog);

    }

    /**
     * ---------------------Utilities-----------------
     */
    private RequestLog getRequestLog(UUID id) {
        return repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("RequestLog not found with id: " + id));
    }


    private void validateSandboxOwner (UUID sandboxId,
                                       RequestLog requestLog) {
        Sandbox sandbox = sandboxServices.getSandbox(sandboxId);
        if(!requestLog.getEndpoint().getId().equals(sandbox.getId())) {
            throw new ConflictException("The log id" + requestLog.getEndpoint().getId() + " doesn't belong to this Sandbox");
        }
    }


    private String extractHeaders(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();

        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);

            // Enmascarar cosas sensibles
            if ("authorization".equalsIgnoreCase(name) || "cookie".equalsIgnoreCase(name)) {
                value = "***";
            }

            sb.append(name).append(": ").append(value).append("\n");
        }

        return sb.toString();
    }

}
