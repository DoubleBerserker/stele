package io.github.DoubleBerserker.stele.controller;

import io.github.DoubleBerserker.stele.services.ImageUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/image/")
public class ImageController {

    private final ImageUploadService imageUploadService;

    @Value("${app.imageUpload.apiKey:}")
    private String uploadApiKey;

    ImageController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey) {

        // Simple authorization: require a valid API key header to allow uploads.
        if (uploadApiKey != null && !uploadApiKey.isEmpty()) {
            if (apiKey == null || !uploadApiKey.equals(apiKey)) {
                Map<String, String> errorBody = new HashMap<>();
                errorBody.put("error", "Forbidden");
                errorBody.put("message", "Invalid or missing API key for image upload.");
                return ResponseEntity.status(403).body(errorBody);
            }
        }
        System.out.println("Reached controller");
        return imageUploadService.uploadImage(file);
    }
}
