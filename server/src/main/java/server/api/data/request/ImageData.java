package server.api.data.request;


import org.springframework.web.multipart.MultipartFile;

public record ImageData(MultipartFile image) {

}
