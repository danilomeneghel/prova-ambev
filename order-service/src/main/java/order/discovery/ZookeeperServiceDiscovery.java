package order.discovery;

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

    public ZookeeperServiceDiscovery(CuratorFramework client, @Value("${zookeeper.basePath}") String basePath) {

        try {
            JsonInstanceSerializer<Object> serializer = new JsonInstanceSerializer<>(Object.class);
            this.serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                    .client(client)
                    .basePath(basePath)
                    .serializer(serializer)
                    .build();
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start service discovery", e);
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
