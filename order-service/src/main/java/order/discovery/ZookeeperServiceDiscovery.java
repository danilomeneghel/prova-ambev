package order.discovery;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ZookeeperServiceDiscovery {

    private final ServiceDiscovery<Object> serviceDiscovery;

    public ZookeeperServiceDiscovery(CuratorFramework client,
                                     @Value("${product.service.basePath}") String productServiceBasePath) {
        JsonInstanceSerializer<Object> serializer = new JsonInstanceSerializer<>(Object.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                .client(client)
                .basePath(productServiceBasePath)
                .serializer(serializer)
                .build();
    }

    @PostConstruct
    public void start() {
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start service discovery", e);
        }
    }

    @PreDestroy
    public void close() {
        try {
            serviceDiscovery.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close service discovery", e);
        }
    }

    public String discoverServiceUrl() throws Exception {
        Collection<ServiceInstance<Object>> instances = serviceDiscovery.queryForInstances("product-service");
        if (instances == null || instances.isEmpty()) {
            throw new RuntimeException("No instances available for service: product-service");
        }
        ServiceInstance<Object> instance = instances.iterator().next();
        return "http://" + instance.getAddress() + ":" + instance.getPort();
    }
    
}
