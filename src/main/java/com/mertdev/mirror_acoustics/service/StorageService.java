package com.mertdev.mirror_acoustics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String save(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Upload klasörünü oluştur (yoksa)
        Files.createDirectories(Path.of(uploadDir));

        // Dosya adını UUID + orijinal ad (boşluklar yerine _)
        String filename = UUID.randomUUID() + "-"
                + Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s", "_");

        Path target = Path.of(uploadDir, filename);

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // Geriye public erişim yolu döndür
        return "/uploads/" + filename;
    }
}
