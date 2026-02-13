package ucr.ac.cr.apisandbox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucr.ac.cr.apisandbox.models.MockEndpoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMockEndpointRepository extends JpaRepository<MockEndpoint, UUID> {
    Optional<MockEndpoint> findByPathAndMethod(String path, String method);
    List<MockEndpoint> findAllBySandbox_Id(UUID sandbox_Id);
}
