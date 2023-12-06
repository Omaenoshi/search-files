package com.example.searchfiles.service.impl;

import com.example.searchfiles.service.DuplicateFileSearchService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.security.MessageDigest;

@Service
public class DuplicateFileSearchServiceImpl implements DuplicateFileSearchService {
    @Override
    public MultiValueMap<String, String> findDuplicateTxtFiles(String directoryPath) throws IOException {
        MultiValueMap<String, String> duplicates = new LinkedMultiValueMap<>();
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            throw new IOException("Указанный путь не является директорией.");
        }

        List<File> txtFiles = listTxtFiles(directory);
        for (File file : txtFiles) {
            try {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                String contentHash = generateHash(fileContent);
                duplicates.add(contentHash, file.getName());
            } catch (IOException e) {
                throw new IOException("Ошибка чтения файла: " + file.getName());
            }
        }

        return filterDuplicates(duplicates);
    }

    private String generateHash(byte[] content) throws RuntimeException{
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content);
            return Arrays.toString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка при создании хеша файла", e);
        }
    }

    private List<File> listTxtFiles(File directory) {
        List<File> txtFiles = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                    txtFiles.add(file);
                } else if (file.isDirectory()) {
                    txtFiles.addAll(listTxtFiles(file));
                }
            }
        }

        return txtFiles;
    }

    private MultiValueMap<String, String> filterDuplicates(MultiValueMap<String, String> duplicates) {
        MultiValueMap<String, String> filteredDuplicates = new LinkedMultiValueMap<>();

        for (String hash : duplicates.keySet()) {
            List<String> files = duplicates.get(hash);
            if (files.size() > 1) {
                filteredDuplicates.put(hash, files);
            }
        }

        return filteredDuplicates;
    }
}
