package product.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;

@Service
public class ZookeeperServiceRegistry implements Closeable {
    
    private final ServiceDiscovery<String> serviceDiscovery;

    public ZookeeperServiceRegistry(CuratorFramework curatorFramework) throws Exception {
        JsonInstanceSerializer<String> serializer = new JsonInstanceSerializer<>(String.class);

        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class)
                .client(curatorFramework)
                .basePath("/services")
                .serializer(serializer)
                .build();

        serviceDiscovery.start();
    }

    public void registerService(String serviceName, String address, int port) throws Exception {
        ServiceInstance<String> instance = ServiceInstance.<String>builder()
                .name(serviceName)
                .address(address)
                .port(port)
                .build();

        serviceDiscovery.registerService(instance);
    }

    @Override
    public void close() throws IOException {
        serviceDiscovery.close();
    }
    
}
