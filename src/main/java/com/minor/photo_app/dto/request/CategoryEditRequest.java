package com.minor.photo_app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEditRequest {
    private String name;
    private String description;
    private String imageUrl;
}
