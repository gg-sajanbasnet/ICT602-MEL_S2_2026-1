
package smartcommunity.persistence;

import smartcommunity.domain.ServiceRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryServiceRequestRepository implements ServiceRequestRepository {

    private final Map<String, ServiceRequest> requests = new LinkedHashMap<>();

    @Override
    public void save(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }

        requests.put(request.getRequestId(), request);
    }

    @Override
    public Optional<ServiceRequest> findById(String requestId) {
        return Optional.ofNullable(requests.get(requestId));
    }

    @Override
    public List<ServiceRequest> findAll() {
        return new ArrayList<>(requests.values());
    }
}
