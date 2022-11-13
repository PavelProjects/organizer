package com.povobolapo.organizer.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {
    private String contentInfoId;
    private String contentId;
    private String fileName;
    private String fileExtension;
    private String owner;
}
