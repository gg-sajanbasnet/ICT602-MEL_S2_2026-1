
package smartcommunity.persistence;

import smartcommunity.domain.ServiceRequest;
import java.util.List;
import java.util.Optional;

public interface ServiceRequestRepository {

    void save(ServiceRequest request);

    Optional<ServiceRequest> findById(String requestId);

    List<ServiceRequest> findAll();
}
