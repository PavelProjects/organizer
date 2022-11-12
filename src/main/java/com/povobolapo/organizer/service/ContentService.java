package com.povobolapo.organizer.service;

import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.model.ContentEntity;
import com.povobolapo.organizer.model.ContentInfoEntity;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.repository.ContentInfoRepository;
import com.povobolapo.organizer.repository.ContentRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContentService {
    private static final Logger log = LoggerFactory.getLogger(ContentService.class);

    // TODO create periodic task to delete content, that not in use

    private final StorageService storageService;
    private final ContentRepository contentRepository;
    private final ContentInfoRepository contentInfoRepository;
    private final UserService userService;

    @Autowired
    public ContentService(StorageService storageService,
                          ContentRepository contentRepository,
                          ContentInfoRepository contentInfoRepository,
                          UserService userService) {
        this.storageService = storageService;
        this.contentRepository = contentRepository;
        this.contentInfoRepository = contentInfoRepository;
        this.userService = userService;
    }

    @Transactional
    public ContentInfoEntity createContent(String fileName, String fileExtension, byte[] content) throws AuthenticationException, IOException {
        ContentEntity contentEntity = creteOrGetContent(content);
        ContentInfoEntity contentInfoEntity = contentInfoRepository.findByFileNameAndFileExtension(fileName, fileExtension);
        if (contentInfoEntity == null) {
            contentInfoEntity = new ContentInfoEntity();
        }

        UserEntity user = userService.getCurrentUser();
        contentInfoEntity.setContent(contentEntity);
        contentInfoEntity.setOwner(user);
        contentInfoEntity.setFileName(fixFileName(fileName));
        contentInfoEntity.setFileExtension(fileExtension);
        contentInfoRepository.save(contentInfoEntity);

        return contentInfoEntity;
    }

    public ContentInfoEntity getContent(String contentInfoId) {
        return contentInfoRepository.findById(contentInfoId).get();
    }

    @Transactional
    public void deleteContent(String contentInfoId) throws AuthenticationException, IOException {
        Optional<ContentInfoEntity> contentInfoEntity = contentInfoRepository.findById(contentInfoId);
        if (contentInfoEntity.isEmpty()) {
            throw new NotFoundException("Can't found content info by id " + contentInfoId);
        }
        if (!canManageContent(contentInfoEntity.get())) {
            throw new AccessDeniedException("Current user can't delete content " + contentInfoId);
        }

        contentInfoRepository.delete(contentInfoEntity.get());
        ContentEntity contentEntity = contentInfoEntity.get().getContent();

        List<ContentInfoEntity> contentInfoEntities = contentInfoRepository.findByContentId(contentEntity.getId());
        if (contentInfoEntities == null || contentInfoEntities.isEmpty()) {
            log.warn("Deleting content {}  by user {}", contentEntity.getId(), contentInfoEntity.get().getOwner());
            storageService.delete(contentEntity.getId());
            contentRepository.delete(contentEntity);
        }
    }

    private ContentEntity creteOrGetContent(byte[] content) throws IOException {
        int hashCode = Arrays.hashCode(content);
        ContentEntity contentEntity = contentRepository.findByHashCode(hashCode);
        if (contentEntity != null) {
            log.warn("Content already exist, skipping creation");
            return contentEntity;
        }
        contentEntity = new ContentEntity();
        contentEntity.setCreationDate(new Date());
        contentEntity.setHashCode(hashCode);
        contentRepository.save(contentEntity);

        storageService.save(contentEntity.getId(), content);

        return contentEntity;
    }

    private boolean canManageContent(ContentInfoEntity contentInfoEntity) throws AuthenticationException {
        UserEntity currentUser = userService.getCurrentUser();
        return StringUtils.equals(currentUser.getLogin(), contentInfoEntity.getOwner().getLogin());
    }

    private String fixFileName(String fileName) {
        return fileName.replace('/', '_');
    }
}
