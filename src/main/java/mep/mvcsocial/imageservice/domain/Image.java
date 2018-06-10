package mep.mvcsocial.imageservice.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Image {

    private String id;
    @NonNull private String userId; // userid of picture owner
    @NonNull private String filename;

    // managment stuff
    private Long dateCreated = Instant.now().toEpochMilli();
    @JsonIgnore private Integer flagCount = 0;

}
