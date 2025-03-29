package product.discovery;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ZookeeperServiceDiscovery {

    private final ServiceDiscovery<String> serviceDiscovery;

    public ZookeeperServiceDiscovery(ServiceDiscovery<String> serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public Optional<ServiceInstance<String>> getServiceInstance(String serviceName) {
        try {
            Collection<ServiceInstance<String>> instances = serviceDiscovery.queryForInstances(serviceName);
            return instances.stream().findFirst(); 
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
}
