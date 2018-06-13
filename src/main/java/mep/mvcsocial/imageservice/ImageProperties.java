package mep.mvcsocial.imageservice;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "service.domain")
public class ImageProperties {

    @Getter
    private int maxSize = 75000;
    @Getter
    private String directory = "uploads";
}
