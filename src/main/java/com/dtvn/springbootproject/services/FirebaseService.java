package com.dtvn.springbootproject.services;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
public interface FirebaseService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
    String generateToken();
    String generateFileName(MultipartFile multiPart);
}
