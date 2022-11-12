package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.ContentDto;
import com.povobolapo.organizer.mapper.ContentInfoMapper;
import com.povobolapo.organizer.model.ContentInfoEntity;
import com.povobolapo.organizer.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.io.IOException;

@RestController
@RequestMapping("/content")
public class ContentController {
    private static final Logger log = LoggerFactory.getLogger(ContentController.class);

    private final ContentService contentService;
    private final ContentInfoMapper contentInfoMapper;

    @Autowired
    public ContentController(ContentService contentService,
                             ContentInfoMapper contentInfoMapper) {
        this.contentService = contentService;
        this.contentInfoMapper = contentInfoMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ContentDto uploadContent(@RequestParam MultipartFile file) throws IOException, AuthenticationException {
        log.info("POST uploadContent: {} {}", file.getName(), file.getContentType());
        ContentInfoEntity contentInfoEntity = contentService.createContent(file.getName(), file.getContentType(), file.getBytes());
        return contentInfoMapper.toDto(contentInfoEntity);
    }

    @GetMapping(produces = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Resource getContent(@RequestParam String contentInfoId) throws IOException {
        log.info("GET getContent: {}", contentInfoId);
        ContentInfoEntity contentInfoEntity = contentService.getContentByContentInfoId(contentInfoId);
        if (contentInfoEntity == null) {
            return null;
        }
        return new ByteArrayResource(contentService.getContentData(contentInfoEntity));
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ContentDto getContentInfo(@RequestParam String contentInfoId) throws IOException {
        log.info("GET getContentInfo: {}", contentInfoId);
        ContentInfoEntity contentInfoEntity = contentService.getContentByContentInfoId(contentInfoId);
        if (contentInfoEntity == null) {
            return null;
        }
        return contentInfoMapper.toDto(contentInfoEntity);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteContent(@RequestParam String contentInfoId) throws IOException, AuthenticationException {
        log.info("DELETE deleteContent: {}", contentInfoId);
        contentService.deleteContent(contentInfoId);
    }
}
