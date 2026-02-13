package ucr.ac.cr.apisandbox.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "request_logs",
       indexes = {
            @Index(name = "idx_req_enpoint_ts", columnList = "endpoint_id, ts"),
            @Index(name = "idx_req_sandbox_ts", columnList = "sandbox_id, ts")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RequestLog {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    /**
     * Sandbox al que pertenece el request
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sandbox_id", nullable = false)
    private Sandbox sandbox;
    /**
     * Sandbox al que pertenece el request
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "endpoint_id", nullable = false)
    private MockEndpoint endpoint;

    /**
     * Timestamp exacto en el que se recibe el request
     */
    @Column(name = "ts", nullable = false)
    private Instant timestamp;

    /**
     *  Metodo HTTP (GET, POST, PUT, DELETE, etc.)
     */
    @Column(length = 10, nullable = false)
    private String method;

    /**
     * Path solicitado
     */
    @Column(length = 255, nullable = false)
    private String path;

    /**
     * Query string completa (?a=1&b=2)
     */
    @Column(name = "query_string", columnDefinition = "text")
    private String queryString;

    /**
     * Headers del request serializados como texto
     */
    @Column(name = "request_headers", columnDefinition = "text")
    private String requestHeaders;

    /**
     * Body del request (si aplica)
     */
    @Column(name = "request_body", columnDefinition = "text")
    private String requestBody;

    /**
     * Status HTTP devuelto
     */
    @Column(name = "response_status", nullable = false)
    private int responseStatus;

    /**
     * Tiempo total de respuesta en milisegundos
     */
    @Column(name = "response_time_ms", nullable = false)
    private int responseTimeMs;

    @PrePersist
    void onCreate() {
        this.timestamp = Instant.now();
    }

}
