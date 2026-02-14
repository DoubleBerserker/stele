package io.github.DoubleBerserker.stele.services.impl;

import io.github.DoubleBerserker.stele.services.ImageUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    private final String UPLOAD_DIR;
    private final long MAX_FILE_SIZE;
    
    // Allowed image MIME types
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp"
    );
    
    // Allowed image file extensions
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "webp"
    );

    ImageUploadServiceImpl (
            @Value("${image.uploadDir}") String UPLOAD_DIR,
            @Value("${image.maxFileSize:5242880}") long MAX_FILE_SIZE) { // Default: 5MB
        this.UPLOAD_DIR = UPLOAD_DIR;
        this.MAX_FILE_SIZE = MAX_FILE_SIZE;
    }

    @Override
    public ResponseEntity<Map<String, String>> uploadImage(MultipartFile imageFile) {

        System.out.println("Reached service");
        try {
            // Validate file is not empty
            if (imageFile == null || imageFile.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "File is empty or not provided");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Validate file size
            if (imageFile.getSize() > MAX_FILE_SIZE) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "File size exceeds maximum allowed size of " + (MAX_FILE_SIZE / 1024 / 1024) + "MB");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Validate content type
            String contentType = imageFile.getContentType();
            if (contentType == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid file type. Only image files are allowed (JPEG, PNG, GIF, WebP)");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (!ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid file type. Only image files are allowed (JPEG, PNG, GIF, WebP)");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            System.out.println("Storing Image:");
            String originalFilename = imageFile.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                originalFilename = "image";
            }
            // Strip any path information from the original filename
            originalFilename = Paths.get(originalFilename).getFileName().toString();
            
            // Validate file extension
            String fileExtension = getFileExtension(originalFilename);
            if (fileExtension.isEmpty() || !ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid file extension. Only image files are allowed (jpg, jpeg, png, gif, webp)");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            String fileName = UUID.randomUUID() + "_" + originalFilename;

            Path uploadRoot = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path path = uploadRoot.resolve(fileName).normalize();

            // Ensure the resolved path is still within the upload root to prevent path traversal
            if (!path.startsWith(uploadRoot)) {
                throw new SecurityException("Invalid file path");
            }

            Files.createDirectories(uploadRoot);
            imageFile.transferTo(path.toFile());

            Map<String, String> response = new HashMap<>();
            response.put("url", "/images/" + fileName);
            System.out.println("Stored image at: " + path);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

    }
    
    /**
     * Extracts the file extension from a filename
     * @param filename the filename to extract extension from
     * @return the file extension without the dot, or empty string if no extension
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}
