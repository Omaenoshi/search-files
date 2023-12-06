package com.example.searchfiles.controller;

import com.example.searchfiles.service.DuplicateFileSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class HomeController {
    private final DuplicateFileSearchService duplicateFileSearchService;

    @Autowired
    public HomeController(DuplicateFileSearchService duplicateFileSearchService) {
        this.duplicateFileSearchService = duplicateFileSearchService;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("duplicates", null);

        return "index";
    }

    @PostMapping("search")
    public String searchDuplicates(@RequestParam("directoryPath") String directoryPath, Model model) {
        try {
            MultiValueMap<String, String> duplicates = duplicateFileSearchService.findDuplicateTxtFiles(directoryPath);
            model.addAttribute("duplicates", duplicates);
        } catch (IOException | RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "index";
    }
}
