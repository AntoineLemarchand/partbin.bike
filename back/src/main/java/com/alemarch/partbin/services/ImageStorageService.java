package com.alemarch.partbin.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {
    
    @Value("${partbin.upload.directory:/app/uploads/products}")
    private String uploadDirectory;
    
    @Value("${partbin.upload.max-images-per-product:10}")
    private int maxImagesPerProduct;
    
    @Value("${partbin.upload.max-file-size:10MB}")
    private String maxFileSizeConfig;
    
    private static final Set<String> ALLOWED_TYPES = Set.of("jpg", "jpeg", "png", "webp");
    private long maxFileSize;
    
    public ImageStorageService() {
        this.maxFileSize = 10 * 1024 * 1024; // 10MB default
    }
    
    public void setMaxFileSize(String maxFileSizeConfig) {
        this.maxFileSizeConfig = maxFileSizeConfig;
        this.maxFileSize = parseFileSize(maxFileSizeConfig);
    }
    
    public List<String> saveImages(List<MultipartFile> files, Long productId) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Files list cannot be null or empty");
        }
        
        if (files.size() > maxImagesPerProduct) {
            throw new IllegalArgumentException("Maximum " + maxImagesPerProduct + " images allowed per product");
        }
        
        // Validate all files before saving
        List<String> validationErrors = new ArrayList<>();
        for (MultipartFile file : files) {
            String error = validateFile(file);
            if (error != null) {
                validationErrors.add(error);
            }
        }
        
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("Validation errors: " + String.join(", ", validationErrors));
        }
        
        List<String> savedPaths = new ArrayList<>();
        List<Path> filesToRollback = new ArrayList<>();
        
        try {
            Path productDirectory = getProductDirectory(productId);
            
            for (MultipartFile file : files) {
                String fileName = generateUniqueFileName(file, productId);
                Path targetPath = productDirectory.resolve(fileName);
                
                // Save file
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                filesToRollback.add(targetPath);
                
                // Store relative path for database
                String relativePath = productId + "/" + fileName;
                savedPaths.add(relativePath);
            }
            
            return savedPaths;
            
        } catch (IOException e) {
            // Rollback: delete any files that were saved
            for (Path fileToDelete : filesToRollback) {
                try {
                    Files.deleteIfExists(fileToDelete);
                } catch (IOException ignored) {
                    // Ignore errors during rollback
                }
            }
            throw new RuntimeException("Failed to save images: " + e.getMessage(), e);
        }
    }
    
    public void deleteImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Image path cannot be null or empty");
        }
        
        // Validate path to prevent directory traversal
        Path uploadPath = Paths.get(uploadDirectory).normalize().toAbsolutePath();
        Path targetPath = uploadPath.resolve(imagePath).normalize().toAbsolutePath();
        
        if (!targetPath.startsWith(uploadPath)) {
            throw new IllegalArgumentException("Invalid image path: potential directory traversal");
        }
        
        try {
            Files.deleteIfExists(targetPath);
            
            // Try to delete the product directory if it's empty
            Path productDirectory = targetPath.getParent();
            if (productDirectory != null && Files.exists(productDirectory)) {
                try {
                    if (Files.list(productDirectory).findAny().isEmpty()) {
                        Files.delete(productDirectory);
                    }
                } catch (IOException ignored) {
                    // Directory not empty or other issue, ignore
                }
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage(), e);
        }
    }
    
    public void deleteProductImages(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        
        try {
            Path productDirectory = getProductDirectory(productId);
            if (Files.exists(productDirectory)) {
                Files.walk(productDirectory)
                    .sorted((a, b) -> b.compareTo(a)) // Delete files before directories
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                            // Ignore deletion errors during cleanup
                        }
                    });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete product images: " + e.getMessage(), e);
        }
    }
    
    public boolean isValidImage(MultipartFile file) {
        return validateFile(file) == null;
    }
    
    private String validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "File cannot be null or empty";
        }
        
        // Check file size
        if (file.getSize() > maxFileSize) {
            return "File size exceeds maximum allowed size of " + maxFileSizeConfig;
        }
        
        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "Filename cannot be null";
        }
        
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_TYPES.contains(fileExtension)) {
            return "File type not allowed. Allowed types: " + String.join(", ", ALLOWED_TYPES);
        }
        
        // Check content type
        String contentType = file.getContentType();
        if (contentType != null && !contentType.startsWith("image/")) {
            return "File must be an image";
        }
        
        return null; // No validation errors
    }
    
    private String generateUniqueFileName(MultipartFile file, Long productId) {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename(), "Filename cannot be null");
        String fileExtension = getFileExtension(originalFilename);
        
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        
        return timestamp + "_" + uuid + "." + fileExtension;
    }
    
    private Path getProductDirectory(Long productId) {
        try {
            Path productDir = Paths.get(uploadDirectory, productId.toString());
            if (!Files.exists(productDir)) {
                Files.createDirectories(productDir);
            }
            return productDir;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create product directory: " + e.getMessage(), e);
        }
    }
    
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
    
    private long parseFileSize(String size) {
        if (size == null) return 10 * 1024 * 1024; // Default 10MB
        
        size = size.toUpperCase().trim();
        try {
            if (size.endsWith("KB")) {
                return Long.parseLong(size.substring(0, size.length() - 2)) * 1024;
            } else if (size.endsWith("MB")) {
                return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024;
            } else if (size.endsWith("GB")) {
                return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024 * 1024;
            } else {
                return Long.parseLong(size);
            }
        } catch (NumberFormatException e) {
            return 10 * 1024 * 1024; // Default 10MB if parsing fails
        }
    }
}
