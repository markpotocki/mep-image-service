package mep.mvcsocial.imageservice.service;


import mep.mvcsocial.imageservice.domain.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Resource getRawImage(String filename);
    Image getImage(String imageId);
    void deleteImage(String imageId);
    Image editImage(String imageId, Image newImage);
    Image createImage(Image newImage, MultipartFile rawFile);

}
