package order.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;

@Service
public class ZookeeperServiceDiscovery {

    private final ServiceDiscovery<Object> serviceDiscovery;
    private final CuratorFramework client;
    private final String productServiceBasePath;

    public ZookeeperServiceDiscovery(CuratorFramework client, @Value("${product.service.basePath}") String productServiceBasePath) {
        this.client = client;
        this.productServiceBasePath = productServiceBasePath;
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                .client(client)
                .basePath(productServiceBasePath)
                .serializer(new JsonInstanceSerializer<>(Object.class))
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
    public void stop() {
        try {
            serviceDiscovery.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to stop service discovery", e);
        }
    }

    public String discoverServiceUrl(String serviceName) throws Exception {
        Collection<ServiceInstance<Object>> instances = serviceDiscovery.queryForInstances(serviceName);
        if (instances == null || instances.isEmpty()) {
            throw new RuntimeException("No instances available for service: " + serviceName);
        }
        ServiceInstance<Object> instance = instances.iterator().next();
        return "http://" + instance.getAddress() + ":" + instance.getPort();
    }
}
