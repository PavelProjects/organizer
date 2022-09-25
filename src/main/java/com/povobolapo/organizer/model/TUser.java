package com.povobolapo.organizer.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_user")
public class TUser implements Serializable {

    @Id
    @GeneratedValue(generator = "t_user_id_seq")
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


    public TUser() {
    }

    public TUser(Integer id, String login, String password, String name, String avatar) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
    }

    public TUser(String login, String password, String name, String avatar) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
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
}
