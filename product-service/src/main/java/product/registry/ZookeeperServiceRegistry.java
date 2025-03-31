package product.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;

@Service
public class ZookeeperServiceRegistry implements Closeable {

    private final ServiceDiscovery<String> serviceDiscovery;

    @Value("${zookeeper.host}")
    private String serverHost;

    @Value("${zookeeper.port}")
    private int serverPort;

    @Value("${product.service.name}")
    private String serviceName;

    public ZookeeperServiceRegistry(CuratorFramework curatorFramework) throws Exception {
        JsonInstanceSerializer<String> serializer = new JsonInstanceSerializer<>(String.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class)
                .client(curatorFramework)
                .basePath("/services")
                .serializer(serializer)
                .build();
        serviceDiscovery.start();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void registerOnStartup() {
        try {
            registerService(serviceName, serverHost, serverPort);
            System.out.printf("Service '%s' registered successfully at %s:%d%n", serviceName, serverHost, serverPort);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void registerService(String serviceName, String serverHost, int serverPort) throws Exception {
        ServiceInstance<String> instance = ServiceInstance.<String>builder()
                .name(serviceName)
                .address(serverHost)
                .port(serverPort)
                .build();
        serviceDiscovery.registerService(instance);
    }

    @Override
    public void close() throws IOException {
        serviceDiscovery.close();
    }
}
