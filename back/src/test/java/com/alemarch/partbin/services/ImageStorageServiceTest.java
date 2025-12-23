package com.alemarch.partbin.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class ImageStorageServiceTest {

    @TempDir
    Path tempDir;
    
    private ImageStorageService imageStorageService;
    
    @BeforeEach
    void setUp() {
        imageStorageService = new ImageStorageService();
        // Override the upload directory for testing
        try {
            var uploadDirField = ImageStorageService.class.getDeclaredField("uploadDirectory");
            uploadDirField.setAccessible(true);
            uploadDirField.set(imageStorageService, tempDir.toString());
            
            var maxImagesField = ImageStorageService.class.getDeclaredField("maxImagesPerProduct");
            maxImagesField.setAccessible(true);
            maxImagesField.setInt(imageStorageService, 10);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up test", e);
        }
    }
    
    @Test
    void saveImages_ValidFiles_ReturnsPaths() throws IOException {
        // Arrange
        MultipartFile file1 = createMockFile("test1.jpg", "image/jpeg", "test content 1");
        MultipartFile file2 = createMockFile("test2.png", "image/png", "test content 2");
        List<MultipartFile> files = Arrays.asList(file1, file2);
        Long productId = 123L;
        
        // Act
        List<String> result = imageStorageService.saveImages(files, productId);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).startsWith("123/"));
        assertTrue(result.get(0).endsWith(".jpg"));
        assertTrue(result.get(1).startsWith("123/"));
        assertTrue(result.get(1).endsWith(".png"));
        
        // Verify files were actually created
        assertTrue(Files.exists(tempDir.resolve("123").resolve(result.get(0).substring(4))));
        assertTrue(Files.exists(tempDir.resolve("123").resolve(result.get(1).substring(4))));
    }
    
    @Test
    void saveImages_EmptyFile_ThrowsException() {
        // Arrange
        MultipartFile emptyFile = createMockFile("empty.jpg", "image/jpeg", "");
        List<MultipartFile> files = Arrays.asList(emptyFile);
        Long productId = 123L;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            imageStorageService.saveImages(files, productId);
        });
    }
    
    @Test
    void saveImages_InvalidFileType_ThrowsException() {
        // Arrange
        MultipartFile invalidFile = createMockFile("test.txt", "text/plain", "test content");
        List<MultipartFile> files = Arrays.asList(invalidFile);
        Long productId = 123L;
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            imageStorageService.saveImages(files, productId);
        });
    }
    
    @Test
    void deleteImage_ValidPath_DeletesFile() throws IOException {
        // Arrange
        MultipartFile file = createMockFile("test.jpg", "image/jpeg", "test content");
        List<MultipartFile> files = Arrays.asList(file);
        Long productId = 123L;
        
        List<String> savedPaths = imageStorageService.saveImages(files, productId);
        String imagePath = savedPaths.get(0);
        
        // Verify file exists
        assertTrue(Files.exists(tempDir.resolve(imagePath)));
        
        // Act
        imageStorageService.deleteImage(imagePath);
        
        // Assert
        assertFalse(Files.exists(tempDir.resolve(imagePath)));
    }
    
    @Test
    void isValidImage_ValidFile_ReturnsTrue() {
        // Arrange
        MultipartFile validFile = createMockFile("test.jpg", "image/jpeg", "test content");
        
        // Act & Assert
        assertTrue(imageStorageService.isValidImage(validFile));
    }
    
    @Test
    void isValidImage_InvalidFile_ReturnsFalse() {
        // Arrange
        MultipartFile invalidFile = createMockFile("test.txt", "text/plain", "test content");
        
        // Act & Assert
        assertFalse(imageStorageService.isValidImage(invalidFile));
    }
    
    @Test
    void deleteProductImages_ExistingFiles_DeletesAllFiles() throws IOException {
        // Arrange
        MultipartFile file1 = createMockFile("test1.jpg", "image/jpeg", "test content 1");
        MultipartFile file2 = createMockFile("test2.png", "image/png", "test content 2");
        List<MultipartFile> files = Arrays.asList(file1, file2);
        Long productId = 123L;
        
        imageStorageService.saveImages(files, productId);
        
        // Verify product directory exists
        assertTrue(Files.exists(tempDir.resolve("123")));
        
        // Act
        imageStorageService.deleteProductImages(productId);
        
        // Assert
        assertFalse(Files.exists(tempDir.resolve("123")));
    }
    
    private MultipartFile createMockFile(String filename, String contentType, String content) {
        return new MockMultipartFile(
            "files",
            filename,
            contentType,
            content.getBytes()
        );
    }
}