package mep.mvcsocial.imageservice.service;

import mep.mvcsocial.imageservice.ImageProperties;
import mep.mvcsocial.imageservice.domain.Image;
import mep.mvcsocial.imageservice.domain.ImageRepo;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ImageServiceImplTest {

    @Test
    public void validFile_SaveCorrectly() throws IOException {
        String filename = UUID.randomUUID().toString();

        // setup mocks
        MockMultipartFile mockFile = new MockMultipartFile(filename, "blah", MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
        ImageRepo mockImageRepo = mock(ImageRepo.class);
        ImageProperties imageProperties = new ImageProperties();
        // create test service
        ImageServiceImpl imageService = new ImageServiceImpl(mockImageRepo, imageProperties);
        imageService.init();

        Image testImage = new Image("mark", filename);
        // when mockRepo called return saved image
        when(mockImageRepo.save(any(Image.class))).thenReturn(testImage);

        // test
        imageService.createImage("mark", mockFile);

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
        String fileId = UUID.randomUUID().toString();

        // setup mocks
        ImageRepo mockImageRepo = mock(ImageRepo.class);
        ImageProperties imageProperties = new ImageProperties();

        when(mockImageRepo.findById(any(String.class))).thenReturn(Optional.of(new Image(fileId, "foo", "test", 1L, 0)));
        // create test service
        ImageServiceImpl imageService = new ImageServiceImpl(mockImageRepo, imageProperties);
        imageService.init();

        // save test file
        Path p = Paths.get(imageProperties.getDirectory(), "test");
        p.toFile().createNewFile();

        // load raw file
        Resource imageRes = imageService.getRawImageByFileId(fileId);

        // verify loaded correctly
        assertTrue(imageRes.exists());
        assertTrue(imageRes.isReadable());
        assertTrue(imageRes.isFile());
        assertEquals("test", imageRes.getFilename());

        // cleanup
        fileCleanup(p);
    }

    private boolean fileCleanup(Path pathToDirectory) throws IOException {
        return Files.deleteIfExists(pathToDirectory);
    }
}
