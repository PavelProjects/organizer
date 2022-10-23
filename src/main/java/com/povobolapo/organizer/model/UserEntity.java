package com.povobolapo.organizer.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "_user")
public class UserEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "login", length = 32, nullable = false, unique = true)
    private String login;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "avatar", length = 128)
    private String avatar;


    public UserEntity() {
    }

    public UserEntity(String id, String login, String name, String avatar) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.avatar = avatar;
    }

    public UserEntity(String login, String name, String avatar) {
        this.login = login;
        this.name = name;
        this.avatar = avatar;
    }

    public UserEntity(String login, String name) {
        this.login = login;
        this.name = name;
    }

    public UserEntity(String login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
