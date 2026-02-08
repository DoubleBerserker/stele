package io.github.DoubleBerserker.stele.services.impl;

import io.github.DoubleBerserker.stele.services.ImageUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    private final String UPLOAD_DIR;

    ImageUploadServiceImpl (@Value("${image.uploadDir}") String UPLOAD_DIR) {
        this.UPLOAD_DIR = UPLOAD_DIR;
    }

    @Override
    public ResponseEntity<Map<String, String>> uploadImage(MultipartFile imageFile) {

        System.out.println("Reached service");
        try {
            System.out.println("Storing Image:");
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);

            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());

            Map<String, String> response = new HashMap<>();
            response.put("url", "/images/" + fileName);
            System.out.println("Stored image at: " + path);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

    }
}
