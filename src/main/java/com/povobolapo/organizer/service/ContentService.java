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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@Scope("singleton")
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

        UserEntity user = userService.getCurrentUser();
        ContentInfoEntity contentInfoEntity =
                new ContentInfoEntity(fixFileName(fileName), fileExtension, contentEntity, user.getLogin());
        contentInfoRepository.save(contentInfoEntity);

        return contentInfoEntity;
    }

    public ContentInfoEntity getContentInfo(String contentInfoId) {
        Optional<ContentInfoEntity> contentInfoEntity = contentInfoRepository.findById(contentInfoId);
        return contentInfoEntity.orElse(null);
    }

    public byte[] getContentData(ContentInfoEntity contentInfoEntity) throws IOException {
        Objects.requireNonNull(contentInfoEntity);
        return storageService.get(contentInfoEntity.getContent().getId());
    }

    @Transactional
    public void deleteContent(String contentInfoId) throws AuthenticationException, IOException {
        Optional<ContentInfoEntity> contentInfoEntity = contentInfoRepository.findById(contentInfoId);
        if (contentInfoEntity.isEmpty()) {
            throw new NotFoundException("Can't found content info by id " + contentInfoId);
        }
        // Только автор созданного контетна может его удалить
        if (!canManageContent(contentInfoEntity.get())) {
            throw new AccessDeniedException("Current user can't delete content " + contentInfoId);
        }
        // Сначала удаляем инфу из бд
        contentInfoRepository.delete(contentInfoEntity.get());
        ContentEntity contentEntity = contentInfoEntity.get().getContent();

        // Если контент более нигде не используется - удялем его полностью
        List<ContentInfoEntity> contentInfoEntities = contentInfoRepository.findByContentId(contentEntity.getId());
        if (contentInfoEntities == null || contentInfoEntities.isEmpty()) {
            log.warn("Deleting content {}  by user {}", contentEntity.getId(), contentInfoEntity.get().getOwner());
            // удаляем из бд
            contentRepository.delete(contentEntity);
            // стираем с диска
            storageService.delete(contentEntity.getId());
        }
    }

    public List<ContentInfoEntity> getUserContents() throws AuthenticationException {
        return contentInfoRepository.findByOwner(userService.getCurrentUser().getLogin());
    }

    private ContentEntity creteOrGetContent(byte[] content) throws IOException {
        // Сначала вычисляется хэш по байтам и проверяется, что контент с таким же хэшем раньше не создавался
        int hashCode = Arrays.hashCode(content);
        ContentEntity contentEntity = contentRepository.findByHashCode(hashCode);
        if (contentEntity != null) {
            log.warn("Content already exist, skipping creation");
            return contentEntity;
        }
        // Если такой контент ранее не создавался -  создаем
        contentEntity = new ContentEntity();
        contentEntity.setCreationDate(new Date());
        contentEntity.setHashCode(hashCode);
        contentRepository.save(contentEntity);
        // Непосредственное сохранение на диске сервера
        storageService.save(contentEntity.getId(), content);

        return contentEntity;
    }

    // TODO add role check
    private boolean canManageContent(ContentInfoEntity contentInfoEntity) throws AuthenticationException {
        UserEntity currentUser = userService.getCurrentUser();
        return StringUtils.equals(currentUser.getLogin(), contentInfoEntity.getOwner());
    }

    // Чистим название файла от посторнних символов
    private String fixFileName(String fileName) {
        return fileName.replace('/', '_').replace('\\', '_');
    }
}
