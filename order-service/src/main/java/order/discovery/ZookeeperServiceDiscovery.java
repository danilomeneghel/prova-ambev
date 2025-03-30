package order.discovery;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZookeeperServiceDiscovery {

    @Value("${zookeeper.host}")
    private String zookeeperHost;

    @Value("${zookeeper.port}")
    private int zookeeperPort;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String discoverServiceUrl(String serviceName) {
        String zookeeperConnectionString = zookeeperHost + ":" + zookeeperPort;

        try (CuratorFramework client = CuratorFrameworkFactory.newClient(
                zookeeperConnectionString, new ExponentialBackoffRetry(1000, 3))) {
            
            client.start();

            String servicePath = "/services/" + serviceName;
            List<String> instances = client.getChildren().forPath(servicePath);

            if (!instances.isEmpty()) {
                String instancePath = servicePath + "/" + instances.get(0);
                byte[] data = client.getData().forPath(instancePath);

                JsonNode jsonNode = objectMapper.readTree(data);
                String host = jsonNode.get("address").asText();
                int port = jsonNode.get("port").asInt();

                return "http://" + host + ":" + port;
            } else {
                throw new RuntimeException("No instance of " + serviceName + " found in Zookeeper.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error discovering service in Zookeeper: " + e.getMessage(), e);
        }
    }
}
