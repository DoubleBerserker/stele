package io.github.DoubleBerserker.stele.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ImageUploadServiceImplTest {

    private ImageUploadServiceImpl imageUploadService;
    
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // Initialize service with temp directory and 5MB max size
        imageUploadService = new ImageUploadServiceImpl(tempDir.toString(), 5242880L);
    }

    @Test
    void uploadImage_withValidJpegFile_shouldSucceed() {
        // Arrange
        byte[] content = "fake image content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                content
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("url"));
        assertTrue(response.getBody().get("url").contains("test.jpg"));
    }

    @Test
    void uploadImage_withValidPngFile_shouldSucceed() {
        // Arrange
        byte[] content = "fake png content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.png",
                "image/png",
                content
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void uploadImage_withEmptyFile_shouldReturnBadRequest() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                new byte[0]
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().get("error").contains("empty"));
    }

    @Test
    void uploadImage_withNullFile_shouldReturnBadRequest() {
        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    void uploadImage_withOversizedFile_shouldReturnBadRequest() {
        // Arrange - Create a file larger than 5MB
        byte[] content = new byte[5242881]; // 5MB + 1 byte
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "large.jpg",
                "image/jpeg",
                content
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().get("error").contains("size exceeds"));
    }

    @Test
    void uploadImage_withInvalidContentType_shouldReturnBadRequest() {
        // Arrange
        byte[] content = "fake content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.txt",
                "text/plain",
                content
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().get("error").contains("Invalid file type"));
    }

    @Test
    void uploadImage_withInvalidExtension_shouldReturnBadRequest() {
        // Arrange
        byte[] content = "fake content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.exe",
                "image/jpeg", // Content type is valid but extension is not
                content
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().get("error").contains("Invalid file extension"));
    }

    @Test
    void uploadImage_withNoExtension_shouldReturnBadRequest() {
        // Arrange
        byte[] content = "fake content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "testfile",
                "image/jpeg",
                content
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    void uploadImage_withWebpFile_shouldSucceed() {
        // Arrange
        byte[] content = "fake webp content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.webp",
                "image/webp",
                content
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void uploadImage_withGifFile_shouldSucceed() {
        // Arrange
        byte[] content = "fake gif content".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.gif",
                "image/gif",
                content
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void uploadImage_withSvgFile_shouldReturnBadRequest() {
        // Arrange
        byte[] content = "<svg></svg>".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.svg",
                "image/svg+xml",
                content
        );

        // Act
        ResponseEntity<Map<String, String>> response = imageUploadService.uploadImage(file);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertTrue(response.getBody().get("error").contains("Invalid file type"));
    }
}
