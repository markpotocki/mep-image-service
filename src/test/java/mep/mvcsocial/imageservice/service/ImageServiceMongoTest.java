package mep.mvcsocial.imageservice.service;


import mep.mvcsocial.imageservice.domain.Image;
import mep.mvcsocial.imageservice.domain.ImageRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ImageServiceMongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ImageRepo imageRepo;


    @Test
    public void findImageByUserId() {
        // setup
        Image image = new Image("foo", "test", "file", Instant.now().toEpochMilli(), 0);
        mongoTemplate.save(image);

        Page<Image> actual = imageRepo.findAllByUserId("test", PageRequest.of(0, 10));
        List<Image> actualAsList = actual.getContent();

        assertTrue(actualAsList.contains(image)); // should contian saved image
        assertTrue(actualAsList.size() == 1); // should contain 1 element
    }
}
