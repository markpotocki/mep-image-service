package mep.mvcsocial.imageservice;


import lombok.RequiredArgsConstructor;
import mep.mvcsocial.imageservice.domain.Image;
import mep.mvcsocial.imageservice.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/services/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<?> getImagesByUser(@RequestParam(name = "user", required = true) String userId) {
        return ResponseEntity.ok(imageService.getImageByUserId(userId));
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable String imageId) {
        Image retrieved = imageService.getImage(imageId);
        return ResponseEntity.ok(retrieved);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createImage(Principal principal, MultipartFile file) {
        Image image = imageService.createImage(principal.getName(), file);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(image);
    }

    @GetMapping(value = "/{imageId}/raw", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
    public Resource getRawFile(@PathVariable String imageId) {
        return imageService.getRawImageByFileId(imageId);
    }
}
