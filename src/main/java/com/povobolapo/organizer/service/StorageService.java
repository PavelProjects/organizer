package com.povobolapo.organizer.service;

import com.povobolapo.organizer.exception.StorageException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Scope("singleton")
public class StorageService {
    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private final Path BASE_PATH;
    private final int MAX_FILE_SiZE;

    @Autowired
    public StorageService(Environment env) throws IOException {
        String basePath = env.getProperty("storage.base.path");
        String maxFileSize = env.getProperty("storage.file.size.max");

        if (StringUtils.isBlank(basePath) || StringUtils.isBlank(maxFileSize)) {
            throw new RuntimeException("Some of storage properties are missing!");
        }

        BASE_PATH = Paths.get(basePath);
        MAX_FILE_SiZE = Integer.parseInt(maxFileSize);

        Files.createDirectories(BASE_PATH);
    }

    public String getBasePath() {
        return BASE_PATH.toString();
    }

    public String save(@NonNull @NotEmpty String contentId, @NotEmpty byte[] content) throws IOException {
        if (content.length > MAX_FILE_SiZE) {
            throw new StorageException("File is too large!");
        }
        Path path = Files.write(Paths.get(BASE_PATH.toString(), contentId), content);
        log.info("New file {} created!", path);
        return path.toString();
    }

    public byte[] get(@NonNull @NotEmpty String contentId) throws IOException {
        return Files.readAllBytes(Paths.get(BASE_PATH.toString(), contentId));
    }

    public boolean delete(@NotNull @NotEmpty String contentId) throws IOException {
        Path filePath = Paths.get(BASE_PATH.toString(), contentId);
        boolean result = Files.deleteIfExists(filePath);
        if (result) {
            log.warn("File {} was deleted!", filePath);
        }
        return result;
    }

    public void deleteAll() throws IOException {
        Files.delete(BASE_PATH);
        Files.createDirectories(BASE_PATH);
    }
}
