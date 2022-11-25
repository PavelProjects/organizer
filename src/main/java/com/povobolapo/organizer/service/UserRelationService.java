package com.povobolapo.organizer.service;

import com.povobolapo.organizer.controller.model.user.UserRelationSearch;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.model.UserEntity;
import com.povobolapo.organizer.model.UserRelationEntity;
import com.povobolapo.organizer.repository.UserRelationRepository;
import com.povobolapo.organizer.repository.specification.UserRelationSpecification;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserRelationService {
    private final UserService userService;
    private final UserRelationRepository userRelationRepository;
    private final NotificationService notificationService;

    @Autowired
    public UserRelationService(UserService userService,
                               UserRelationRepository userRelationRepository,
                               NotificationService notificationService) {
        this.userService = userService;
        this.userRelationRepository = userRelationRepository;
        this.notificationService = notificationService;
    }

    public List<UserRelationEntity> getRelations(UserRelationSearch searchRequest) throws AuthenticationException {
        String login = StringUtils.isBlank(searchRequest.getLogin()) ? UserAuthoritiesService.getCurrentUserLogin() : searchRequest.getLogin();
        if (!UserAuthoritiesService.canReadUserData(login)) {
            throw new AccessDeniedException("Current user can't read data of " + login);
        }

        Page<UserRelationEntity> requestEntities =  userRelationRepository.findAll(
                UserRelationSpecification.userRelations(findAndCheckUser(login),
                        StringUtils.isBlank(searchRequest.getRelationType()) ? null : UserRelationEntity.RelationType.valueOf(searchRequest.getRelationType())),
                PageRequest.of(
                        searchRequest.getPage(),
                        searchRequest.getSize(),
                        Sort.by(Sort.Direction.fromString(searchRequest.getSort()), searchRequest.getSortBy())
                )
        );
        return requestEntities.getContent();
    }

    @Transactional
    public void sendRelationRequest(String toLogin) throws AuthenticationException {
        String currentUser = UserAuthoritiesService.getCurrentUserLogin();
        if (isRelationExist(currentUser, toLogin)) {
            log.debug("Relation between {} and {} already exist", currentUser, toLogin);
            return;
        }
        if (StringUtils.equals(toLogin, currentUser)) {
            return;
        }

        UserEntity toUser = userService.getUserByLogin(toLogin);
        if (toUser == null) {
            throw new NotFoundException("Can't found user " + toLogin);
        }
        UserEntity fromUser = userService.getUserByLogin(currentUser);

        userRelationRepository.save(new UserRelationEntity(toUser, fromUser, UserRelationEntity.RelationType.REQUEST));
        notificationService.createNotification(
                toUser,
                fromUser,
                "New friend request from user " + fromUser.getName(),
                NotificationService.NotifyTypes.USER
        );
        log.info("Created new friend request from {} to {} user", fromUser, toUser);
    }

    @Transactional
    public void approveRelation(String relationId) throws AuthenticationException {
        UserRelationEntity userRelationEntity = updateRelationRequest(relationId, UserRelationEntity.RelationType.FRIEND);
        notificationService.createNotification(
                userRelationEntity.getSecondUser(),
                userRelationEntity.getFirstUser(),
                userRelationEntity.getSecondUser().getName() + " accepted friend request from you!",
                NotificationService.NotifyTypes.USER
        );
        log.info("User relation {} was approved", relationId);
    }

    @Transactional
    public void declineRelation(String relationId) throws AuthenticationException {
        updateRelationRequest(relationId, UserRelationEntity.RelationType.IGNORE);
        log.info("User relation request {} was declined", relationId);
    }

    @Transactional
    public void removeRelation(String login) throws AuthenticationException {
        String currentUser = UserAuthoritiesService.getCurrentUserLogin();
        List<UserRelationEntity> relations = userRelationRepository.findAll(
                UserRelationSpecification.relationBetween(findAndCheckUser(currentUser), findAndCheckUser(login)));
        if (relations.isEmpty()) {
            throw new NotFoundException("Can't found relation between current user and " + login);
        }
        for (UserRelationEntity relationEntity: relations ) {
            if (!UserAuthoritiesService.canDeleteUserData(relationEntity.getFirstUser().getLogin()) ||
                    !UserAuthoritiesService.canDeleteUserData(relationEntity.getSecondUser().getLogin())) {
                throw new AccessDeniedException("Current user can't remove this user relation");
            }
            userRelationRepository.delete(relationEntity);
            log.warn("User relation {} was deleted by {}", relationEntity, currentUser);
        }
    }

    private UserEntity findAndCheckUser(String login) {
        UserEntity user = userService.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundException("Can't found user " + login);
        }
        return user;
    }

    private UserRelationEntity updateRelationRequest(String relationId, UserRelationEntity.RelationType relationType) throws AuthenticationException {
        Optional<UserRelationEntity> userRelation = userRelationRepository.findById(relationId);
        if (userRelation.isEmpty()) {
            throw new NotFoundException("Relation not found " + relationId);
        }
        UserRelationEntity userRelationEntity = userRelation.get();

        if (!UserAuthoritiesService.canModifyUserData(userRelationEntity.getSecondUser().getLogin())) {
            throw new AccessDeniedException("Current user can't approve this relation");
        }

        userRelationEntity.setRelationType(relationType.name());
        userRelationRepository.save(userRelationEntity);
        return userRelationEntity;
    }

    private boolean isRelationExist(String fromUser, String toUser) {
        return userRelationRepository.exists(UserRelationSpecification.relationBetween(findAndCheckUser(fromUser), findAndCheckUser(toUser)));
    }

}
