package com.minor.photo_app.service;

import com.minor.photo_app.mapper.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceMapper placeMapper;
}
