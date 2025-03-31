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
            System.out.println("Service discovery started successfully.");
        } catch (Exception e) {
            System.err.println("Error starting service discovery: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void close() {
        try {
            serviceDiscovery.close();
        } catch (Exception e) {
            System.err.println("Error closing service discovery: " + e.getMessage());
        }
    }

    public String discoverServiceUrl() throws Exception {
        Collection<ServiceInstance<Object>> instances = serviceDiscovery.queryForInstances("product-service");
        if (instances == null || instances.isEmpty()) {
            throw new RuntimeException("No instances available for service: product-service");
        }
        ServiceInstance<Object> instance = instances.iterator().next();
        String url = "http://" + instance.getAddress() + ":" + instance.getPort();
        System.out.println("URL service discovered: " + url);
        return url;
    }
    
}
