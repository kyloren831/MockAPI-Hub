package ucr.ac.cr.apisandbox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucr.ac.cr.apisandbox.models.Sandbox;

import java.util.Optional;
import java.util.UUID;

public interface ISandboxRepository extends JpaRepository<Sandbox, UUID> {
    Optional<Sandbox> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
