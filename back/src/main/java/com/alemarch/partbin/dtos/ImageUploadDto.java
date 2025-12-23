package com.alemarch.partbin.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ImageUploadDto {
    @NotNull
    @Size(min = 1, max = 10, message = "Provide 1-10 images")
    private List<@NotNull MultipartFile> files;
}
