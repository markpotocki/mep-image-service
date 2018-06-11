package mep.mvcsocial.imageservice.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepo extends MongoRepository<Image, String> {
    Page<Image> findAllByUserId(String userId, Pageable page);

}
