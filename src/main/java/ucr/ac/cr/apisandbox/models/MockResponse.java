package ucr.ac.cr.apisandbox.models;

import jakarta.persistence.*;
import lombok.*;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.databind.JsonNode;


import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "mock_responses")
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MockResponse {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="endpoint_id",nullable = false)
    private MockEndpoint endpoint;
    @Column(nullable = false, name = "status_code")
    private int statusCode; // Ej: 200, 404, 500

    @Column(columnDefinition = "jsonb", name = "body_json")
    @Type(JsonType.class)
    private JsonNode bodyJson;
    @Column(name = "content_type",nullable = false,length = 80)
    private String contentType="application/json";
    @Column(name = "delay_ms",nullable = false)
    private int delayMs; //Simular latencia

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        var now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }
    @PreUpdate
    void onUpdate() {updatedAt = Instant.now();}
}
