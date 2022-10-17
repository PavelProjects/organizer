package com.povobolapo.organizer.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/***
 * WARNING!!!
 * ИСПОЛЬЗОВАТЬ ТОЛЬКО ДЛЯ АВТОРИЗАЦИИ!!!
 */

@Entity
@Table(name = "_user_credits")
public class UserCreditsEntity implements Serializable {
    @Id
    @GenericGenerator(name = "entity_id", strategy = "com.povobolapo.organizer.model.EntityIdGenerator")
    @GeneratedValue(generator = "entity_id")
    @Column(name = "id")
    private String id;

    @Column(name = "login", length = 32, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "mail", length = 128, nullable = false)
    private String mail;

    public UserCreditsEntity() {
    }

    public UserCreditsEntity(String login, String password, String mail) {
        this.login = login;
        this.password = password;
        this.mail = mail;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
