package mep.mvcsocial.imageservice.service;

import mep.mvcsocial.imageservice.ImageProperties;
import mep.mvcsocial.imageservice.domain.Image;
import mep.mvcsocial.imageservice.domain.ImageRepo;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ImageServiceImplTest {

    @Test
    public void validFile_SaveCorrectly() throws IOException {
        String filename = UUID.randomUUID().toString();

        // setup mocks
        MockMultipartFile mockFile = new MockMultipartFile(filename, "test".getBytes());
        ImageRepo mockImageRepo = mock(ImageRepo.class);
        ImageProperties imageProperties = new ImageProperties();
        // create test service
        ImageServiceImpl imageService = new ImageServiceImpl(mockImageRepo, imageProperties);
        imageService.init();

        Image testImage = new Image("mark", filename);
        // when mockRepo called return saved image
        when(mockImageRepo.save(any(Image.class))).thenReturn(testImage);

        // test
        imageService.createImage(testImage, mockFile);

        // verify file exists
        Path pathToDirectory = Paths.get(imageProperties.getDirectory(), filename);
        boolean exists = Files.exists(pathToDirectory);
        assertTrue(exists);

        // verify method calls
        verify(mockImageRepo).save(any());

        // cleanup
        fileCleanup(pathToDirectory);
    }

    @Test
    public void validFile_LoadCorrectly() throws IOException {
        String filename = UUID.randomUUID().toString();

        // setup mocks
        ImageRepo mockImageRepo = mock(ImageRepo.class);
        ImageProperties imageProperties = new ImageProperties();
        // create test service
        ImageServiceImpl imageService = new ImageServiceImpl(mockImageRepo, imageProperties);
        imageService.init();

        // save test file
        Path p = Paths.get(imageProperties.getDirectory(), filename);
        p.toFile().createNewFile();

        // load raw file
        Resource imageRes = imageService.getRawImage(filename);

        // verify loaded correctly
        assertTrue(imageRes.exists());
        assertTrue(imageRes.isReadable());
        assertTrue(imageRes.isFile());
        assertEquals(filename, imageRes.getFilename());

        // cleanup
        fileCleanup(p);
    }

    private boolean fileCleanup(Path pathToDirectory) throws IOException {
        return Files.deleteIfExists(pathToDirectory);
    }
}