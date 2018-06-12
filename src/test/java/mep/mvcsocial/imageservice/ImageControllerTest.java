package mep.mvcsocial.imageservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import mep.mvcsocial.imageservice.domain.Image;
import mep.mvcsocial.imageservice.service.ImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ImageController.class)
@RunWith(SpringRunner.class)
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ImageService imageService;
    private ObjectMapper objectMapper = new ObjectMapper();

    // test get
    @Test
    @WithMockUser
    public void getExistingFile_ReturnDbFile() throws Exception {
        Image testImage = new Image("id", "user", "file.jpg", 1L, 0);

        // when imageService return testImage
        when(imageService.getImageByUserId("user")).thenReturn(List.of(testImage));

        mockMvc.perform(get("/api/services/images?user=user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(testImage))));
    }

    @Test
    @WithMockUser
    public void getRawExistingFile_ReturnResource()  throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file.png", "blah".getBytes());
        File f = new File("uploads/file.png");
        mockFile.transferTo(f);
        Resource r = new FileSystemResource(f);

        // when imageService raw file return r
        when(imageService.getRawImageByFileId(any(String.class))).thenReturn(r);

        mockMvc.perform(get("/api/services/images/fileid/raw"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(mockFile.getBytes()));

        // cleanup files
        f.delete();
    }

    @Test
    @WithMockUser
    public void createValidFile_ReturnDbImage() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file.png", "blah".getBytes());
        Image testImage = new Image("id", "user", "file.jpg", 1L, 0);

        when(imageService.createImage(any(String.class), any(MultipartFile.class))).thenReturn(testImage);

        mockMvc.perform(post("/api/services/images").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(testImage)));
    }
}
