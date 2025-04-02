package order.discovery;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ZookeeperServiceDiscovery {

    private final DiscoveryClient discoveryClient;

    public ZookeeperServiceDiscovery(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String discoverServiceUrl(String serviceName) {
        List<String> instances = discoveryClient.getServices();
        if (instances.contains(serviceName)) {
            return discoveryClient.getInstances(serviceName).get(0).getUri().toString();
        }
        throw new RuntimeException("No instances available for service: " + serviceName);
    }
}
