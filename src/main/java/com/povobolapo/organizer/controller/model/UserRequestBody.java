package com.povobolapo.organizer.controller.model;

import com.povobolapo.organizer.model.UserEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequestBody {

    @Size(max = 32)
    @NotNull
    private String login;
    @Size(max = 32)
    private String password;
    @Size(max = 64)
    private String name;

    private String avatar;

    public UserRequestBody() {
    }

    public UserRequestBody(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public UserEntity toUser() {
        UserEntity user = new UserEntity();
        user.setLogin(this.login);
        user.setPassword(this.password);
        if (this.name == null || this.name.isBlank()) {
            user.setName(this.login);
        } else {
            user.setName(this.name);
        }
        if (this.avatar != null) {
            user.setAvatar(avatar);
        }
        return user;
    }
}
