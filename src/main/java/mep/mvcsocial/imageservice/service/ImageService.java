package mep.mvcsocial.imageservice.service;


import mep.mvcsocial.imageservice.domain.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    Resource getRawImageByFileId(String fileId);
    Image getImage(String imageId);
    void deleteImage(String imageId);
    Image createImage(String userId, MultipartFile rawFile);
    List<Image> getImageByUserId(String userId);

}
