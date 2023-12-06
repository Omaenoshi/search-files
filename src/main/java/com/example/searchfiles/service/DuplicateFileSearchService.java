package com.example.searchfiles.service;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

@Service
public interface DuplicateFileSearchService {
    MultiValueMap<String, String> findDuplicateTxtFiles(String directoryPath) throws IOException;
}
