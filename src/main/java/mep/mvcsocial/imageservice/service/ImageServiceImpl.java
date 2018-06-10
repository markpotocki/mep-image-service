package mep.mvcsocial.imageservice.service;

import lombok.RequiredArgsConstructor;
import mep.mvcsocial.imageservice.ImageProperties;
import mep.mvcsocial.imageservice.domain.Image;
import mep.mvcsocial.imageservice.domain.ImageRepo;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepo imageRepo;
    private final ImageProperties imageProperties;
    private Path root;

    @Override
    public Image createImage(Image newImage, MultipartFile rawFile) {
        Image databaseImage = imageRepo.save(newImage);

        // verify size of domain
        if(imageProperties.getMaxSize() < rawFile.getSize())
            throw new FileTooLargeException("File is too large");

        // TODO saveImage to server
        File file = new File(root.resolve(databaseImage.getFilename()).toUri());
        try {
            rawFile.transferTo(file);
        } catch(IOException e) {
            e.printStackTrace();
        }
        // return created domain
        return databaseImage;
    }

    @Override
    public Image getImage(String imageId) {
        return imageRepo.findById(imageId).get();
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
                    root.resolve(image.getFilename()).toFile().delete();

                    imageRepo.delete(image);
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
