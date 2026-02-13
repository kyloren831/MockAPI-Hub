package ucr.ac.cr.apisandbox.models;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(
        name = "sandboxes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "slug")
        }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Sandbox {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    @Column(nullable = false,length = 120)
    private String name;
    @Column(nullable = false, unique = true, length = 120)
    private String slug; // URL friendly
    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name="updated_at",nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sandbox",
                targetEntity = MockEndpoint.class,
                cascade = CascadeType.ALL,
                orphanRemoval = true,
                fetch = FetchType.LAZY)
    private List<MockEndpoint> endpoints;
}
