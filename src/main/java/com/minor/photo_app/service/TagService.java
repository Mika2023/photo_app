package com.minor.photo_app.service;

import com.minor.photo_app.dto.response.TagFullResponse;
import com.minor.photo_app.entity.Tag;
import com.minor.photo_app.mapper.TagMapper;
import com.minor.photo_app.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagFullResponse> findAll() {
        List<Tag> tags = tagRepository.findAll();
        return tagMapper.toFullResponseList(tags);
    }
}
