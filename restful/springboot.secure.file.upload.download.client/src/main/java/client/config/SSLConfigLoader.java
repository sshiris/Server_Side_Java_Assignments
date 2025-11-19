package client.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
@Component
public class SSLConfigLoader {
    @Value("${ssl.trust-store}")
    private String trustStore;
    @Value("${ssl.trust-store-password}")
    private String trustStorePassword;
    @PostConstruct
    public void setSSLProperties() {
        System.setProperty("javax.net.ssl.trustStore", trustStore);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
    }
}