package io.github.DoubleBerserker.stele.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImageUploadService {

    ResponseEntity<Map<String, String>> uploadImage(MultipartFile imageFile);

}
