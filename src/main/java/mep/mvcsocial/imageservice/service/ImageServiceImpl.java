package mep.mvcsocial.imageservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mep.mvcsocial.imageservice.ImageProperties;
import mep.mvcsocial.imageservice.domain.Image;
import mep.mvcsocial.imageservice.domain.ImageRepo;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepo imageRepo;
    private final ImageProperties imageProperties;
    private Path root;

    @Override
    public Image createImage(String userId, MultipartFile rawFile) {
        Image toSave = new Image(userId, getRandomFileName(rawFile.getContentType()));
        Image databaseImage = imageRepo.save(toSave);

        // saveImage to server
        File file = new File(root.resolve(databaseImage.getFilename()).toUri());
        try {
            rawFile.transferTo(file);
        } catch(IOException e) {
            throw new StorageException("Failed to save file.");
        }
        // return created domain
        return databaseImage;
    }

    private String getRandomFileName(String contentType) {
        switch(contentType) {
            case MediaType.IMAGE_JPEG_VALUE:
                return UUID.randomUUID().toString() + ".jpg";
            case MediaType.IMAGE_PNG_VALUE:
                return UUID.randomUUID().toString() + ".png";
            default:
                throw new StorageException("File is not an image.");
        }
    }

    @Override
    public Image getImage(String imageId) {
        return imageRepo.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException("Image with id " + imageId + " not found"));
    }

    @Override
    public Image editImage(String imageId, Image newImage) {
        return null;
    }

    @Override
    public Resource getRawImage(String filename) {
        Path pathToFile = root.resolve(filename);

        // verify exists
        if(pathToFile.toFile().exists()) {
            return new PathResource(pathToFile);
        } else {
            return null;
        }
    }

    @Override
    public void deleteImage(String imageId) {
        imageRepo
                .findById(imageId)
                .ifPresent( image -> {
                    boolean isDeleted = root.resolve(image.getFilename()).toFile().delete();
                    if(isDeleted)
                        imageRepo.delete(image);
                    else
                        log.error("Failed to delete image " + imageId + " from database. Not found.");
                });
    }

    @PostConstruct
    void init() {
        String directory = imageProperties.getDirectory();
        root = Paths.get(directory);
        if(!root.toFile().exists())
            root.toFile().mkdir();
    }
}
