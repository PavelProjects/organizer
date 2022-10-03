package com.povobolapo.organizer.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "_user")
public class UserEntity implements Serializable {
    //TODO добавить названия полей таблицы, что бы на них можно было ссылаться в коде

    @Id
    @GeneratedValue(generator = "_user_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "avatar")
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
