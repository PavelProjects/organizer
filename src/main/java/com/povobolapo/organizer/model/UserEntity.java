package com.povobolapo.organizer.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "_user")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "_user_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "login", length = 32, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "avatar", length = 128)
    private String avatar;


    public UserEntity() {
    }

    public UserEntity(Integer id, String login, String password, String name, String avatar) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
    }

    public UserEntity(String login, String password, String name, String avatar) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
    }

    public UserEntity(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public UserEntity(String login) {
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
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

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
