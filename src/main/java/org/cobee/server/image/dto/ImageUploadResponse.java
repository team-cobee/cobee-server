package org.cobee.server.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {
    private String imageUrl;
    private String originalName;
    private Long fileSize;
    private String contentType;

}
