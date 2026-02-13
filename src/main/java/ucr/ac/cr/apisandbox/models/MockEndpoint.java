package ucr.ac.cr.apisandbox.models;

import jakarta.persistence.*;
import lombok.*;

import javax.swing.text.StyledEditorKit;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mock_endpoints",
       uniqueConstraints = {
            @UniqueConstraint(
                    name = "uq_endpoint_per_sandbox",
                    columnNames = {"sandbox_id", "method", "path"}
            )
       },
       indexes = {
            @Index(name = "idx_endpoint_sandbox",
                   columnList = "sandbox_id"),
               @Index(name = "idx_endpoint_method_path",
                   columnList = "method,path")
       })
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MockEndpoint {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "sandbox_id",nullable = false)
    private Sandbox sandbox;
    @Column(nullable = false, length = 10)
    private String method; // GET|POST|PUT|PATCH|DELETE
    @Column(nullable = false, length = 255)
    private String path; // Ej: /users, /orders/{id}
    @Column(nullable = false)
    private boolean enabled = true;
    @Column(nullable = false, length = 255)
    private String description;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(targetEntity = MockResponse.class,
               mappedBy = "endpoint",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<MockResponse> responses;

    @PrePersist
    void onCreate() {
        var now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }
    @PreUpdate
    void onUpdate() {updatedAt = Instant.now();}
}
