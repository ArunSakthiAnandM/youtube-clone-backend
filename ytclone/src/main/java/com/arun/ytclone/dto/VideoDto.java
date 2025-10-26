package com.arun.ytclone.dto;

import com.arun.ytclone.model.VideoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    private String id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    private String url;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    private String thumbnailUrl;

    private Set<String> tags;

    @NotNull(message = "Video status is required")
    private VideoStatus videoStatus;
}
