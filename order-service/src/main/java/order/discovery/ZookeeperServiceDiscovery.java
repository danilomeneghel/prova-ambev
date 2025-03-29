package order.discovery;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceProvider;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ZookeeperServiceDiscovery {

    private final ServiceDiscovery<String> serviceDiscovery;

    public ZookeeperServiceDiscovery(ServiceDiscovery<String> serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public Optional<ServiceInstance<String>> getServiceInstance(String serviceName) {
        try {
            return serviceDiscovery.queryForInstances(serviceName).stream().findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
