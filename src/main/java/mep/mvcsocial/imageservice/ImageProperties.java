package mep.mvcsocial.imageservice;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "service.domain")
public class ImageProperties {

    @Getter
    private int maxSize = 75000;
    @Getter
    private String directory = "uploads";
}
