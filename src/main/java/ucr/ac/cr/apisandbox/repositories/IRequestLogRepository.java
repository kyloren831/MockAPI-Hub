package ucr.ac.cr.apisandbox.repositories;

import io.micrometer.common.KeyValues;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ucr.ac.cr.apisandbox.models.RequestLog;

import java.util.List;
import java.util.UUID;


public interface IRequestLogRepository extends JpaRepository<RequestLog, UUID> {
    List<RequestLog> findBySandbox_Id (UUID sandboxId, Pageable pageable);

    List<RequestLog> findByEndpoint_IdAndSandbox_Id(UUID endpointId, UUID endpointSandboxId,Pageable pageable);

    KeyValues findByEndpoint_IdAndSandbox_Id(UUID endpointId, UUID sandboxId, Sort sort, Limit limit);
}
