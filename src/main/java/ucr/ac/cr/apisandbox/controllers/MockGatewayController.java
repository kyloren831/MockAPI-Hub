package ucr.ac.cr.apisandbox.controllers;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucr.ac.cr.apisandbox.services.MockGatewayService;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mock/{sandboxSlug}")
public class MockGatewayController {

    private final MockGatewayService mockGatewayService;

    @RequestMapping("/**")
    public ResponseEntity<?> handleMock(@PathVariable String sandboxSlug, HttpServletRequest request) {
        return mockGatewayService.handleMock(sandboxSlug, request);
    }
}
