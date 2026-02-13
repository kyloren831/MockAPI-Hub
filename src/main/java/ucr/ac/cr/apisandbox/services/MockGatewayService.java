package ucr.ac.cr.apisandbox.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;
import ucr.ac.cr.apisandbox.models.MockEndpoint;
import ucr.ac.cr.apisandbox.models.MockResponse;
import ucr.ac.cr.apisandbox.models.Sandbox;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MockGatewayService {

    private final MockResponseServices mockResponseServices;
    private final SandboxServices sandboxServices;
    private final MockEndpointServices mockEndpointServices;
    private final RequestLogServices requestLogServices;

    public ResponseEntity<?> handleMock(String sandboxSlug, HttpServletRequest request) {

        // Inicio de cronometro
        long startedAtMillis = System.currentTimeMillis();

        //Defaults por si algo sale mal responder bonito
        int statusToLog = 500;
        MockEndpoint resolvedEndpoint = null;
        Sandbox sandbox = null;

        try{
            // resolver sandboxpor slug
            sandbox = sandboxServices.getBySlug(sandboxSlug);
            //metodo real que llegó
            String method = request.getMethod();
            //Resolver path relativo
            String path = extractRelativePath(request, sandboxSlug);
            //Resolver endpoint
            resolvedEndpoint = mockEndpointServices.resolveEndpoint(sandbox.getId(), method, path);
            //Resolver response


            MockResponse mockResponse = mockResponseServices.getActiveMockResponse(resolvedEndpoint.getId());
            if(mockResponse == null){
                return ResponseEntity.status(404).body("None enabled MockResponses");
            }
            //Simular delay
            int delayMs = mockResponse.getDelayMs();
            if(delayMs > 0){
                try{
                    Thread.sleep(delayMs);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }

            //Construir respuesta HTTP
            statusToLog = mockResponse.getStatusCode();
            String contentType = Optional.ofNullable(mockResponse.getContentType())
                    .orElse(MediaType.APPLICATION_JSON_VALUE);
            String body = (mockResponse.getBodyJson() == null) ? "" : mockResponse.getBodyJson().toString();

            ResponseEntity<String> response = ResponseEntity
                    .status(statusToLog)
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(body);

            //Guardar log (Si falla el log no se detiene la respuesta
            try{
                requestLogServices.save(sandbox,
                        resolvedEndpoint,
                        request,
                        statusToLog,
                        startedAtMillis);
            }catch (Exception e){
                //Sin throw para que no se caiga el gateway
            }
            return  response;

        }catch(Exception e){
            // Cualquier otro fallo -> 500
            statusToLog = 500;

            try {
                if (sandbox != null && resolvedEndpoint != null) {
                    requestLogServices.save(sandbox, resolvedEndpoint, request, statusToLog, startedAtMillis);
                }
            } catch (Exception ignored) {}

            return ResponseEntity.status(500).body("Mock gateway error");
        }

    }

    /**
     * Saca el path real que el usuario quiso llamar, sin el prefijo /mock/{sandboxSlug}.
     * Ej:
     *   requestURI = /mock/frontend-qa/orders/2
     *   relative   = /orders/2
     *
     * Usamos atributos que Spring coloca cuando se usa "/**".
     */
    private String extractRelativePath(HttpServletRequest request, String sandboxSlug) {
        String pathWithinMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern  = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        // Normalmente:
        // bestMatchPattern  = /mock/{sandboxSlug}/**
        // pathWithinMapping = /mock/frontend-qa/orders/2
        // Queremos quitar "/mock/{slug}"
        String prefix = "/mock/" + sandboxSlug;

        if (pathWithinMapping != null && pathWithinMapping.startsWith(prefix)) {
            String relative = pathWithinMapping.substring(prefix.length());
            return relative.isBlank() ? "/" : relative;
        }

        // Fallback si algo raro pasa
        String uri = request.getRequestURI();
        if (uri.startsWith(prefix)) {
            String relative = uri.substring(prefix.length());
            return relative.isBlank() ? "/" : relative;
        }

        return "/";
    }
}
