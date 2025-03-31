package product.registry;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;

@Service
public class ZookeeperServiceRegistry implements Closeable {

    private final ServiceDiscovery<String> serviceDiscovery;
    private final String serviceName;
    private final String serviceHost;
    private final int servicePort;
    private final String basePath;

    public ZookeeperServiceRegistry(
            CuratorFramework client,
            @Value("${product.service.name}") String serviceName,
            @Value("${product.service.host}") String serviceHost,
            @Value("${product.service.port}") int servicePort,
            @Value("${product.service.basePath}") String basePath) throws Exception {

        if (serviceName == null || serviceName.isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty.");
        }
        if (basePath == null || basePath.isEmpty()) {
            throw new IllegalArgumentException("Base path cannot be null or empty.");
        }
        if (serviceHost == null || serviceHost.isEmpty()) {
            throw new IllegalArgumentException("Service host cannot be null or empty.");
        }

        this.serviceName = serviceName;
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.basePath = basePath;

        ServiceInstance<String> instance = ServiceInstance.<String>builder()
                .name(serviceName)
                .address(serviceHost)
                .port(servicePort)
                .build();

        JsonInstanceSerializer<String> serializer = new JsonInstanceSerializer<>(String.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class)
                .client(client)
                .basePath(basePath)
                .serializer(serializer)
                .thisInstance(instance)
                .build();
    }

    public void registerService() throws Exception {
        ServiceInstance<String> instance = ServiceInstance.<String>builder()
                .name(serviceName)
                .address(serviceHost)
                .port(servicePort)
                .build();

        serviceDiscovery.registerService(instance);
        System.out.printf("Service '%s' registered successfully at %s:%d%n", serviceName, serviceHost, servicePort);
    }

    @PostConstruct
    public void start() {
        try {
            serviceDiscovery.start();
            System.out.println("Zookeeper service discovery started successfully.");
        } catch (Exception e) {
            System.err.println("Error starting Zookeeper service discovery: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void close() {
        try {
            serviceDiscovery.close();
            System.out.println("Zookeeper service discovery closed successfully.");
        } catch (IOException e) {
            System.err.println("Error closing Zookeeper service discovery: " + e.getMessage());
        }
    }
}
