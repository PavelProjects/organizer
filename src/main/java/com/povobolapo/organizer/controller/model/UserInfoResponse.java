package com.povobolapo.organizer.controller.model;

import com.povobolapo.organizer.model.UserEntity;

import java.util.Objects;

public class UserInfoResponse {
    private String login;
    private String name;
    private String avatar;

    public UserInfoResponse() {
    }

    public UserInfoResponse(String login, String name, String avatar) {
        this.login = login;
        this.name = name;
        this.avatar = avatar;
    }

    public UserInfoResponse(UserEntity userEntity) {
        Objects.requireNonNull(userEntity, "User entity is null");
        login = userEntity.getLogin();
        name = userEntity.getName();
        avatar = userEntity.getAvatar();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
