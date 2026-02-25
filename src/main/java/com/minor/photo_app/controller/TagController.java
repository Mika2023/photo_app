package com.minor.photo_app.controller;

import com.minor.photo_app.dto.response.TagFullResponse;
import com.minor.photo_app.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public List<TagFullResponse> getAllTags() {
        return tagService.findAll();
    }
}
