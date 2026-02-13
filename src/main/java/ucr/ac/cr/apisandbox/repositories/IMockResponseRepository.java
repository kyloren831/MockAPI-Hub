package ucr.ac.cr.apisandbox.repositories;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ucr.ac.cr.apisandbox.models.MockResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IMockResponseRepository extends JpaRepository<MockResponse, UUID> {
    List<MockResponse> findByEndpoint_Id(UUID endpointId);
    Optional<MockResponse> findByEnabledAndEndpoint_Id(Boolean enabled, UUID endpointId);

}
