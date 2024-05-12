package com.backskeleton.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final String uploadDir = "src/main/resources/static/images/";

    public String storeFile(MultipartFile file, String source) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String randomFileName = UUID.randomUUID().toString() + fileExtension;
        
        String directoryPath = uploadDir+source;

        if (!Files.exists(Paths.get(directoryPath))) {
            Files.createDirectories(Paths.get(directoryPath));
        }
    
        Path filePath = Paths.get(directoryPath, randomFileName);
        // System.out.println("Chemin absolu: " + filePath.toAbsolutePath());
        // System.out.println(filePath);
        Files.copy(file.getInputStream(), filePath);
        return  randomFileName;

    }

    public String updateFile(String fileName, MultipartFile file, String source) throws IOException {
        // Supprimez l'ancien fichier
        deleteFile(fileName);
        // Enregistrez le nouveau fichier
        return storeFile(file, source);
    }

    public void deleteFile(String fileName) throws IOException {
        fileName = fileName.split("/images/")[1];
        Path filePath = Paths.get(uploadDir + "/" + fileName);
        Files.deleteIfExists(filePath);
    }
}
